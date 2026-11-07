package org.aamorelos.mobile.notifications

import org.aamorelos.mobile.notifications.PushNotificationDestination.EventoDetail
import org.aamorelos.mobile.notifications.PushNotificationDestination.ReflexionDetail
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PushNotificationParserTest {

    @Test
    fun eventoInformativo_navegaAlDetalleConEventoId() {
        val destino = PushNotificationParser.parse(
            mapOf("tipo" to "informativo", "slogan" to "paseo", "evento_id" to "1")
        )
        assertEquals(EventoDetail(eventoId = 1), destino)
        assertEquals("evento_detalle/1", destino?.route)
    }

    @Test
    fun eventoInvitacion_navegaAlDetalleConEventoId() {
        val destino = PushNotificationParser.parse(
            mapOf("tipo" to "invitacion", "slogan" to "aniversario", "evento_id" to "42")
        )
        assertEquals(EventoDetail(eventoId = 42), destino)
    }

    @Test
    fun reflexion_navegaAlDetalleConSloganYReflexionId() {
        val destino = PushNotificationParser.parse(
            mapOf("tipo" to "reflexion", "slogan" to "solo-por-hoy", "reflexion_id" to "7")
        )
        assertEquals(ReflexionDetail(slogan = "solo-por-hoy", reflexionId = "7"), destino)
        assertEquals("reflexion_detalle/solo-por-hoy", destino?.route)
    }

    @Test
    fun reflexion_sinSlogan_usaReflexionIdComoIdentificador() {
        val destino = PushNotificationParser.parse(
            mapOf("tipo" to "reflexion", "reflexion_id" to "mi-reflexion")
        )
        assertEquals(ReflexionDetail(slogan = "mi-reflexion", reflexionId = "mi-reflexion"), destino)
    }

    @Test
    fun dataSinTipo_infierePorClavePresente() {
        assertEquals(
            EventoDetail(eventoId = 3),
            PushNotificationParser.parse(mapOf("evento_id" to "3"))
        )
        assertEquals(
            ReflexionDetail(slogan = "abc", reflexionId = "9"),
            PushNotificationParser.parse(mapOf("slogan" to "abc", "reflexion_id" to "9"))
        )
    }

    @Test
    fun dataInvalido_devuelveNull() {
        assertNull(PushNotificationParser.parse(emptyMap()))
        assertNull(PushNotificationParser.parse(mapOf("tipo" to "informativo", "evento_id" to "x")))
        assertNull(PushNotificationParser.parse(mapOf("tipo" to "reflexion")))
    }
}
