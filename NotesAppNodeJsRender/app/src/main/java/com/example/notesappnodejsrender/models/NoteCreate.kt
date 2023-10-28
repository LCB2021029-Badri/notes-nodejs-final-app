package com.example.notesappnodejsrender.models

data class NoteCreate(
    val id: String,
    val userid: String,
    val title: String,
    val content: String,
)