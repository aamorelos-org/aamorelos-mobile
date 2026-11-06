package org.aamorelos.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.aamorelos.mobile.data.cache.ReflexionImage
import org.aamorelos.mobile.domain.model.ReflexionModel

/**
 * Tarjeta moderna de reflexión (Material Design 3):
 * - Esquinas pronunciadas (20.dp), elevación 2.dp y borde fino outlineVariant
 * - Categoría en mayúsculas (labelMedium, bold, primary, letterSpacing)
 * - Título jerárquico (titleLarge) con contraste
 * - Acción "LEER REFLEXIÓN →" con color primario
 */
@Composable
fun ReflexionCard(
    reflexion: ReflexionModel,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
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
            // Categoría superior en mayúsculas
            Text(
                text = reflexion.subtitulo.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Título principal de gran jerarquía
            Text(
                text = reflexion.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Imagen (propiedad exacta "imagen" de la API) con caché en filesDir/reflexiones/<slogan>.webp
            if (!reflexion.imagen.isNullOrBlank()) {
                AsyncImage(
                    model = ReflexionImage(url = reflexion.imagen, slogan = reflexion.slogan),
                    contentDescription = reflexion.alt ?: reflexion.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = reflexion.alt?.takeIf { it.isNotBlank() } ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Acción inferior alineada a la izquierda: navega al detalle
            TextButton(
                onClick = onClick,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 0.dp)
            ) {
                Text(
                    text = "LEER REFLEXIÓN →",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
