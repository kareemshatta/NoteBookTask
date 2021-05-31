package com.example.notebook.model

class Note(
    var id: String,
    val text: String
){
    constructor() : this("","")
}