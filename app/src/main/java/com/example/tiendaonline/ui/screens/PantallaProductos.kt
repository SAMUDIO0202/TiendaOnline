package com.example.tiendaonline.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.tiendaonline.data.model.Producto
import com.example.tiendaonline.viewmodel.CarritoViewModel
import com.example.tiendaonline.viewmodel.ClienteViewModel
import com.example.tiendaonline.viewmodel.ProductoViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProductos(
    navController: NavHostController,
    productoViewModel: ProductoViewModel,
    clienteViewModel: ClienteViewModel,
    carritoViewModel: CarritoViewModel
) {
    val productos by productoViewModel.productos.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var locationText by remember { mutableStateOf("Obteniendo ubicación...") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permisoLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val results = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    locationText = results?.firstOrNull()?.locality ?: "Ubicación desconocida"
                } else {
                    locationText = "No se pudo obtener ubicación"
                }
            }
        } else {
            locationText = "Permiso de ubicación denegado"
        }
    }

    LaunchedEffect(Unit) {
        val permisoFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permisoFine == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val results = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    locationText = results?.firstOrNull()?.locality ?: "Ubicación desconocida"
                } else {
                    locationText = "No se pudo obtener ubicación"
                }
            }
        } else {
            permisoLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok -> }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = crearUriImagen(context)
            imagenUri = uri
            camaraLauncher.launch(uri)
        } else {
            scope.launch { snackbarHostState.showSnackbar("Permiso de cámara denegado") }
        }
    }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var productoEditando by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoEdicion by remember { mutableStateOf(false) }

    var menuAbierto by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Productos PetZone", fontWeight = FontWeight.Bold, color = Color.White) },

                navigationIcon = {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },

                actions = {
                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Comprar") },
                            onClick = {
                                menuAbierto = false
                                navController.navigate("home")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Perfil") },
                            onClick = {
                                menuAbierto = false
                                navController.navigate("perfil")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                menuAbierto = false
                                clienteViewModel.cerrarSesion()
                                navController.navigate("bienvenida") {
                                    popUpTo("productos") { inclusive = true }
                                }
                            }
                        )
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
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            imagen = imagenUri?.toString()
                        )
                        scope.launch {
                            productoViewModel.insertarProducto(nuevo)
                            snackbarHostState.showSnackbar("Producto agregado correctamente ✅")
                        }
                        nombre = ""
                        descripcion = ""
                        precio = ""
                        imagenUri = null
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
            Text("Agregar Producto", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(6.dp))
            Text("Tu ubicación: $locationText", color = Color.Gray, fontSize = 14.sp)

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

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val permisoCamara = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permisoCamara == PackageManager.PERMISSION_GRANTED) {
                        val uri = crearUriImagen(context)
                        imagenUri = uri
                        camaraLauncher.launch(uri)
                    } else {
                        permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar Foto del Producto", color = Color.White)
            }

            imagenUri?.let {
                Spacer(Modifier.height(10.dp))
                AsyncImage(
                    model = it,
                    contentDescription = "Foto del producto",
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }

            Spacer(Modifier.height(25.dp))
            Divider()
            Spacer(Modifier.height(10.dp))

            Text("Lista de Productos", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(10.dp))

            if (productos.isEmpty()) {
                Text("No hay productos aún.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
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
                                    Text(producto.descripcion, fontSize = 14.sp, color = Color.Gray)
                                    Text("$${producto.precio}", fontSize = 16.sp, color = Color(0xFF6A11CB))
                                    Row {
                                        IconButton(onClick = {
                                            productoEditando = producto
                                            mostrarDialogoEdicion = true
                                        }) {
                                            Icon(Icons.Default.Edit, null, tint = Color(0xFF6A11CB))
                                        }
                                        IconButton(onClick = {
                                            productoViewModel.eliminarProducto(producto)
                                            scope.launch { snackbarHostState.showSnackbar("Producto eliminado 🗑️") }
                                        }) {
                                            Icon(Icons.Default.Delete, null, tint = Color.Red)
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

    if (mostrarDialogoEdicion && productoEditando != null) {
        Dialog(onDismissRequest = { mostrarDialogoEdicion = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
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
                            onClick = { mostrarDialogoEdicion = false },
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

fun crearUriImagen(context: Context): Uri {
    val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile("petzone_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        imageFile
    )
}