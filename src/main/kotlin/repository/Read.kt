package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import org.example.models.Comment
import org.example.models.News
import org.example.models.User

class Read(
    private val collUsers: MongoCollection<User>,
    private val collNews: MongoCollection<News>,
    private val collComments: MongoCollection<Comment>
) {

    fun getUserNews() {
        println("Introduzca el nick del usuario:")
        val email = readln().trim().lowercase()

        val noticias = collNews.find(Filters.eq("user", email)).toList()
        if (noticias.isEmpty()) {
            println("No se encontraron noticias del usuario con email: '$email'.")
            return
        }

        noticias.forEach { noticia ->
            println("Título: ${noticia.titulo}")
            println("Cuerpo: ${noticia.cuerpo}")
            println("Fecha de publicación: ${noticia.fechaPub}")
            println("Tags: ${noticia.tag.joinToString(", ")}")
            println("---")
        }
    }

    fun getNewComments() {
        println("Introduzca el título de la noticia: ")
        val tituloNoticia = readln().trim().lowercase()

        val pipeline = listOf(
            Aggregates.match(Filters.eq("new", tituloNoticia)),
            Aggregates.lookup(
                "collNews",
                "new",
                "titulo",
                "comentarios"
            )
        )

        val resultado = collComments.aggregate(pipeline).toList()
        resultado.forEach { comentario ->
            println("Usuario : ${comentario.user}")
            println("Texto : ${comentario.texto}")
            println("Fecha de publicación : ${comentario.fecha}")

        }
    }

    fun getNewByTag() {
        println("Introduzca una etiqueta para buscar la noticia:")
        val etiqueta = readln().trim().lowercase()

        val noticias = collNews.find(Filters.eq("tag", etiqueta)).toList()
        if (noticias.isEmpty()) {
            println("No se encontraron noticias con la etiqueta '$etiqueta'.")
            return
        }

        noticias.forEach { noticia ->
            println("Título: ${noticia.titulo}")
            println("Usuario: ${noticia.user}")
            println("Cuerpo: ${noticia.cuerpo}")
            println("Fecha de publicación: ${noticia.fechaPub}")
            println("Tags: ${noticia.tag.joinToString(", ")}")
            println("---")
        }
    }

    fun getLastNews() {
        val sortDescending = Sorts.descending("fechaPub")
        val noticias = collNews.find()
            .sort(sortDescending)
            .limit(10)
            .toList()

        if (noticias.isEmpty()) {
            println("No se encontraron noticias recientes")
            return
        }

        noticias.forEach { noticia ->
            println("Título: ${noticia.titulo}")
            println("Usuario: ${noticia.user}")
            println("Cuerpo: ${noticia.cuerpo}")
            println("Fecha de publicación: ${noticia.fechaPub}")
            println("Tags: ${noticia.tag.joinToString(", ")}")
            println("---")
        }
    }
}