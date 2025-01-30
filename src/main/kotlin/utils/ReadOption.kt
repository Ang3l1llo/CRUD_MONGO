package org.example.utils

fun readOption(): Int {
    while (true) {
        println("Seleccione una opción:")
        val input = readln()
        try {
            return input.toInt()
        } catch (e: NumberFormatException) {
            println("Error: '$input' no es un número válido. Intente de nuevo.")
        }
    }
}