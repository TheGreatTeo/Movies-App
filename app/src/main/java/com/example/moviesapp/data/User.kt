package com.example.moviesapp.data

open class User(email: String, password: String,username: String) {
    var email = ""
    var password = ""
    var username = ""
    init {
        this.email = email
        this.password = password
        this.username = username
    }

}