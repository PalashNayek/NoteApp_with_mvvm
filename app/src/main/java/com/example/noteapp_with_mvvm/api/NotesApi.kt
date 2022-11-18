package com.example.noteapp_with_mvvm.api

import com.example.noteapp_with_mvvm.model.noteRequest.NoteRequest
import com.example.noteapp_with_mvvm.model.noteResponse.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesApi {

    @GET("/note")
    suspend fun getNotes() : Response<List<NoteResponse>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest) : Response<NoteResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId : String, @Body noteRequest: NoteRequest) : Response<NoteResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String) : Response<NoteResponse>
}