package com.ozkanfurkan.turkcellintro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ozkanfurkan.turkcellintro.model.Todo
import com.ozkanfurkan.turkcellintro.viewmodel.ToDoListViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.websocket.Frame

// Burada ekran tanımlarını yap.
sealed class Screen(val route: String) {
    data object Register: Screen("register")
    data object Homepage: Screen("homepage")
    data object AddTodo: Screen("addtodo")
}

// Telefon çevirildiği an => Yeniden başlatılır.
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        val supabase = createSupabaseClient(
            supabaseUrl = "https://lqdlewuvfadrqxpatgdg.supabase.co", //local.properties
            supabaseKey = "sb_publishable__56iluzuZmwiPpEdXxq8qw_znOrZNjo"
        ) {
            install(Postgrest)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Scaffold {
                    paddingValues ->  MyNavigatableApp(Modifier.padding(paddingValues))
            }
        }
    }
}

@Composable
fun MyNavigatableApp(modifier: Modifier) {
    val navController = rememberNavController()
    val todoViewModel: ToDoListViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Homepage.route) {
        composable(Screen.Register.route) { RegisterScreen(modifier, navController) }
        composable(Screen.Homepage.route) { 
            Homepage(modifier, todoViewModel, onNavigateToAdd = {
                navController.navigate(Screen.AddTodo.route)
            }) 
        }
        composable(Screen.AddTodo.route) {
            AddTodoScreen(modifier, todoViewModel, onBack = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
fun Homepage(modifier: Modifier, todoViewModel: ToDoListViewModel, onNavigateToAdd: () -> Unit) {
    val todos by todoViewModel.todos.collectAsState()
    val isLoading by todoViewModel.isLoading.collectAsState()
    val error by todoViewModel.error.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = onNavigateToAdd,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Yeni Görev Ekle")
        }

        when {
            isLoading -> { Text("Yükleniyor") }
            error != null -> { Text("Hata aldı: $error") }
            else -> {
                ToDoList(todos, onDelete = { id -> todoViewModel.delete(id) })
            }
        }
    }
}

@Composable
fun AddTodoScreen(modifier: Modifier, todoViewModel: ToDoListViewModel, onBack: () -> Unit) {
    val text = remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Yeni Görev", modifier = Modifier.padding(bottom = 16.dp))
        TextField(
            value = text.value,
            onValueChange = { newValue -> text.value = newValue },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Görev Başlığı") }
        )
        Button(
            onClick = {
                if (text.value.isNotBlank()) {
                    todoViewModel.addTodo(text.value) {
                        onBack()
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Ekle")
        }
    }
}





@Composable
fun AddToDo(onAdd: (String) -> Unit) {
    val text = remember { mutableStateOf("abc") }

    Column {
        Text("Todo List")
        TextField(
            value=text.value, // Statik olduğu sürece ekranda güncellenmez.
            onValueChange = { newValue -> text.value = newValue }
        )
        Button(
            onClick = {
                onAdd(text.value)
                text.value = ""
            }
        ) {
            Text("Tıkla")
        }
    }
}
// State aynı
@Composable
fun ToDoList(toDoList: List<Todo>, onDelete: (Int) -> Unit) {

    LazyColumn(modifier = Modifier.fillMaxSize())
    {
        itemsIndexed(toDoList) {
                index,todo ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(todo.id.toString())
                Text(todo.title)
                IconButton(onClick = {
                    onDelete(todo.id)
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Sil")
                }
            }
        }
    }
}