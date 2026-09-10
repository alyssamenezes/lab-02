package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities= cityRepository.cities,
                        onAddCity={ cityRepository.addCity(it) },
                        onDeleteCity={ cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun CityListScreen(
    //list of city names this screen receives from MainActivity
    cities: List<String>,
    onAddCity: (String)->Unit,
    onDeleteCity: (String)->Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf(value = "") }
    var isCityAdded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = { isCityAdded = true },
            ) {
                Text("ADD CITY")
            }

            Button(
                onClick = {
                    if (selectedCity != null) {
                        onDeleteCity(selectedCity!!)
                        //reset
                        selectedCity = null
                    }
                }
            ) { Text("DELETE CITY") }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            items(cities) { city ->
                CityRow(
                    city = city, isSelected = (city == selectedCity),
                    onSelect = { selectedCity = if (selectedCity == city) null else city }
                )
            }

        }

        if (isCityAdded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City Name") },
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                            isCityAdded = false
                        }
                    }
                ) { Text("CONFIRM") }

            }
        }

    }

}


@Composable
fun CityRow(city: String, isSelected: Boolean, onSelect:()->Unit) {
    // () -> Unit means a function type that takes no
    // parameters and returns no value
    Text(
        text = city,
        fontSize=28.sp,
        modifier=Modifier
            .fillMaxWidth()
            .background(if(isSelected) Color.Gray else Color.Transparent)
            .clickable { onSelect() }
            .padding(horizontal=18.dp,vertical=14.dp)


    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}

class CityRepository {
    private val _cities =  mutableStateListOf(
        "Edmonton","Vancouver", "Moscow", "Sydney","Berlin",
        "Vienna", "Tokyo", "Beijing","Osaka","New Delhi"
    )
    val cities: List<String>
        get()=_cities

    fun addCity(city:String){
        _cities.add(city)
    }

    fun deleteCity(city:String){
        _cities.remove(city)
    }
}