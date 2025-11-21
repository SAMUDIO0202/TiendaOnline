package com.example.tiendaonline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tiendaonline.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(navController: NavHostController, carritoViewModel: CarritoViewModel) {
    val carrito = carritoViewModel.carrito.collectAsState().value
    val total = carritoViewModel.total()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { carritoViewModel.vaciarCarrito() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Vaciar carrito", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6A11CB))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(padding)
                .padding(20.dp)
        ) {
            if (carrito.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío 🐾", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(carrito) { producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    producto.imagen?.let {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = "Imagen",
                                            modifier = Modifier.fillMaxWidth().height(150.dp)
                                        )
                                    }
                                    Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("$${producto.precio}", fontSize = 16.sp, color = Color(0xFF6A11CB))
                                }
                                IconButton(onClick = { carritoViewModel.eliminarDelCarrito(producto) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Divider()
                Spacer(Modifier.height(10.dp))
                Text("Total: $${String.format("%.2f", total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { carritoViewModel.vaciarCarrito() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
                ) {
                    Text("Finalizar compra", color = Color.White)
                }
            }
        }
    }
}