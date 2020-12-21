package com.pru.hiltarchi.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.pru.hiltarchi.models.TodoList
import com.pru.hiltarchi.repositories.TodoRepository
import com.pru.hiltarchi.utils.Resource
import kotlinx.coroutines.launch

class TodoViewModel @ViewModelInject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private val _todos = MutableLiveData<Resource<TodoList>>()
    val todos: LiveData<Resource<TodoList>>
        get() = _todos

   /* init {
        getTodos()
    }*/

    fun getTodos() = viewModelScope.launch {
        _todos.postValue(Resource.Loading())
        try {
            val data = TodoList(data = todoRepository.getTodos())
            _todos.postValue(Resource.Success(data))
        } catch (e: Exception) {
            _todos.postValue(Resource.Error(message = "Error! ${e.message ?: "Something went wrong"}"))
        }
    }
}