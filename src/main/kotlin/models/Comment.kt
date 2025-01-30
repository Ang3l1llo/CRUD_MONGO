package org.example.models

import java.time.LocalDateTime

data class Comment (
    val user: String,
    val new: String,
    val texto: String,
    val fecha: LocalDateTime
)