package com.pru.hiltarchi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pru.hiltarchi.R
import com.pru.hiltarchi.databinding.FragmentTodoDetailsBinding
import com.pru.hiltarchi.ui.BaseFragment
import com.pru.hiltarchi.utils.Resource
import com.pru.hiltarchi.utils.hide
import com.pru.hiltarchi.utils.show
import com.pru.hiltarchi.viewmodels.TodoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoDetailFragment : BaseFragment(R.layout.fragment_todo_details) {
    private lateinit var todoDetailBinding: FragmentTodoDetailsBinding
    private val todoViewModel by viewModels<TodoDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoDetailBinding = FragmentTodoDetailsBinding.bind(view)
        setupObserver()
        todoDetailBinding.fabView.setOnClickListener {
            setFragmentResult(
                "Task",
                bundleOf(
                    "Task" to "aa",
                )
            )
            findNavController().popBackStack()
        }
    }

    private fun setupObserver() {
        todoViewModel.todoDetail.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    todoDetailBinding.pbView.show()
                }
                is Resource.Success -> {
                    todoDetailBinding.pbView.hide()
                    val stringBuilder = StringBuilder()
                    stringBuilder.append(it.data?.id.toString() + "\n")
                    stringBuilder.append(it.data?.title + "\n")
                    stringBuilder.append(it.data?.completed)
                    todoDetailBinding.tvDetail.text = stringBuilder.toString()
                }
                is Resource.Error -> {
                    todoDetailBinding.pbView.hide()
                    todoDetailBinding.tvDetail.text = it.message
                }
            }
        })
    }
}