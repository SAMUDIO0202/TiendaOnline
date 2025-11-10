package com.example.tiendaonline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tiendaonline.viewmodel.ClienteViewModel


@Composable
fun PantallaRegistro(navController: NavHostController, clienteViewModel: ClienteViewModel) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }

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
            EncabezadoConLogo("Crea tu Cuenta", "Regístrate para disfrutar de PetZone.")
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo Electrónico") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    clienteViewModel.registrarCliente(nombre, correo, contraseña)
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
            ) {
                Text("Registrarme", color = Color.White, fontWeight = FontWeight.Bold)
            }

            mensaje?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = if (it.contains("exitoso")) Color(0xFF388E3C) else Color.Red)
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Text("¿Ya tienes cuenta? ", color = Color.Gray)
                Text(
                    text = "Inicia Sesión",
                    color = Color(0xFF6A11CB),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("login/$correo") }
                )
            }
        }
    }
}