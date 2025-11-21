package com.example.tiendaonline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tiendaonline.viewmodel.ProductoViewModel
import com.example.tiendaonline.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(
    navController: NavHostController,
    productoViewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val productos by productoViewModel.productos.collectAsState()

    var menuAbierto by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PetZone", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6A11CB))
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Mi Perfil") },
                    onClick = {
                        menuAbierto = false
                        navController.navigate("perfil")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Vender") },
                    onClick = {
                        menuAbierto = false
                        navController.navigate("productos")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cerrar Sesión") },
                    onClick = {
                        menuAbierto = false
                        navController.navigate("bienvenida") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF6F6F6))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Productos Disponibles",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A11CB)
                )

                Spacer(Modifier.height(16.dp))

                if (productos.isEmpty()) {
                    Text("No hay productos aún.", color = Color.Gray)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(productos) { producto ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(4.dp)
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
                                                contentDescription = "Imagen del producto",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(150.dp)
                                            )
                                        }
                                        Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Text(producto.descripcion, fontSize = 14.sp, color = Color.Gray)
                                        Text("$${producto.precio}", fontSize = 16.sp, color = Color(0xFF6A11CB))

                                        Button(
                                            onClick = { carritoViewModel.agregarAlCarrito(producto) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
                                        ) {
                                            Text("Agregar al carrito", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}