package com.example.moviesapp.data

class User(email: String, password: String) {
    private var email: String = ""
    private var password: String = ""

    init {
        this.email = email
        this.password = password
    }
}