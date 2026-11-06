package org.aamorelos.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.aamorelos.mobile.data.cache.DirectorioImage
import org.aamorelos.mobile.domain.model.DirectorioModel

/**
 * Tarjeta de galería del directorio (Material Design 3):
 * - Imagen superior con caché en filesDir/directorio/<imageKey>.webp
 * - Etiqueta con número de página en mayúsculas
 * - Periodo "Mes • Año"
 */
@Composable
fun DirectorioCard(
    directorio: DirectorioModel,
    modifier: Modifier = Modifier
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
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "PÁGINA ${directorio.pagina}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!directorio.imagen.isNullOrBlank()) {
                AsyncImage(
                    model = DirectorioImage(url = directorio.imagen, slogan = directorio.imageKey),
                    contentDescription = "Página ${directorio.pagina} - ${directorio.mes} ${directorio.anio}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            if (directorio.mes.isNotBlank() || directorio.anio.isNotBlank()) {
                Text(
                    text = "${directorio.mes} • ${directorio.anio}".trim().trim('•').trim(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
