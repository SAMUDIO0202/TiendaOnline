package com.example.tiendaonline.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tiendaonline.data.dao.ClienteDao
import com.example.tiendaonline.data.dao.ProductoDao
import com.example.tiendaonline.data.model.Cliente
import com.example.tiendaonline.data.model.Producto

@Database(entities = [Cliente::class, Producto::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clienteDao(): ClienteDao
    abstract fun productoDao(): ProductoDao
}