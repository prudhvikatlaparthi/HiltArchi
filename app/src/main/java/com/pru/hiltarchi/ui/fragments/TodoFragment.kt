package com.pru.hiltarchi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pru.hiltarchi.R
import com.pru.hiltarchi.adapters.TodoAdapter
import com.pru.hiltarchi.databinding.FragmentTodoBinding
import com.pru.hiltarchi.listeners.onResumeListener
import com.pru.hiltarchi.ui.BaseFragment
import com.pru.hiltarchi.utils.Resource
import com.pru.hiltarchi.utils.hide
import com.pru.hiltarchi.utils.show
import com.pru.hiltarchi.viewmodels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoFragment : BaseFragment(R.layout.fragment_todo), onResumeListener {
    private lateinit var todoBinding: FragmentTodoBinding
    private val todoAdapter: TodoAdapter by lazy { TodoAdapter(emptyList()) }
    private val todoViewModel by viewModels<TodoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoBinding = FragmentTodoBinding.bind(view)
        setupObserver()
        todoBinding.apply {
            rcView.run {
                this.layoutManager = LinearLayoutManager(requireContext())
                this.adapter = todoAdapter
            }
        }
        todoAdapter.setItemClickListener {
            val action =
                TodoFragmentDirections.actionHomeFragmentToTodoDetailFragment(todoID = it.id ?: -1)
            findNavController().navigate(action)
        }
        setFragmentResultListener("Task") { _, bundle ->
            todoViewModel.getTodos()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupObserver() {
        todoViewModel.todos.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    todoBinding.pbView.show()
                }
                is Resource.Success -> {
                    todoBinding.pbView.hide()
                    todoAdapter.addData(it.data!!.data)
                }
                is Resource.Error -> {
                    todoBinding.pbView.hide()
                    todoBinding.errorView.show()
                    todoBinding.errorView.text = it.message
                }
            }
        })
    }

    override fun onResumedCalled() {
        todoViewModel.getTodos()
    }
}