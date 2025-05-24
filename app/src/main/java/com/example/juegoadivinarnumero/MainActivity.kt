package com.example.juegoadivinarnumero

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import com.example.juegoadivinarnumero.ui.theme.JuegoAdivinarNumeroTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuegoAdivinarNumeroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    JuegoAdivinarNumero()
                }
            }
        }
    }
}

@Composable
fun JuegoAdivinarNumero() {
    var juegoActivo by remember { mutableStateOf(false) }
    var numeroSecreto by remember { mutableStateOf(0) }
    var intentosRestantes by remember { mutableStateOf(3) }
    var entradaUsuario by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var tiempoRestante by remember { mutableStateOf(60) }

    val context = LocalContext.current

    LaunchedEffect(juegoActivo) {
        if (juegoActivo) {
            tiempoRestante = 60
            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--
            }
            if (tiempoRestante == 0) {
                mensaje = "¡Tiempo agotado! El número era $numeroSecreto."
                juegoActivo = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Juego de Adivinar el Número", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        if (!juegoActivo) {
            Button(onClick = {
                numeroSecreto = Random.nextInt(0, 101)
                intentosRestantes = 3
                mensaje = ""
                entradaUsuario = ""
                juegoActivo = true
            }) {
                Text("Iniciar Juego")
            }
        } else {
            Button(onClick = {
                numeroSecreto = Random.nextInt(0, 101)
                intentosRestantes = 3
                mensaje = ""
                entradaUsuario = ""
                tiempoRestante = 60
                juegoActivo = true
            }) {
                Text("Reiniciar Juego")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Tienes $intentosRestantes intento(s)")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tiempoRestante.toString(),
                onValueChange = {},
                label = { Text("Tiempo restante (segundos)") },
                enabled = false,
                modifier = Modifier.width(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = entradaUsuario,
                onValueChange = { entradaUsuario = it },
                label = { Text("Ingresa un número del 0 al 100") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val intento = entradaUsuario.toIntOrNull()
                if (intento == null || intento !in 0..100) {
                    Toast.makeText(context, "Por favor ingresa un número válido entre 0 y 100.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (intento == numeroSecreto) {
                    mensaje = "¡Felicidades! Adivinaste el número $numeroSecreto."
                    juegoActivo = false
                } else {
                    intentosRestantes--
                    mensaje = if (intento < numeroSecreto) {
                        "El número secreto es mayor."
                    } else {
                        "El número secreto es menor."
                    }

                    if (intentosRestantes == 0) {
                        mensaje = "Lo siento, perdiste. El número era $numeroSecreto."
                        juegoActivo = false
                    }
                }
                entradaUsuario = ""
            }) {
                Text("Probar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(mensaje)
        }
    }
}