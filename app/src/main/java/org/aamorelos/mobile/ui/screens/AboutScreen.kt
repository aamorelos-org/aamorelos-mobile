package org.aamorelos.mobile.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Pantalla de Información institucional ("¿Qué es AA?").
 * Contenido oficial en tarjetas con scroll vertical (LazyColumn).
 *
 * - Contactos interactivos con Intents nativos (llamada / correo).
 * - Diseño adaptativo con MaterialTheme.colorScheme (claro/oscuro).
 */
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun dial(telefono: String) {
        try {
            val telUri = Uri.parse("tel:${telefono.replace(" ", "").replace("-", "")}")
            context.startActivity(Intent(Intent.ACTION_DIAL, telUri))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No se encontró una aplicación para llamar", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMail(correo: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$correo")))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No se encontró una aplicación de correo", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item(key = "que_es_aa") {
            AboutCard(title = "¿Qué es AA?") {
                Text(
                    text = "Alcohólicos Anónimos (AA) es una comunidad de experiencia, fortaleza y esperanza para recuperarse del alcoholismo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Bullet(text = "El único requisito para ser miembro de AA es el deseo de dejar de beber.")
                Bullet(text = "No hay cuotas ni honorarios.")
                Bullet(text = "No está afiliada a sectas, partidos políticos u organizaciones de cualquier tipo.")
            }
        }

        item(key = "historia") {
            AboutCard(title = "Historia de AA") {
                Text(
                    text = "AA fue fundada el 10 de junio de 1935 en Akron, Ohio, por el Dr. Robert H. Smith (Bob S.) y Bill W.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Entre ellos nació el concepto del “puente de comprensión”, y en 1939 se publicó el libro de AA, base del programa de recuperación.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(key = "alcoholismo") {
            AboutCard(title = "¿Qué es el alcoholismo?") {
                Text(
                    text = "Es una enfermedad progresiva que se puede contener mediante un programa de recuperación.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "No es una falta moral ni falta de fuerza de voluntad.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(key = "como_funciona") {
            AboutCard(title = "¿Cómo funciona Alcohólicos Anónimos?") {
                Bullet(text = "Los Doce Pasos")
                Bullet(text = "Las Doce Tradiciones")
                Bullet(text = "Terapia grupal de ayuda mutua, voluntaria y gratuita.")
                Bullet(text = "Los grupos registrados en la OSG (Oficina de Servicios Generales).")
            }
        }

        item(key = "como_ingreso") {
            AboutCard(title = "¿Cómo ingreso a un grupo?") {
                Text(
                    text = "Acudí al grupo más cercano o llamá al teléfono de asistencia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ContactRow(
                    icon = Icons.Default.Phone,
                    text = "800 216 92 31",
                    contentDescription = "Llamar 800 216 92 31",
                    onClick = { dial("800 216 92 31") }
                )
                ContactRow(
                    icon = Icons.Default.Phone,
                    text = "777 992-40-62",
                    contentDescription = "Llamar 777 992-40-62",
                    onClick = { dial("777 992-40-62") }
                )
                ContactRow(
                    icon = Icons.Default.Email,
                    text = "area28@aamexico.org.mx",
                    contentDescription = "Enviar correo",
                    onClick = { sendMail("area28@aamexico.org.mx") }
                )
            }
        }

        item(key = "no_hace") {
            AboutCard(title = "¿Qué es lo que AA no hace?") {
                Bullet(text = "No da diagnósticos.")
                Bullet(text = "No acepta dinero externo.")
                Bullet(text = "No ofrece servicios médicos ni de vivienda.")
                Bullet(text = "No controla ni interna a sus miembros.")
                Bullet(text = "No da consejos profesionales (psicológicos, médicos, laborales, etc.).")
                Bullet(text = "No apoya u obstaculiza causas ajenas.")
            }
        }

        item(key = "central") {
            AboutCard(title = "¿Qué es la Central Mexicana de Servicios Generales de AA?") {
                Text(
                    text = "Es una Asociación Civil no lucrativa y la única representante legal autorizada para distribuir literatura en México.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ContactRow(
                    icon = Icons.Default.Phone,
                    text = "800 216 92 31",
                    contentDescription = "Llamar 800 216 92 31",
                    onClick = { dial("800 216 92 31") }
                )
                ContactRow(
                    icon = Icons.Default.Email,
                    text = "area28@aamexico.org.mx",
                    contentDescription = "Enviar correo",
                    onClick = { sendMail("area28@aamexico.org.mx") }
                )
            }
        }
    }
}

@Composable
private fun AboutCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            content()
        }
    }
}

@Composable
private fun Bullet(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ContactRow(
    icon: ImageVector,
    text: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
