package com.example.tiendaonline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tiendaonline.viewmodel.ClienteViewModel

@Composable
fun PantallaPerfil(
    navController: NavHostController,
    clienteViewModel: ClienteViewModel
) {
    val cliente = clienteViewModel.clienteActual.collectAsState().value

    var editando by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf(cliente?.nombre ?: "") }
    var correo by remember { mutableStateOf(cliente?.correo ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            EncabezadoConLogo("Mi Perfil", "Gestiona tu cuenta 🐾")

            Spacer(Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!editando) {
                        Text("Nombre: ${cliente?.nombre ?: ""}", fontSize = 18.sp)
                        Text("Correo: ${cliente?.correo ?: ""}", fontSize = 18.sp)

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { editando = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
                        ) {
                            Text("Editar Datos", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { editando = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text("Cancelar", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    clienteViewModel.actualizarCliente(nombre, correo)
                                    editando = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
                            ) {
                                Text("Guardar", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(50.dp))

            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
            ) {
                Text("Ver productos", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    clienteViewModel.cerrarSesion()
                    navController.navigate("bienvenida") {
                        popUpTo("perfil") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}