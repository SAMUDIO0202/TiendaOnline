package com.example.tiendaonline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.tiendaonline.data.database.AppDatabase
import com.example.tiendaonline.ui.screens.*
import com.example.tiendaonline.viewmodel.CarritoViewModel
import com.example.tiendaonline.viewmodel.ProductoViewModel
import com.example.tiendaonline.viewmodel.ClienteViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "petzone_db"
        ).build()

        val productoViewModel: ProductoViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return ProductoViewModel(db.productoDao()) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
        val clienteViewModel: ClienteViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return ClienteViewModel(db.clienteDao()) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
        val carritoViewModel: CarritoViewModel by viewModels()


        setContent {
            TiendaOnlineApp(
                productoViewModel = productoViewModel,
                clienteViewModel = clienteViewModel,
                carritoViewModel = carritoViewModel
            )
        }
    }

    @Composable
    fun TiendaOnlineApp(
        productoViewModel: ProductoViewModel,
        clienteViewModel: ClienteViewModel,
        carritoViewModel: CarritoViewModel
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") { PantallaSplash(navController) }
            composable("bienvenida") { PantallaBienvenida(navController) }
            composable("login/{correo}") { backStackEntry ->
                val correo = backStackEntry.arguments?.getString("correo") ?: ""
                PantallaLogin(navController, clienteViewModel, correo)
            }
            composable("registro") { PantallaRegistro(navController, clienteViewModel) }
            composable("perfil") { PantallaPerfil(navController, clienteViewModel) }
            composable("productos") {
                PantallaProductos(
                    navController,
                    productoViewModel,
                    clienteViewModel,
                    carritoViewModel
                )
            }
            composable("carrito") { PantallaCarrito(navController, carritoViewModel) }

        }
    }
}