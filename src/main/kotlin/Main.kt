package org.example

import com.mongodb.client.MongoCollection
import org.example.connection.MongoConnection
import org.example.models.*
import org.example.repository.Create
import org.example.repository.Delete
import org.example.repository.Read
import org.example.repository.Update
import org.example.utils.menu
import org.example.utils.readOption

fun main() {
    //conectar a la bbdd
    val database = MongoConnection.getDatabase("CRUD")

    //conseguir las colecciones
    val collUsers: MongoCollection<User> = database.getCollection("collUsers", User::class.java)
    val collNews: MongoCollection<News> = database.getCollection("collNews", News::class.java)
    val collComments: MongoCollection<Comment> = database.getCollection("collComments", Comment::class.java)

    //instancia de las clases que realizan el CRUD
    val create = Create(collUsers, collNews, collComments)
    val delete = Delete(collNews,collComments)
    val update = Update(collNews,collComments)
    val read = Read(collUsers, collNews, collComments)

    var option: Int

    try {
        do {
            menu()

            option = readOption()

            when (option) {

                1 -> create.createUser()

                2 -> create.createNew()

                3 -> create.createComment()

                4 -> delete.deleteNew()

                5 -> update.updateNew()

                6 -> read.getUserNews()

                7 -> read.getNewComments()

                8 -> read.getNewByTag()

                9 -> read.getLastNews()

                10 -> println("Saliendo..")
            }

        } while (option != 10)

    } catch (e: Exception) {
        println("Error: ${e.message}")
    } finally {
        MongoConnection.close()
    }
}