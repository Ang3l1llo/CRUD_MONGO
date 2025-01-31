package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.example.models.Comment
import org.example.models.News

class Delete(private val collNews: MongoCollection<News>,private val collComments: MongoCollection<Comment>) {

    fun deleteNew(){
        println("Título de la noticia a eliminar:")
        val titulo = readln().trim()

        val tituloAeliminar = Filters.eq("titulo", titulo)
        val tituloExistente = collNews.find(tituloAeliminar).firstOrNull()
        if (tituloExistente == null) {
            println("Error: No existe una noticia con el título: '$titulo'.")
            return
        }

        collNews.deleteOne(tituloAeliminar)
        println("Noticia eliminada exitosamente")

        val filtroComentarios = Filters.eq("new", titulo)
        collComments.deleteMany(filtroComentarios)

    }
}