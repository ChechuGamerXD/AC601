package com.jesusaledo.ac601

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                AppSharedPreferences()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppSharedPreferences() {
    val context = LocalContext.current
    var currentEmail by remember { mutableStateOf(loadEmail(context)) }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Correo Electrónico") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            exitProcess(0)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de texto
                EmailInputField(currentEmail) { email ->
                    currentEmail = email
                }

                // Botón guardar
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = {
                        saveEmail(context, currentEmail)
                        Toast.makeText(context, "Email guardado", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text("Guardar Email")
                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailInputField(email: String, onEmailChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = email,
        onValueChange = {
            onEmailChanged(it)
        },
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

// Funciones auxiliares para guardar y cargar el correo electrónico usando SharedPreferences

fun saveEmail(context: Context, email: String) {
    val prefs = context.getSharedPreferences("EmailPreferencia", Context.MODE_PRIVATE)
    with(prefs.edit()) {
        putString("email", email)
        apply()
    }
}

fun loadEmail(context: Context): String {
    val prefs = context.getSharedPreferences("EmailPreferencia", Context.MODE_PRIVATE)
    return prefs.getString("email", "") ?: ""
}