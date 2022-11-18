package com.example.noteapp_with_mvvm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteapp_with_mvvm.R
import com.example.noteapp_with_mvvm.databinding.FragmentLoginBinding
import com.example.noteapp_with_mvvm.databinding.FragmentMainBinding
import com.example.noteapp_with_mvvm.databinding.FragmentNoteBinding
import com.example.noteapp_with_mvvm.model.noteRequest.NoteRequest
import com.example.noteapp_with_mvvm.model.noteResponse.NoteResponse
import com.example.noteapp_with_mvvm.utils.NetworkResult
import com.example.noteapp_with_mvvm.view_model.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note: NoteResponse? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        /*noteViewModel.statusLiveDataNoteViewModel.observe(viewLifecycleOwner, Observer {

        })*/
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                noteViewModel.statusStateFlowNoteViewModel.collect{
                    when (it) {
                        is NetworkResult.Success -> {
                            findNavController().popBackStack()
                        }
                        is NetworkResult.Error -> {

                        }
                        is NetworkResult.Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNotesViewModel(it!!._id) }
        }
        binding.apply {
            btnSubmit.setOnClickListener {
                val title = txtTitle.text.toString()
                val description = txtDescription.text.toString()
                val noteRequest = NoteRequest(title, description)
                if (note == null) {
                    noteViewModel.createNotesViewModel(noteRequest)
                } else {
                    noteViewModel.updateNotesViewModel(note!!._id, noteRequest)
                }
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {
            note = Gson().fromJson<NoteResponse>(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }
        else{
            binding.addEditText.text = resources.getString(R.string.add_note)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}