package org.example.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class User(
    @BsonId
    val _id: String?,
    val nombre: String,
    val nick: String,
    val estado: UserStatus,

    @BsonProperty("telefonos")
    val tlfn: List<String>,
    val direccion: Address?
)

enum class UserStatus {
    ACTIVE,
    INACTIVE,
    BANNED
}