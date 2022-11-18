package com.example.noteapp_with_mvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp_with_mvvm.api.NotesApi
import com.example.noteapp_with_mvvm.model.noteRequest.NoteRequest
import com.example.noteapp_with_mvvm.model.noteResponse.NoteResponse
import com.example.noteapp_with_mvvm.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesApi: NotesApi) {

    private val _notesMutableStateFlow = MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading())
    val notesStateFlowRepository : StateFlow<NetworkResult<List<NoteResponse>>> get() = _notesMutableStateFlow

    private val _statusMutableStateFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading())
    val statusStateFlowRepository : StateFlow<NetworkResult<String>> get() = _statusMutableStateFlow

    //get all notes response and set live data .............................
    suspend fun getNotesRepository(){
        val response = notesApi.getNotes()
        if (response.isSuccessful && response.body() != null){
            _notesMutableStateFlow.emit(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesMutableStateFlow.emit(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _notesMutableStateFlow.emit(NetworkResult.Error("Something went to wrong"))
        }
    }

    //create notes response and set live data.....................
    suspend fun createNoteRepository(noteRequest: NoteRequest){
        _statusMutableStateFlow.emit(NetworkResult.Loading())
        val response = notesApi.createNote(noteRequest)
        handleResponse(response, "note created")
    }



    //delete note and set live data..........................
    suspend fun deleteNoteRepository(noteId : String){
        _statusMutableStateFlow.emit(NetworkResult.Loading())
        val response = notesApi.deleteNote(noteId)
        handleResponse(response, "note deleted")
    }
    //update note and set live data..........................
    suspend fun updateNoteRepository(noteId : String, noteRequest: NoteRequest){
        _statusMutableStateFlow.emit(NetworkResult.Loading())
        val response = notesApi.updateNote(noteId, noteRequest)
        handleResponse(response, "note updated")
    }

    private suspend fun handleResponse(response: Response<NoteResponse>, message : String) {
        if (response.isSuccessful && response.body() != null) {
            _statusMutableStateFlow.emit(NetworkResult.Success(message))
        } else {
            _statusMutableStateFlow.emit(NetworkResult.Error("Something went to wrong"))
        }
    }
}