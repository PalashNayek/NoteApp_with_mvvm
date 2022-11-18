package com.example.noteapp_with_mvvm.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp_with_mvvm.model.noteRequest.NoteRequest
import com.example.noteapp_with_mvvm.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel(){

    val notesStateFlowNoteViewModel get() = noteRepository.notesStateFlowRepository
    val statusStateFlowNoteViewModel get() = noteRepository.statusStateFlowRepository

    fun getAllNotesViewModel(){
        viewModelScope.launch {
            noteRepository.getNotesRepository()
        }
    }

    fun createNotesViewModel(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNoteRepository(noteRequest)
        }
    }

    fun deleteNotesViewModel(noteId: String){
        viewModelScope.launch {
            noteRepository.deleteNoteRepository(noteId)
        }
    }

    fun updateNotesViewModel(noteId : String, noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNoteRepository(noteId, noteRequest)
        }
    }
}