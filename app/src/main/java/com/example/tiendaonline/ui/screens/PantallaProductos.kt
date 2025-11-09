package com.example.tiendaonline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.tiendaonline.viewmodel.ProductoViewModel
import com.example.tiendaonline.viewmodel.ClienteViewModel
import com.example.tiendaonline.data.model.Producto
import com.example.tiendaonline.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProductos(navController: NavHostController, productoViewModel: ProductoViewModel, clienteViewModel: ClienteViewModel, carritoViewModel: CarritoViewModel) {
    val productos by productoViewModel.productos.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    var productoEditando by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoEdicion by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Productos PetZone", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Ver carrito", tint = Color.White)
                    }
                    IconButton(onClick = {
                        clienteViewModel.cerrarSesion()
                        navController.navigate("bienvenida") {
                            popUpTo("productos") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6A11CB))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nombre.isNotEmpty() && precio.isNotEmpty()) {
                        val nuevo = Producto(
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio.toDoubleOrNull() ?: 0.0
                        )
                        scope.launch {
                            productoViewModel.insertarProducto(nuevo)
                            snackbarHostState.showSnackbar("Producto agregado correctamente ✅")
                        }
                        nombre = ""
                        descripcion = ""
                        precio = ""
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Por favor completa todos los campos ⚠️") }
                    }
                },
                containerColor = Color(0xFF6A11CB)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Agregar Producto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1E)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(25.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(Modifier.height(10.dp))

            Text(
                text = "Lista de Productos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A11CB)
            )

            Spacer(Modifier.height(10.dp))

            if (productos.isEmpty()) {
                Text("No hay productos aún.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
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
                                Row {
                                    IconButton(onClick = {
                                        productoEditando = producto
                                        mostrarDialogoEdicion = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF6A11CB))
                                    }
                                    IconButton(onClick = {
                                        productoViewModel.eliminarProducto(producto)
                                        scope.launch { snackbarHostState.showSnackbar("Producto eliminado 🗑️") }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoEdicion && productoEditando != null) {
        Dialog(onDismissRequest = { mostrarDialogoEdicion = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Editar Producto", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.height(16.dp))

                    var nuevoNombre by remember { mutableStateOf(productoEditando!!.nombre) }
                    var nuevaDescripcion by remember { mutableStateOf(productoEditando!!.descripcion) }
                    var nuevoPrecio by remember { mutableStateOf(productoEditando!!.precio.toString()) }

                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nuevaDescripcion,
                        onValueChange = { nuevaDescripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nuevoPrecio,
                        onValueChange = { nuevoPrecio = it },
                        label = { Text("Precio") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                mostrarDialogoEdicion = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                        Button(
                            onClick = {
                                val actualizado = productoEditando!!.copy(
                                    nombre = nuevoNombre,
                                    descripcion = nuevaDescripcion,
                                    precio = nuevoPrecio.toDoubleOrNull() ?: 0.0
                                )
                                productoViewModel.actualizarProducto(actualizado)
                                mostrarDialogoEdicion = false
                                scope.launch { snackbarHostState.showSnackbar("Producto actualizado 🔁") }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
                        ) {
                            Text("Guardar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}