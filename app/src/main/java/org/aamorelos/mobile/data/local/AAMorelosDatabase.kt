package org.aamorelos.mobile.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.aamorelos.mobile.domain.model.DistritoDetailModel
import org.aamorelos.mobile.domain.model.DistritoModel
import org.aamorelos.mobile.domain.model.GrupoDetailModel
import org.aamorelos.mobile.domain.model.GrupoModel
import org.aamorelos.mobile.domain.model.ReflexionModel

class AAMorelosDatabase(context: Context) : SQLiteOpenHelper(
    context,
    "aamorelos",
    null,
    8
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS grupos (
                estadoId TEXT NOT NULL,
                id TEXT NOT NULL,
                nombre TEXT NOT NULL,
                distritoId TEXT,
                municipio TEXT NOT NULL,
                vecinos TEXT,
                tipo TEXT NOT NULL,
                genero TEXT NOT NULL,
                lengua TEXT NOT NULL,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (estadoId, id)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS distritos (
                estadoId TEXT NOT NULL,
                id TEXT NOT NULL,
                nombre TEXT NOT NULL,
                municipio TEXT NOT NULL,
                vecinos TEXT,
                area TEXT NOT NULL,
                areaId TEXT NOT NULL,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (estadoId, id)
            )
            """.trimIndent()
        )
        db.execSQL(CREATE_GRUPO_DETALLE_SQL)
        db.execSQL(CREATE_DISTRITO_DETALLE_SQL)
        db.execSQL(CREATE_REFLEXIONES_SQL)
        db.execSQL(CREATE_REFLEXION_DETALLE_SQL)
        db.execSQL(CREATE_EVENTOS_SQL)
        db.execSQL(CREATE_EVENTO_DETALLE_SQL)
        db.execSQL(CREATE_EVENTO_FRASES_SQL)
        db.execSQL(CREATE_EVENTO_ANIVERSARIO_SQL)
        db.execSQL(CREATE_EVENTO_ANIVERSARIO_DIAS_SQL)
        db.execSQL(CREATE_DIRECTORIO_SQL)
        db.execSQL(CREATE_REFLEXION_DIARIA_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(CREATE_GRUPO_DETALLE_SQL)
        }
        if (oldVersion < 3) {
            db.execSQL(CREATE_DISTRITO_DETALLE_SQL)
        }
        if (oldVersion < 4) {
            db.execSQL(CREATE_REFLEXIONES_SQL)
        }
        if (oldVersion < 5) {
            db.execSQL(CREATE_REFLEXION_DETALLE_SQL)
        }
        if (oldVersion < 6) {
            db.execSQL(CREATE_EVENTOS_SQL)
            db.execSQL(CREATE_EVENTO_DETALLE_SQL)
            db.execSQL(CREATE_EVENTO_FRASES_SQL)
            db.execSQL(CREATE_EVENTO_ANIVERSARIO_SQL)
            db.execSQL(CREATE_EVENTO_ANIVERSARIO_DIAS_SQL)
        }
        if (oldVersion < 7) {
            db.execSQL(CREATE_DIRECTORIO_SQL)
        }
        if (oldVersion < 8) {
            db.execSQL(CREATE_REFLEXION_DIARIA_SQL)
        }
    }

    fun getGrupos(estadoId: String, ts: Long): List<GrupoModel> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nombre, distritoId, municipio, vecinos, tipo, genero, lengua, ultimaActualizacion " +
                "FROM grupos WHERE estadoId = ? AND ultimaActualizacion > ?",
            arrayOf(estadoId, ts.toString())
        )
        val result = mutableListOf<GrupoModel>()
        var valid = cursor.moveToFirst()
        while (valid) {
            result.add(
                GrupoModel(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("id")) ?: "",
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) ?: "",
                    distritoId = cursor.getString(cursor.getColumnIndexOrThrow("distritoId")) ?: "",
                    municipio = cursor.getString(cursor.getColumnIndexOrThrow("municipio")) ?: "",
                    vecinos = cursor.getString(cursor.getColumnIndexOrThrow("vecinos"))?.takeIf { it.isNotBlank() },
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")) ?: "",
                    genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")) ?: "",
                    lengua = cursor.getString(cursor.getColumnIndexOrThrow("lengua")) ?: ""
                )
            )
            valid = cursor.moveToNext()
        }
        cursor.close()
        return result
    }

    fun saveGrupos(entities: List<GrupoEntity>) {
        val db = writableDatabase
        db.beginTransaction()
        entities.forEach { entity ->
            val values = ContentValues().apply {
                put("estadoId", entity.estadoId)
                put("id", entity.id)
                put("nombre", entity.nombre)
                put("distritoId", entity.distritoId)
                put("municipio", entity.municipio)
                put("vecinos", entity.vecinos)
                put("tipo", entity.tipo)
                put("genero", entity.genero)
                put("lengua", entity.lengua)
                put("ultimaActualizacion", entity.ultimaActualizacion)
            }
            db.insert("grupos", null, values)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun getDistritos(estadoId: String, ts: Long): List<DistritoModel> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nombre, municipio, vecinos, area, areaId FROM distritos WHERE estadoId = ? AND ultimaActualizacion > ?",
            arrayOf(estadoId, ts.toString())
        )
        val result = mutableListOf<DistritoModel>()
        var valid = cursor.moveToFirst()
        while (valid) {
            result.add(
                DistritoModel(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("id")) ?: "",
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) ?: "",
                    municipio = cursor.getString(cursor.getColumnIndexOrThrow("municipio")) ?: "",
                    vecinos = cursor.getString(cursor.getColumnIndexOrThrow("vecinos"))?.takeIf { it.isNotBlank() },
                    area = cursor.getString(cursor.getColumnIndexOrThrow("area")) ?: "",
                    areaId = cursor.getString(cursor.getColumnIndexOrThrow("areaId")) ?: ""
                )
            )
            valid = cursor.moveToNext()
        }
        cursor.close()
        return result
    }

    fun saveDistritos(entities: List<DistritoEntity>) {
        val db = writableDatabase
        db.beginTransaction()
        entities.forEach { entity ->
            val values = ContentValues().apply {
                put("estadoId", entity.estadoId)
                put("id", entity.id)
                put("nombre", entity.nombre)
                put("municipio", entity.municipio)
                put("vecinos", entity.vecinos)
                put("area", entity.area)
                put("areaId", entity.areaId)
                put("ultimaActualizacion", entity.ultimaActualizacion)
            }
            db.insert("distritos", null, values)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun getGrupoDetail(estadoId: String, grupoId: String, ts: Long): GrupoDetailModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, distritoId, municipioId, tipoId, lenguaId, generoId, nombre, fechaCreacion,
                   direccion, vecinos, codigoPostal, referencias, localidad, cpr,
                   horarioLunes, horarioMartes, horarioMiercoles, horarioJueves,
                   horarioViernes, horarioSabado, horarioDomingo, horarioObservacion,
                   lenguajeDetalle, permiteFumar, esActivo, latitud, longitud, fumarTexto,
                   municipio, tipo, genero, lengua
            FROM grupo_detalle
            WHERE estadoId = ? AND id = ? AND ultimaActualizacion > ?
            """.trimIndent(),
            arrayOf(estadoId, grupoId, ts.toString())
        )
        return try {
            if (cursor.moveToFirst()) {
                GrupoDetailModel(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("id")) ?: "",
                    distritoId = cursor.getString(cursor.getColumnIndexOrThrow("distritoId")) ?: "",
                    municipioId = cursor.getString(cursor.getColumnIndexOrThrow("municipioId")) ?: "",
                    tipoId = cursor.getInt(cursor.getColumnIndexOrThrow("tipoId")),
                    lenguaId = cursor.getInt(cursor.getColumnIndexOrThrow("lenguaId")),
                    generoId = cursor.getInt(cursor.getColumnIndexOrThrow("generoId")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) ?: "",
                    fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion"))?.takeIf { it.isNotBlank() },
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"))?.takeIf { it.isNotBlank() },
                    vecinos = cursor.getString(cursor.getColumnIndexOrThrow("vecinos"))?.takeIf { it.isNotBlank() },
                    codigoPostal = cursor.getString(cursor.getColumnIndexOrThrow("codigoPostal"))?.takeIf { it.isNotBlank() },
                    referencias = cursor.getString(cursor.getColumnIndexOrThrow("referencias"))?.takeIf { it.isNotBlank() },
                    localidad = cursor.getString(cursor.getColumnIndexOrThrow("localidad"))?.takeIf { it.isNotBlank() },
                    cpr = cursor.getString(cursor.getColumnIndexOrThrow("cpr"))?.takeIf { it.isNotBlank() },
                    horarioLunes = cursor.getString(cursor.getColumnIndexOrThrow("horarioLunes"))?.takeIf { it.isNotBlank() },
                    horarioMartes = cursor.getString(cursor.getColumnIndexOrThrow("horarioMartes"))?.takeIf { it.isNotBlank() },
                    horarioMiercoles = cursor.getString(cursor.getColumnIndexOrThrow("horarioMiercoles"))?.takeIf { it.isNotBlank() },
                    horarioJueves = cursor.getString(cursor.getColumnIndexOrThrow("horarioJueves"))?.takeIf { it.isNotBlank() },
                    horarioViernes = cursor.getString(cursor.getColumnIndexOrThrow("horarioViernes"))?.takeIf { it.isNotBlank() },
                    horarioSabado = cursor.getString(cursor.getColumnIndexOrThrow("horarioSabado"))?.takeIf { it.isNotBlank() },
                    horarioDomingo = cursor.getString(cursor.getColumnIndexOrThrow("horarioDomingo"))?.takeIf { it.isNotBlank() },
                    horarioObservacion = cursor.getString(cursor.getColumnIndexOrThrow("horarioObservacion"))?.takeIf { it.isNotBlank() },
                    lenguajeDetalle = cursor.getString(cursor.getColumnIndexOrThrow("lenguajeDetalle"))?.takeIf { it.isNotBlank() },
                    permiteFumar = cursor.getInt(cursor.getColumnIndexOrThrow("permiteFumar")),
                    esActivo = cursor.getInt(cursor.getColumnIndexOrThrow("esActivo")),
                    latitud = cursor.getString(cursor.getColumnIndexOrThrow("latitud"))?.takeIf { it.isNotBlank() },
                    longitud = cursor.getString(cursor.getColumnIndexOrThrow("longitud"))?.takeIf { it.isNotBlank() },
                    fumarTexto = cursor.getString(cursor.getColumnIndexOrThrow("fumarTexto"))?.takeIf { it.isNotBlank() },
                    municipio = cursor.getString(cursor.getColumnIndexOrThrow("municipio")) ?: "",
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")) ?: "",
                    genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")) ?: "",
                    lengua = cursor.getString(cursor.getColumnIndexOrThrow("lengua")) ?: ""
                )
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    fun saveGrupoDetail(entity: GrupoDetailEntity) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("estadoId", entity.estadoId)
            put("id", entity.id)
            put("distritoId", entity.distritoId)
            put("municipioId", entity.municipioId)
            put("tipoId", entity.tipoId)
            put("lenguaId", entity.lenguaId)
            put("generoId", entity.generoId)
            put("nombre", entity.nombre)
            put("fechaCreacion", entity.fechaCreacion)
            put("direccion", entity.direccion)
            put("vecinos", entity.vecinos)
            put("codigoPostal", entity.codigoPostal)
            put("referencias", entity.referencias)
            put("localidad", entity.localidad)
            put("cpr", entity.cpr)
            put("horarioLunes", entity.horarioLunes)
            put("horarioMartes", entity.horarioMartes)
            put("horarioMiercoles", entity.horarioMiercoles)
            put("horarioJueves", entity.horarioJueves)
            put("horarioViernes", entity.horarioViernes)
            put("horarioSabado", entity.horarioSabado)
            put("horarioDomingo", entity.horarioDomingo)
            put("horarioObservacion", entity.horarioObservacion)
            put("lenguajeDetalle", entity.lenguajeDetalle)
            put("permiteFumar", entity.permiteFumar)
            put("esActivo", entity.esActivo)
            put("latitud", entity.latitud)
            put("longitud", entity.longitud)
            put("fumarTexto", entity.fumarTexto)
            put("municipio", entity.municipio)
            put("tipo", entity.tipo)
            put("genero", entity.genero)
            put("lengua", entity.lengua)
            put("ultimaActualizacion", entity.ultimaActualizacion)
        }
        db.insertWithOnConflict("grupo_detalle", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getDistritoDetail(distritoId: String, ts: Long): DistritoDetailModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, areaId, municipioId, nombre, distritoNumero, fechaCreacion,
                   direccion, vecinos, codigoPostal, referencia, telefono,
                   servicioHoras, esActivo, municipio, area
            FROM distrito_detalle
            WHERE id = ? AND ultimaActualizacion > ?
            """.trimIndent(),
            arrayOf(distritoId, ts.toString())
        )
        return try {
            if (cursor.moveToFirst()) {
                val distritoNumeroCol = cursor.getColumnIndex("distritoNumero")
                val esActivoCol = cursor.getColumnIndex("esActivo")
                DistritoDetailModel(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("id")) ?: "",
                    areaId = cursor.getString(cursor.getColumnIndexOrThrow("areaId"))?.takeIf { it.isNotBlank() },
                    municipioId = cursor.getString(cursor.getColumnIndexOrThrow("municipioId"))?.takeIf { it.isNotBlank() },
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) ?: "",
                    distritoNumero = if (!cursor.isNull(distritoNumeroCol)) cursor.getInt(distritoNumeroCol) else null,
                    fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion"))?.takeIf { it.isNotBlank() },
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"))?.takeIf { it.isNotBlank() },
                    vecinos = cursor.getString(cursor.getColumnIndexOrThrow("vecinos"))?.takeIf { it.isNotBlank() },
                    codigoPostal = cursor.getString(cursor.getColumnIndexOrThrow("codigoPostal"))?.takeIf { it.isNotBlank() },
                    referencia = cursor.getString(cursor.getColumnIndexOrThrow("referencia"))?.takeIf { it.isNotBlank() },
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))?.takeIf { it.isNotBlank() },
                    servicioHoras = cursor.getString(cursor.getColumnIndexOrThrow("servicioHoras"))?.takeIf { it.isNotBlank() },
                    esActivo = if (!cursor.isNull(esActivoCol)) cursor.getInt(esActivoCol) else null,
                    municipio = cursor.getString(cursor.getColumnIndexOrThrow("municipio"))?.takeIf { it.isNotBlank() },
                    area = cursor.getString(cursor.getColumnIndexOrThrow("area"))?.takeIf { it.isNotBlank() }
                )
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    fun saveDistritoDetail(entity: DistritoDetailEntity) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", entity.id)
            put("areaId", entity.areaId)
            put("municipioId", entity.municipioId)
            put("nombre", entity.nombre)
            if (entity.distritoNumero != null) put("distritoNumero", entity.distritoNumero) else putNull("distritoNumero")
            put("fechaCreacion", entity.fechaCreacion)
            put("direccion", entity.direccion)
            put("vecinos", entity.vecinos)
            put("codigoPostal", entity.codigoPostal)
            put("referencia", entity.referencia)
            put("telefono", entity.telefono)
            put("servicioHoras", entity.servicioHoras)
            if (entity.esActivo != null) put("esActivo", entity.esActivo) else putNull("esActivo")
            put("municipio", entity.municipio)
            put("area", entity.area)
            put("ultimaActualizacion", entity.ultimaActualizacion)
        }
        db.insertWithOnConflict("distrito_detalle", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getReflexiones(limit: Int, offset: Int): List<ReflexionModel> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, slogan, titulo, subtitulo, imagen, alt, url
            FROM reflexiones
            ORDER BY position ASC
            LIMIT ? OFFSET ?
            """.trimIndent(),
            arrayOf(limit.toString(), offset.toString())
        )
        val result = mutableListOf<ReflexionModel>()
        try {
            var valid = cursor.moveToFirst()
            while (valid) {
                result.add(
                    ReflexionModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        slogan = cursor.getString(cursor.getColumnIndexOrThrow("slogan")) ?: "",
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")) ?: "",
                        subtitulo = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo")) ?: "",
                        imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))?.takeIf { it.isNotBlank() },
                        alt = cursor.getString(cursor.getColumnIndexOrThrow("alt"))?.takeIf { it.isNotBlank() },
                        url = cursor.getString(cursor.getColumnIndexOrThrow("url"))?.takeIf { it.isNotBlank() }
                    )
                )
                valid = cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }
        return result
    }

    fun getReflexionesCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM reflexiones", null)
        return try {
            if (cursor.moveToFirst()) cursor.getInt(0) else 0
        } finally {
            cursor.close()
        }
    }

    fun saveReflexiones(entities: List<ReflexionEntity>) {
        if (entities.isEmpty()) return
        val db = writableDatabase
        db.beginTransaction()
        try {
            entities.forEach { entity ->
                val values = ContentValues().apply {
                    put("id", entity.id)
                    put("slogan", entity.slogan)
                    put("titulo", entity.titulo)
                    put("subtitulo", entity.subtitulo)
                    put("imagen", entity.imagen)
                    put("alt", entity.alt)
                    put("url", entity.url)
                    put("position", entity.position)
                    put("ultimaActualizacion", entity.ultimaActualizacion)
                }
                db.insertWithOnConflict("reflexiones", null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getReflexionDiaria(fecha: String, ts: Long): org.aamorelos.mobile.domain.model.ReflexionDiariaModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, fecha, titulo, texto, cita, contenido
            FROM reflexion_diaria
            WHERE fecha = ? AND ultimaActualizacion > ?
            """.trimIndent(),
            arrayOf(fecha, ts.toString())
        )
        return try {
            if (cursor.moveToFirst()) {
                org.aamorelos.mobile.domain.model.ReflexionDiariaModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")) ?: "",
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")) ?: "",
                    texto = cursor.getString(cursor.getColumnIndexOrThrow("texto"))?.takeIf { it.isNotBlank() },
                    cita = cursor.getString(cursor.getColumnIndexOrThrow("cita"))?.takeIf { it.isNotBlank() },
                    contenido = cursor.getString(cursor.getColumnIndexOrThrow("contenido"))?.takeIf { it.isNotBlank() }
                )
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    fun saveReflexionDiaria(entity: ReflexionDiariaEntity) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("fecha", entity.fecha)
            put("id", entity.id)
            put("titulo", entity.titulo)
            put("texto", entity.texto)
            put("cita", entity.cita)
            put("contenido", entity.contenido)
            put("ultimaActualizacion", entity.ultimaActualizacion)
        }
        db.insertWithOnConflict("reflexion_diaria", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getReflexionDetail(slogan: String, ts: Long): org.aamorelos.mobile.domain.model.ReflexionDetailModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT slogan, id, titulo, subtitulo, imagen, alt, url, descripcion
            FROM reflexion_detalle
            WHERE slogan = ? AND ultimaActualizacion > ?
            """.trimIndent(),
            arrayOf(slogan, ts.toString())
        )
        return try {
            if (cursor.moveToFirst()) {
                org.aamorelos.mobile.domain.model.ReflexionDetailModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    slogan = cursor.getString(cursor.getColumnIndexOrThrow("slogan")) ?: "",
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")) ?: "",
                    subtitulo = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo"))?.takeIf { it.isNotBlank() },
                    imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))?.takeIf { it.isNotBlank() },
                    alt = cursor.getString(cursor.getColumnIndexOrThrow("alt"))?.takeIf { it.isNotBlank() },
                    url = cursor.getString(cursor.getColumnIndexOrThrow("url"))?.takeIf { it.isNotBlank() },
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))?.takeIf { it.isNotBlank() }
                )
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    fun saveReflexionDetail(entity: ReflexionDetailEntity) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("slogan", entity.slogan)
            put("id", entity.id)
            put("titulo", entity.titulo)
            put("subtitulo", entity.subtitulo)
            put("imagen", entity.imagen)
            put("alt", entity.alt)
            put("url", entity.url)
            put("descripcion", entity.descripcion)
            put("ultimaActualizacion", entity.ultimaActualizacion)
        }
        db.insertWithOnConflict("reflexion_detalle", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getEventos(limit: Int, offset: Int): List<org.aamorelos.mobile.domain.model.EventoItemModel> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, tipo, slogan, url, titulo, subtitulo, imagen, alt, pie
            FROM eventos
            ORDER BY position ASC
            LIMIT ? OFFSET ?
            """.trimIndent(),
            arrayOf(limit.toString(), offset.toString())
        )
        val result = mutableListOf<org.aamorelos.mobile.domain.model.EventoItemModel>()
        try {
            var valid = cursor.moveToFirst()
            while (valid) {
                result.add(
                    org.aamorelos.mobile.domain.model.EventoItemModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")) ?: "",
                        slogan = cursor.getString(cursor.getColumnIndexOrThrow("slogan")) ?: "",
                        url = cursor.getString(cursor.getColumnIndexOrThrow("url"))?.takeIf { it.isNotBlank() },
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")) ?: "",
                        subtitulo = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo"))?.takeIf { it.isNotBlank() },
                        imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))?.takeIf { it.isNotBlank() },
                        alt = cursor.getString(cursor.getColumnIndexOrThrow("alt"))?.takeIf { it.isNotBlank() },
                        pie = cursor.getString(cursor.getColumnIndexOrThrow("pie"))?.takeIf { it.isNotBlank() }
                    )
                )
                valid = cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }
        return result
    }

    fun getEventosCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM eventos", null)
        return try {
            if (cursor.moveToFirst()) cursor.getInt(0) else 0
        } finally {
            cursor.close()
        }
    }

    fun saveEventos(entities: List<EventoEntity>) {
        if (entities.isEmpty()) return
        val db = writableDatabase
        db.beginTransaction()
        try {
            entities.forEach { entity ->
                val values = ContentValues().apply {
                    put("id", entity.id)
                    put("tipo", entity.tipo)
                    put("slogan", entity.slogan)
                    put("url", entity.url)
                    put("titulo", entity.titulo)
                    put("subtitulo", entity.subtitulo)
                    put("imagen", entity.imagen)
                    put("alt", entity.alt)
                    put("pie", entity.pie)
                    put("position", entity.position)
                    put("ultimaActualizacion", entity.ultimaActualizacion)
                }
                db.insertWithOnConflict("eventos", null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getDirectorio(limit: Int, offset: Int): List<org.aamorelos.mobile.domain.model.DirectorioModel> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, pagina, imagen, anio, mes
            FROM directorio
            ORDER BY position ASC
            LIMIT ? OFFSET ?
            """.trimIndent(),
            arrayOf(limit.toString(), offset.toString())
        )
        val result = mutableListOf<org.aamorelos.mobile.domain.model.DirectorioModel>()
        try {
            var valid = cursor.moveToFirst()
            while (valid) {
                result.add(
                    org.aamorelos.mobile.domain.model.DirectorioModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        pagina = cursor.getInt(cursor.getColumnIndexOrThrow("pagina")),
                        imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))?.takeIf { it.isNotBlank() },
                        anio = cursor.getString(cursor.getColumnIndexOrThrow("anio")) ?: "",
                        mes = cursor.getString(cursor.getColumnIndexOrThrow("mes")) ?: ""
                    )
                )
                valid = cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }
        return result
    }

    fun getDirectorioCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM directorio", null)
        return try {
            if (cursor.moveToFirst()) cursor.getInt(0) else 0
        } finally {
            cursor.close()
        }
    }

    fun saveDirectorio(entities: List<DirectorioEntity>) {
        if (entities.isEmpty()) return
        val db = writableDatabase
        db.beginTransaction()
        try {
            entities.forEach { entity ->
                val values = ContentValues().apply {
                    put("id", entity.id)
                    put("pagina", entity.pagina)
                    put("imagen", entity.imagen)
                    put("anio", entity.anio)
                    put("mes", entity.mes)
                    put("position", entity.position)
                    put("ultimaActualizacion", entity.ultimaActualizacion)
                }
                db.insertWithOnConflict("directorio", null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getEventoDetail(eventoId: Int, ts: Long): org.aamorelos.mobile.domain.model.EventoDetailModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id, tipo, slogan, url, titulo, subtitulo, imagen, alt, pie
            FROM evento_detalle
            WHERE id = ? AND ultimaActualizacion > ?
            """.trimIndent(),
            arrayOf(eventoId.toString(), ts.toString())
        )
        return try {
            if (!cursor.moveToFirst()) {
                null
            } else {
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")) ?: ""
                val slogan = cursor.getString(cursor.getColumnIndexOrThrow("slogan")) ?: ""
                val frases = mutableListOf<String>()
                db.query(
                    "evento_frases",
                    arrayOf("texto"),
                    "eventoId = ?",
                    arrayOf(eventoId.toString()),
                    null, null,
                    "position ASC"
                ).use { fc ->
                    while (fc.moveToNext()) {
                        fc.getString(0)?.takeIf { it.isNotBlank() }?.let { frases.add(it) }
                    }
                }
                var aniversarioEntity: EventoAniversarioEntity? = null
                db.query(
                    "evento_aniversario",
                    arrayOf("epigrafe", "kicker", "fraseReflexion", "sede", "diasUnidad", "juntaPublica", "contactoNombre", "contactoTelefono"),
                    "eventoId = ?",
                    arrayOf(eventoId.toString()),
                    null, null, null
                ).use { ac ->
                    if (ac.moveToFirst()) {
                        val dias = mutableListOf<EventoAniversarioDiaEntity>()
                        db.query(
                            "evento_aniversario_dias",
                            arrayOf("dia", "tema", "fecha", "grupos", "numero", "esJuntaPublica"),
                            "eventoId = ?",
                            arrayOf(eventoId.toString()),
                            null, null,
                            "position ASC"
                        ).use { dc ->
                            while (dc.moveToNext()) {
                                dias.add(
                                    EventoAniversarioDiaEntity(
                                        dia = dc.getString(0) ?: "",
                                        tema = dc.getString(1) ?: "",
                                        fecha = dc.getString(2) ?: "",
                                        grupos = dc.getString(3) ?: "",
                                        numero = dc.getString(4) ?: "",
                                        esJuntaPublica = dc.getInt(5) == 1
                                    )
                                )
                            }
                        }
                        aniversarioEntity = EventoAniversarioEntity(
                            epigrafe = ac.getString(0),
                            kicker = ac.getString(1),
                            fraseReflexion = ac.getString(2),
                            sede = ac.getString(3),
                            diasUnidad = ac.getString(4),
                            juntaPublica = ac.getString(5),
                            contactoNombre = ac.getString(6),
                            contactoTelefono = ac.getString(7),
                            dias = dias
                        )
                    }
                }
                EventoDetailEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    tipo = tipo,
                    slogan = slogan,
                    url = cursor.getString(cursor.getColumnIndexOrThrow("url"))?.takeIf { it.isNotBlank() },
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")) ?: "",
                    subtitulo = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo"))?.takeIf { it.isNotBlank() },
                    imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))?.takeIf { it.isNotBlank() },
                    alt = cursor.getString(cursor.getColumnIndexOrThrow("alt"))?.takeIf { it.isNotBlank() },
                    pie = cursor.getString(cursor.getColumnIndexOrThrow("pie"))?.takeIf { it.isNotBlank() },
                    frases = frases,
                    aniversario = aniversarioEntity,
                    ultimaActualizacion = 0L
                ).toModel()
            }
        } finally {
            cursor.close()
        }
    }

    fun saveEventoDetail(entity: EventoDetailEntity) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put("id", entity.id)
                put("tipo", entity.tipo)
                put("slogan", entity.slogan)
                put("url", entity.url)
                put("titulo", entity.titulo)
                put("subtitulo", entity.subtitulo)
                put("imagen", entity.imagen)
                put("alt", entity.alt)
                put("pie", entity.pie)
                put("ultimaActualizacion", entity.ultimaActualizacion)
            }
            db.insertWithOnConflict("evento_detalle", null, values, SQLiteDatabase.CONFLICT_REPLACE)
            db.delete("evento_frases", "eventoId = ?", arrayOf(entity.id.toString()))
            entity.frases.forEachIndexed { index, texto ->
                val fv = ContentValues().apply {
                    put("eventoId", entity.id)
                    put("position", index)
                    put("texto", texto)
                }
                db.insertWithOnConflict("evento_frases", null, fv, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.delete("evento_aniversario", "eventoId = ?", arrayOf(entity.id.toString()))
            db.delete("evento_aniversario_dias", "eventoId = ?", arrayOf(entity.id.toString()))
            entity.aniversario?.let { an ->
                val av = ContentValues().apply {
                    put("eventoId", entity.id)
                    put("epigrafe", an.epigrafe)
                    put("kicker", an.kicker)
                    put("fraseReflexion", an.fraseReflexion)
                    put("sede", an.sede)
                    put("diasUnidad", an.diasUnidad)
                    put("juntaPublica", an.juntaPublica)
                    put("contactoNombre", an.contactoNombre)
                    put("contactoTelefono", an.contactoTelefono)
                }
                db.insertWithOnConflict("evento_aniversario", null, av, SQLiteDatabase.CONFLICT_REPLACE)
                an.dias.forEachIndexed { index, dia ->
                    val dv = ContentValues().apply {
                        put("eventoId", entity.id)
                        put("position", index)
                        put("dia", dia.dia)
                        put("tema", dia.tema)
                        put("fecha", dia.fecha)
                        put("grupos", dia.grupos)
                        put("numero", dia.numero)
                        // esJuntaPublica comes from domain; dias list is EventoAniversarioDiaEntity in entity path
                        put("esJuntaPublica", dia.esJuntaPublica)
                    }
                    db.insertWithOnConflict("evento_aniversario_dias", null, dv, SQLiteDatabase.CONFLICT_REPLACE)
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    companion object {
        private const val CREATE_GRUPO_DETALLE_SQL = """
            CREATE TABLE IF NOT EXISTS grupo_detalle (
                estadoId TEXT NOT NULL,
                id TEXT NOT NULL,
                distritoId TEXT NOT NULL,
                municipioId TEXT NOT NULL,
                tipoId INTEGER NOT NULL,
                lenguaId INTEGER NOT NULL,
                generoId INTEGER NOT NULL,
                nombre TEXT NOT NULL,
                fechaCreacion TEXT,
                direccion TEXT,
                vecinos TEXT,
                codigoPostal TEXT,
                referencias TEXT,
                localidad TEXT,
                cpr TEXT,
                horarioLunes TEXT,
                horarioMartes TEXT,
                horarioMiercoles TEXT,
                horarioJueves TEXT,
                horarioViernes TEXT,
                horarioSabado TEXT,
                horarioDomingo TEXT,
                horarioObservacion TEXT,
                lenguajeDetalle TEXT,
                permiteFumar INTEGER NOT NULL,
                esActivo INTEGER NOT NULL,
                latitud TEXT,
                longitud TEXT,
                fumarTexto TEXT,
                municipio TEXT NOT NULL,
                tipo TEXT NOT NULL,
                genero TEXT NOT NULL,
                lengua TEXT NOT NULL,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (estadoId, id)
            )
        """
        private const val CREATE_DISTRITO_DETALLE_SQL = """
            CREATE TABLE IF NOT EXISTS distrito_detalle (
                id TEXT NOT NULL,
                areaId TEXT,
                municipioId TEXT,
                nombre TEXT NOT NULL,
                distritoNumero INTEGER,
                fechaCreacion TEXT,
                direccion TEXT,
                vecinos TEXT,
                codigoPostal TEXT,
                referencia TEXT,
                telefono TEXT,
                servicioHoras TEXT,
                esActivo INTEGER,
                municipio TEXT,
                area TEXT,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (id)
            )
        """
        private const val CREATE_REFLEXIONES_SQL = """
            CREATE TABLE IF NOT EXISTS reflexiones (
                id INTEGER NOT NULL,
                slogan TEXT NOT NULL,
                titulo TEXT NOT NULL,
                subtitulo TEXT NOT NULL,
                imagen TEXT,
                alt TEXT,
                url TEXT,
                position INTEGER NOT NULL,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (id)
            )
        """
        private const val CREATE_REFLEXION_DIARIA_SQL = """
            CREATE TABLE IF NOT EXISTS reflexion_diaria (
                fecha TEXT NOT NULL,
                id INTEGER NOT NULL,
                titulo TEXT NOT NULL,
                texto TEXT,
                cita TEXT,
                contenido TEXT,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (fecha)
            )
        """
        private const val CREATE_REFLEXION_DETALLE_SQL = """
            CREATE TABLE IF NOT EXISTS reflexion_detalle (
                slogan TEXT NOT NULL,
                id INTEGER NOT NULL,
                titulo TEXT NOT NULL,
                subtitulo TEXT,
                imagen TEXT,
                alt TEXT,
                url TEXT,
                descripcion TEXT,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (slogan)
            )
        """
        private const val CREATE_EVENTOS_SQL = """
            CREATE TABLE IF NOT EXISTS eventos (
                id INTEGER NOT NULL,
                tipo TEXT NOT NULL,
                slogan TEXT NOT NULL,
                url TEXT,
                titulo TEXT NOT NULL,
                subtitulo TEXT,
                imagen TEXT,
                alt TEXT,
                pie TEXT,
                position INTEGER NOT NULL,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (id)
            )
        """
        private const val CREATE_EVENTO_DETALLE_SQL = """
            CREATE TABLE IF NOT EXISTS evento_detalle (
                id INTEGER NOT NULL,
                tipo TEXT NOT NULL,
                slogan TEXT NOT NULL,
                url TEXT,
                titulo TEXT NOT NULL,
                subtitulo TEXT,
                imagen TEXT,
                alt TEXT,
                pie TEXT,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (id)
            )
        """
        private const val CREATE_EVENTO_FRASES_SQL = """
            CREATE TABLE IF NOT EXISTS evento_frases (
                eventoId INTEGER NOT NULL,
                position INTEGER NOT NULL,
                texto TEXT NOT NULL,
                PRIMARY KEY (eventoId, position)
            )
        """
        private const val CREATE_EVENTO_ANIVERSARIO_SQL = """
            CREATE TABLE IF NOT EXISTS evento_aniversario (
                eventoId INTEGER NOT NULL,
                epigrafe TEXT,
                kicker TEXT,
                fraseReflexion TEXT,
                sede TEXT,
                diasUnidad TEXT,
                juntaPublica TEXT,
                contactoNombre TEXT,
                contactoTelefono TEXT,
                PRIMARY KEY (eventoId)
            )
        """
        private const val CREATE_EVENTO_ANIVERSARIO_DIAS_SQL = """
            CREATE TABLE IF NOT EXISTS evento_aniversario_dias (
                eventoId INTEGER NOT NULL,
                position INTEGER NOT NULL,
                dia TEXT NOT NULL,
                tema TEXT NOT NULL,
                fecha TEXT NOT NULL,
                grupos TEXT NOT NULL,
                numero TEXT NOT NULL,
                esJuntaPublica INTEGER NOT NULL,
                PRIMARY KEY (eventoId, position)
            )
        """
        private const val CREATE_DIRECTORIO_SQL = """
            CREATE TABLE IF NOT EXISTS directorio (
                id INTEGER NOT NULL,
                pagina INTEGER NOT NULL,
                imagen TEXT,
                anio TEXT NOT NULL,
                mes TEXT NOT NULL,
                position INTEGER NOT NULL,
                ultimaActualizacion INTEGER NOT NULL,
                PRIMARY KEY (id)
            )
        """
    }
}
