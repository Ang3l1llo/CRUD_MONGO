package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.example.models.*
import java.time.LocalDate
import java.time.LocalDateTime


class Create(
    private val collUsers: MongoCollection<User>,
    private val collNews: MongoCollection<News>,
    private val collComments: MongoCollection<Comment>
) {

    fun createUser() {

        println("Introduzca correo electrónico:")
        val email = readln().lowercase()

        val emailExistente = collUsers.find(Filters.eq("_id", email)).firstOrNull()

        if (emailExistente != null) {
            println("Error: Ya existe un usuario con el email '$email'. Proporcione otro correo.")
            return
        }

        println("Introduzca nombre completo del usuario:")
        val nombre = readln().lowercase()

        println("Elija nick:")
        val nick = readln().lowercase()

        val nickExistente = collUsers.find(Filters.eq("nick", nick)).firstOrNull()

        if (nickExistente != null) {
            println("Error: Ya existe un usuario con el nick '$nick'. Elija otro nick.")
            return
        }

        val estado = UserStatus.ACTIVE

        val telefonos = mutableListOf<String>()
        println("Introduzca los teléfonos de contacto (uno por línea, escriba 'fin' para terminar):")
        while (true) {
            val tlfn = readln().trim()
            if (tlfn.equals("fin", ignoreCase = true)) break
            if (tlfn.isNotEmpty()) {
                telefonos.add(tlfn)
            }
        }
        println("Introduzca su dirección. Primero la calle: ")
        val calle = readln().lowercase()
        println("Número:")
        val num = readln()
        println("Puerta:")
        val puerta = readln().lowercase()
        println("CP: ")
        val cp = readln()
        println("Ciudad:")
        val ciudad = readln().lowercase()

        val direccion = Address(calle, num, puerta, cp, ciudad)

        val nuevoUsuario = User(email, nombre, nick, estado, telefonos, direccion)

        collUsers.insertOne(nuevoUsuario)
        println("Usuario registrado correctamente")
    }

    fun createNew() {
        println("Título de la noticia:")
        val titulo = readln().lowercase()

        val tituloExistente = collNews.find(Filters.eq("titulo", titulo)).firstOrNull()
        if (tituloExistente != null) {
            println("Error: Ya existe una noticia con el título: '$titulo'.")
            return
        }

        println("Cuerpo de la noticia: ")
        val cuerpo = readln().lowercase()

        val fechaPub = LocalDate.now()

        val tags = mutableListOf<String>()
        println("Introduzca los tags de la noticia (uno por línea, escriba 'fin' para terminar):")
        while (true) {
            val tag = readln().trim().lowercase()
            if (tag.equals("fin", ignoreCase = true)) break
            if (tag.isNotEmpty()) {
                tags.add(tag)
            }
        }

        println("Introduzca el email del usuario que publica la noticia:")
        val userEmail = readln().lowercase()

        val usuarioExistente = collUsers.find(Filters.eq("_id", userEmail)).firstOrNull()
        if (usuarioExistente == null) {
            println("Error: No existe un usuario con el email '$userEmail'.")
            return
        }

        val nuevaNoticia = News(titulo, cuerpo, fechaPub, tags, usuarioExistente.nick)

        collNews.insertOne(nuevaNoticia)
        println("Noticia publicada con éxito")
    }

    fun createComment() {
        println("Introduzca el email del usuario que publica el comentario:")
        val userEmail = readln().lowercase()

        val usuarioExistente = collUsers.find(Filters.eq("_id", userEmail)).firstOrNull()
        if (usuarioExistente == null) {
            println("Error: No existe un usuario con el email '$userEmail'.")
            return
        }

        if (usuarioExistente.estado == UserStatus.INACTIVE || usuarioExistente.estado == UserStatus.BANNED) {
            println("Error: El usuario no puede realizar comentarios porque su estado es ${usuarioExistente.estado}.")
            return
        }
        println("Introduzca el título de la noticia en la que quiere hacer el comentario:")
        val noticia = readln().lowercase()

        val noticiaExistente = collNews.find(Filters.eq("titulo", noticia)).firstOrNull()
        if (noticiaExistente == null) {
            println("Error: No existe una noticia con el título '$noticia'.")
            return
        }
        println("Introduzca el comentario:")
        val comentario = readln().lowercase()

        val fecha = LocalDateTime.now()

        val nuevoComentario = Comment(usuarioExistente.nick, noticia, comentario, fecha)

        collComments.insertOne(nuevoComentario)
        println("Comentario publicado con éxito")

    }
}