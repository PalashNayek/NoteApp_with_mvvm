package com.example.noteapp_with_mvvm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp_with_mvvm.R
import com.example.noteapp_with_mvvm.adapter.NoteAdapter
import com.example.noteapp_with_mvvm.databinding.FragmentMainBinding
import com.example.noteapp_with_mvvm.model.noteResponse.NoteResponse
import com.example.noteapp_with_mvvm.utils.NetworkResult
import com.example.noteapp_with_mvvm.utils.TokenManager
import com.example.noteapp_with_mvvm.view_model.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var tokenManager : TokenManager

    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        noteViewModel.getAllNotesViewModel()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

    }

    private fun bindObservers() {
        /*noteViewModel.notesLiveDataNoteViewModel.observe(viewLifecycleOwner, Observer {

        })*/

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                noteViewModel.notesStateFlowNoteViewModel.collect{
                    binding.progressBar.isVisible = false
                    when(it){
                        is NetworkResult.Error -> {
                            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResult.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is NetworkResult.Success -> {
                            adapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }

    //note item onClick ........................................
    private fun onNoteClicked(noteResponse: NoteResponse){

        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
        //Toast.makeText(context, noteResponse.title, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}