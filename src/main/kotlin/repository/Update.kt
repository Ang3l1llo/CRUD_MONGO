package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.example.models.Comment
import org.example.models.News

class Update(private val collNews: MongoCollection<News>,private val collComments: MongoCollection<Comment>) {

    fun updateNew(){
        println("Título de la noticia a modificar:")
        val titulo = readln().trim().lowercase()

        val tituloAmodificar = Filters.eq("titulo", titulo)
        val noticiaExistente = collNews.find(tituloAmodificar).firstOrNull()
        if (noticiaExistente == null) {
            println("Error: No existe una noticia con el título: '$titulo'.")
            return
        }

        println("Introduzca nuevo título para su noticia:")
        val tituloNuevo = readln().trim().lowercase()

        println("Introduzca la información de la noticia:")
        val nuevoCuerpo = readln().lowercase()

        val nuevasEtiquetas = mutableListOf<String>()
        println("Introduzca los nuevos tags de la noticia (uno por línea, escriba 'fin' para terminar):")
        while (true) {
            val tag = readln().trim().lowercase()
            if (tag.equals("fin", ignoreCase = true)) break
            if (tag.isNotEmpty()) {
                nuevasEtiquetas.add(tag)
            }
        }

        val nuevaNoticia = News(tituloNuevo,nuevoCuerpo,noticiaExistente.fechaPub,nuevasEtiquetas,noticiaExistente.user)

        collNews.replaceOne(tituloAmodificar,nuevaNoticia)
        println("Noticia actualizada")

        //Ahora hay que modificar también todos esos comentarios asociados a la noticia
        val filtroComentarios = Filters.eq("new", titulo)
        val actualizacionComentarios = Updates.set("new", tituloNuevo)
        collComments.updateMany(filtroComentarios, actualizacionComentarios)
    }
}