package com.example.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkMode = remember { mutableStateOf( false ) }
            DataStoreTheme ( darkTheme = darkMode.value ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting( darkMode = darkMode )
                }
            }
        }
    }
}

@Composable
fun Greeting ( darkMode: MutableState<Boolean> ) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserMail(context)

    val userMail = dataStore.getMail.collectAsState ( initial = "" )
    val isDarkMode = dataStore.getDarkMode.collectAsState ( initial = false )

    darkMode.value = isDarkMode.value

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var email by rememberSaveable { mutableStateOf ( "" ) }

        TextField (
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions().copy( keyboardType = KeyboardType.Email )
        )

        Spacer ( modifier = Modifier.height(16.dp) )

        Button ( onClick = {
            scope.launch {
                dataStore.saveMail( email )
            }
        } ) {
            Text ( text = "Guardar Email" )
        }

        Spacer ( modifier = Modifier.height(16.dp) )

        Text ( text = userMail.value )

        DarkMode ( darkMode = darkMode ) {
            scope.launch {
                dataStore.saveDarkMode(darkMode.value)
            }
        }
    }
}

@Composable
fun DarkMode ( darkMode: MutableState<Boolean>, onDarkModeToggle: () -> Unit ) {
    Button ( onClick = {
        darkMode.value = !darkMode.value
        onDarkModeToggle()
    } ) {
        Icon ( imageVector = Icons.Default.Star, contentDescription = "DarkMode" )
        //Spacer ( modifier = Modifier.width ( 16.dp ) )
        Text ( text = "Dark Mode", fontSize = 15.sp )
    }
}