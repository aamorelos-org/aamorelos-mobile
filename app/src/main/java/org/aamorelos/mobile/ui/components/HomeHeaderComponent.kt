package org.aamorelos.mobile.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Cabecera institucional principal para la pantalla de inicio.
 *
 * - Título en negrita y gran tamaño
 * - Subtítulo organizativo + lema motivacional en cursiva (color primario)
 * - Datos de contacto interactivos (ubicación, correo y teléfono)
 *
 * Diseño adaptativo con [MaterialTheme.colorScheme] (claro/oscuro).
 */
@Composable
fun HomeHeaderComponent(
    titulo: String = "Alcohólicos Anónimos",
    subtituloOrganizativo: String = "6to Distrito • Área Morelos • 2026",
    lemaMotivacional: String = "«El servicio con amor para el que vendrá»",
    ubicacion: String = "Área Morelos • 6to Distrito",
    correo: String = "area28@aamexico.org.mx",
    telefono: String = "777 992-40-62",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Título principal en grande y negrita
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Subtítulo organizativo + lema motivacional en cursiva (primario)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = subtituloOrganizativo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (lemaMotivacional.isNotBlank()) {
                    Text(
                        text = lemaMotivacional,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 4. Divisor visual
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 5. Datos de contacto rápido interactivos
            ContactRow(
                icon = Icons.Default.LocationOn,
                text = ubicacion,
                contentDescription = "Ubicación",
                onClick = null
            )

            ContactRow(
                icon = Icons.Default.Email,
                text = correo,
                contentDescription = "Enviar correo",
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$correo"))
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "No se encontró una aplicación de correo", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            ContactRow(
                icon = Icons.Default.Phone,
                text = telefono,
                contentDescription = "Llamar por teléfono",
                onClick = {
                    try {
                        val telUri = Uri.parse("tel:${telefono.replace(" ", "").replace("-", "")}")
                        context.startActivity(Intent(Intent.ACTION_DIAL, telUri))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "No se encontró una aplicación para llamar", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
private fun ContactRow(
    icon: ImageVector,
    text: String,
    contentDescription: String,
    onClick: (() -> Unit)?
) {
    val rowModifier = if (onClick != null) {
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    } else {
        Modifier.fillMaxWidth()
    }
    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
            tint = if (onClick != null) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (onClick != null) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            fontWeight = if (onClick != null) FontWeight.Medium else FontWeight.Normal
        )
    }
}
