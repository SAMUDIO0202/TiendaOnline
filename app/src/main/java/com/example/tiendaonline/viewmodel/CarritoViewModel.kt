package com.example.tiendaonline.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiendaonline.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarritoViewModel : ViewModel() {
    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito: StateFlow<List<Producto>> = _carrito

    fun agregarAlCarrito(producto: Producto) {
        _carrito.value = _carrito.value + producto
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito.value = _carrito.value.filter { it.id != producto.id }
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    fun total(): Double = _carrito.value.sumOf { it.precio }
}