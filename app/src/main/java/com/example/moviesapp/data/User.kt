package com.example.moviesapp.data

open class User(email: String, password: String) {
    var email: String = ""
    var password: String = ""

    init {
        this.email = email
        this.password = password
    }

}