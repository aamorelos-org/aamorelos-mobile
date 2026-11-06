package org.aamorelos.mobile.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Contenido de título para el TopAppBar en Inicio: lema + título + subtítulo.
 * Los contactos (correo/teléfono) van como acciones en el TopAppBar.
 */
@Composable
fun HomeTopAppBarTitle(
    lemaInstitucional: String = "CENTRAL MEXICANA DE SERVICIOS GENERALES DE A.A., A.C.",
    titulo: String = "Alcohólicos Anónimos",
    subtituloOrganizativo: String = "6to Distrito • Área Morelos • 2026",
    lemaMotivacional: String = "El servicio con amor para el que vendrá",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = lemaInstitucional.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtituloOrganizativo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (lemaMotivacional.isNotBlank()) {
            Text(
                text = lemaMotivacional,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun openEmail(context: android.content.Context, correo: String = "area28@aamexico.org.mx") {
    try {
        context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$correo")))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No se encontró una aplicación de correo", Toast.LENGTH_SHORT).show()
    }
}

fun openDial(context: android.content.Context, telefono: String = "777 992-40-62") {
    try {
        val telUri = Uri.parse("tel:${telefono.replace(" ", "").replace("-", "")}")
        context.startActivity(Intent(Intent.ACTION_DIAL, telUri))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No se encontró una aplicación para llamar", Toast.LENGTH_SHORT).show()
    }
}
