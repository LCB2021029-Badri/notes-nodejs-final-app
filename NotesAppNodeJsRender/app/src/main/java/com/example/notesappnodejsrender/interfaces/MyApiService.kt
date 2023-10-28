package com.example.notesappnodejsrender.interfaces

import com.example.notesappnodejsrender.models.Note
import com.example.notesappnodejsrender.models.NoteCreate
import com.example.notesappnodejsrender.models.PostNoteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApiService {
    @GET("/notes/list")
    fun fetchData(): Call<List<Note>>

    @POST("/notes/list")
    fun filterUserId(@Body data: NoteCreate): Call<List<Note>>

    @POST("/notes/add")
    fun postData(@Body data: NoteCreate): Call<PostNoteResponse>

    @POST("/notes/delete")
    fun deleteById(@Body data: NoteCreate): Call<PostNoteResponse>
}