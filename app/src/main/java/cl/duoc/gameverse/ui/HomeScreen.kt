package cl.duoc.gameverse.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.gameverse.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Imagen del logo (recurso en drawable)
            Image(
                painter = painterResource(id = R.drawable.icono2),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp),
                contentScale = ContentScale.Fit
            )
            
            // Título principal
            Text(
                text = "Bienvenido a GameVerse",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary // usar MaterialTheme (Parte 2.5)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo / descripción
            Text(
                text = "¿Quieres ingresar?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPrimaryAction,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Iniciar Sesión") }

                OutlinedButton(
                    onClick = onSecondaryAction,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Registrarse") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onPrimaryAction = { /*TODO*/ }) {

    }
}