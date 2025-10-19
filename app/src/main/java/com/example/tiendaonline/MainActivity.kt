package com.example.tiendaonline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiendaOnlineApp()
        }
    }
}

@Composable
fun TiendaOnlineApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { PantallaSplash(navController) }
        composable("bienvenida") { PantallaBienvenida(navController) }
        composable("login") { PantallaLogin(navController) }
        composable("registro") { PantallaRegistro(navController) }
        composable("perfil") { PantallaPerfil(navController) }
    }
}

@Composable
fun PantallaSplash(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.petzone_logo),
                contentDescription = "Logo PetZone",
                modifier = Modifier
                    .size(240.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "PetZone",
                color = Color(0xFF6A11CB),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))
            Text(
                text = "Cuidamos lo que amas 🐾",
                color = Color(0xFF6A11CB).copy(alpha = 0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("bienvenida") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

@Composable
fun EncabezadoConLogo(titulo: String, subtitulo: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.petzone_logo),
            contentDescription = "Logo PetZone",
            modifier = Modifier.size(240.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = titulo,
            color = Color(0xFF1C1C1E),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitulo,
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PantallaBienvenida(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            EncabezadoConLogo(
                titulo = "¡Bienvenido a PetZone!",
                subtitulo = "Crea tu cuenta o inicia sesión para continuar."
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Correo Electrónico") },
                placeholder = { Text("Ingresa tu correo...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Continuar", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text("¿No tienes una cuenta? ", color = MaterialTheme.colorScheme.onBackground.copy(0.7f))
                Text(
                    text = "Regístrate",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("registro") }
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Divider(thickness = 1.dp, color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(10.dp))
            Text("o", color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))
            Divider(thickness = 1.dp, color = Color(0xFFE0E0E0))

            Spacer(modifier = Modifier.height(24.dp))

            BotonSocialSimple(
                texto = "Continuar con Google",
                colorTexto = MaterialTheme.colorScheme.primary,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            BotonSocialSimple(
                texto = "Continuar con Facebook",
                colorTexto = MaterialTheme.colorScheme.primary,
                onClick = {}
            )
        }
    }
}

@Composable
fun BotonSocialSimple(texto: String, colorTexto: Color, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Text(
            texto,
            color = colorTexto,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )
    }
}

@Composable
fun PantallaLogin(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            EncabezadoConLogo("Iniciar Sesión", "Ingresa tus credenciales para continuar.")
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Correo Electrónico") },
                placeholder = { Text("ejemplo@email.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("perfil") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
            ) {
                Text("Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("¿No tienes cuenta? ", color = Color.Gray)
                Text(
                    text = "Regístrate",
                    color = Color(0xFF6A11CB),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("registro") }
                )
            }
        }
    }
}

@Composable
fun PantallaRegistro(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            EncabezadoConLogo("Crea tu Cuenta", "Regístrate para disfrutar de PetZone.")
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Nombre Completo") },
                placeholder = { Text("Tu nombre y apellido") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Correo Electrónico") },
                placeholder = { Text("ejemplo@email.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
            ) {
                Text("Registrarme", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("¿Ya tienes cuenta? ", color = Color.Gray)
                Text(
                    text = "Inicia Sesión",
                    color = Color(0xFF6A11CB),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }
    }
}

@Composable
fun PantallaPerfil(navController: NavHostController) {
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
            EncabezadoConLogo("Mi Perfil", "Bienvenido de nuevo a PetZone 🐾")
            Spacer(Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Nombre: Juan Samudio", fontSize = 18.sp, color = Color(0xFF1C1C1E))
                    Text("Correo: juan.samudio@email.com", fontSize = 18.sp, color = Color(0xFF1C1C1E))
                    Text("Miembro desde: Octubre 2024", fontSize = 16.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(50.dp))

            Button(
                onClick = {
                    navController.navigate("bienvenida") {
                        popUpTo("perfil") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
            ) {
                Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}