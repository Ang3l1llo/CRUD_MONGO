package org.example.models

import java.time.LocalDate

data class News (
    val titulo: String,
    val cuerpo: String,
    val fechaPub: LocalDate,
    val tag: List<String>,
    val user: String
)