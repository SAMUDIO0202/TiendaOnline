package com.example.tiendaonline.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tiendaonline.data.model.Cliente

@Dao
interface ClienteDao {

    @Insert
    suspend fun registrarCliente(cliente: Cliente)

    @Update
    suspend fun actualizarCliente(cliente: Cliente)

    @Query("SELECT * FROM clientes WHERE correo = :correo AND contraseña = :contraseña LIMIT 1")
    suspend fun login(correo: String, contraseña: String): Cliente?
}