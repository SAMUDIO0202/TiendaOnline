package com.example.tiendaonline.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaonline.data.dao.ClienteDao
import com.example.tiendaonline.data.model.Cliente
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClienteViewModel(private val clienteDao: ClienteDao) : ViewModel() {

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _clienteActual = MutableStateFlow<Cliente?>(null)
    val clienteActual: StateFlow<Cliente?> = _clienteActual

    fun registrarCliente(nombre: String, correo: String, contraseña: String) {
        viewModelScope.launch {
            try {
                val nuevo = Cliente(nombre = nombre, correo = correo, contraseña = contraseña)
                clienteDao.registrarCliente(nuevo)
                _mensaje.value = "Registro exitoso"
            } catch (e: Exception) {
                _mensaje.value = "Error al registrar: ${e.message}"
            }
        }
    }

    fun login(correo: String, contraseña: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val cliente = clienteDao.login(correo, contraseña)
            if (cliente != null) {
                _clienteActual.value = cliente
                _mensaje.value = "Bienvenido ${cliente.nombre} 🐾"
                onSuccess()
            } else {
                _mensaje.value = "Credenciales incorrectas"
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun cerrarSesion() {
        _clienteActual.value = null
        _mensaje.value = "Sesión cerrada"
    }
}