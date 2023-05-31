package com.android.marinmusicplayer

data class Song(var title: String, var artist: String, var path: String): java.io.Serializable

// je vais rendre la classe serialasible en héritant de la class java : java.io.Serializable pour transformer l'object et données serialasible