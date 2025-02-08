package com.example.todo.viewmodel

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items  // Added import for items
import androidx.compose.material3.Divider   // Use Divider from Material3 instead of HorizontalDivider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.model.Todo
import com.example.todo.model.TodosApi
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    // Consider using a state-based list (e.g., snapshotStateListOf) if you plan to update this list dynamically.
    val todos = mutableStateListOf<Todo>()

    init {
        getTodoList()
    }

    private fun getTodoList() {
        Log.d("API_start-----------", "test")
        viewModelScope.launch {
            var todosApi: TodosApi? = null
            try {
                val todosApi = TodosApi.getInstance()
                val apiTodos = todosApi.getTodos()

                Log.d("API_RESPONSE", "Fetched todos: $apiTodos")

                todos.clear()
                todos.addAll(apiTodos)

            } catch (e: Exception) {
                Log.d("ERROR", e.message.toString())
            }
        }
    }
}

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel = viewModel()
) {
    TodoList(modifier = modifier, todos = todoViewModel.todos)
}

@Composable
fun TodoList(modifier: Modifier = Modifier, todos: List<Todo>) {
    LazyColumn(modifier = modifier) {
        items(todos) { todo ->
            Text(
                text = todo.title,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            HorizontalDivider(color=Color.LightGray, thickness = 2.dp)
        }
    }
}
