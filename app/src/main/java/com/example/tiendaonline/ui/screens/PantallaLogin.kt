package com.example.tiendaonline.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tiendaonline.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaLogin(navController: NavHostController, clienteViewModel: ClienteViewModel, correoInicial: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var correo by remember { mutableStateOf(correoInicial) }
    var contrasena by remember { mutableStateOf("") }
    val mensaje by clienteViewModel.mensaje.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            EncabezadoConLogo("Iniciar Sesión", "Ingresa tus credenciales para continuar.")
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                placeholder = { Text("ejemplo@email.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        clienteViewModel.login(correo, contrasena) {
                            navController.navigate("perfil")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
            ) {
                Text("Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("¿No tienes cuenta? ", color = Color.Gray)
                Text(
                    text = "Regístrate",
                    color = Color(0xFF6A11CB),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("registro") }
                )
            }

            if (mensaje != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = mensaje ?: "",
                    color = if (mensaje?.contains("Bienvenido") == true) Color(0xFF4CAF50) else Color.Red,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                if (mensaje?.contains("Bienvenido") == true) {
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    clienteViewModel.limpiarMensaje()
                }
            }
        }
    }
}