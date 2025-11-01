package com.example.assignmentfiveq2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assignmentfiveq2.ui.theme.AssignmentFiveQ2Theme
import kotlin.random.Random

// Main
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentFiveQ2Theme {
                MyDailyHubApp()
            }
        }
    }
}


// Nav Route Definitions using a sealed class
sealed class AppScreen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Notes : AppScreen("notes_screen", "Notes", Icons.Default.Home)
    object Tasks : AppScreen("tasks_screen", "Tasks", Icons.Default.DateRange)
    object Calendar : AppScreen("calendar_screen", "Calendar", Icons.Default.List)
}

// ViewModel for the Notes screen
class NotesViewModel : ViewModel() {

    val notes = mutableStateListOf<String>()

    var newNoteTxt by mutableStateOf("")
        private set




    fun onnewNoteTxtChanged(newText: String) {
        newNoteTxt = newText
    }

    fun addNote() {
        if (newNoteTxt.isNotBlank()) {
            notes.add(0, newNoteTxt) // add to the top of the list
            newNoteTxt = "" // clear the input field
        }
    }

    fun removeNote(note: String) {
        notes.remove(note)
    }
}

// Data model for a single task item
data class Task(val id: Int, val label: String, var isCompleted: Boolean = false)



// ViewModel for the Tasks screen
class TasksViewModel : ViewModel() {
    
    val tasks = mutableStateListOf<Task>()

    var newTaskTxt by mutableStateOf("")
        private set

    fun onNewTaskTextChanged(newText: String) {
        newTaskTxt = newText
    }

    fun addTask() {
        if (newTaskTxt.isNotBlank()) {
            // Use a random ID to ensure it's unique
            val newTask = Task(id = Random.nextInt(), label = newTaskTxt)
            tasks.add(0, newTask) // Add to the top of the list
            newTaskTxt = "" // Clear the input field
        }
    }


    fun onTaskCompletionChanged(task: Task, completed: Boolean) {
        val taskIdx = tasks.indexOfFirst { it.id == task.id }
        if (taskIdx != -1) {
            tasks[taskIdx] = tasks[taskIdx].copy(isCompleted = completed)
        }
    }


    fun removeTask(task: Task) {
        tasks.remove(task)
    }
}


// --- Composables ---

@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("My Notes", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Input field and button to add a new note
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.newNoteTxt,
                onValueChange = { viewModel.onnewNoteTxtChanged(it) },
                label = { Text("Add a new note") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { viewModel.addNote() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // List of notes
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(viewModel.notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = note,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { viewModel.removeNote(note) }) {
                            Icon(Icons.Default.Close, contentDescription = "Remove Note")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TasksScreen(viewModel: TasksViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("My Tasks", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Input field and button to add a new task
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.newTaskTxt,
                onValueChange = { viewModel.onNewTaskTextChanged(it) },
                label = { Text("Add a new task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { viewModel.addTask() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // List of tasks
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(viewModel.tasks, key = { it.id }) { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { isChecked ->
                            viewModel.onTaskCompletionChanged(task, isChecked)
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = task.label,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = { viewModel.removeTask(task) }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove Task")
                    }
                }
            }
        }
    }
}


@Composable
fun CalendarScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Calendar",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}


// Main App Composable with Navigation
@Composable
fun MyDailyHubApp() {
    val navController = rememberNavController()

    // List of screens for the bottom navigation bar
    val bottomNavItems = listOf(
        AppScreen.Notes,
        AppScreen.Tasks,
        AppScreen.Calendar
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination to avoid building a deep back stack.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true // Saves UI state
                                }
                                // Avoid creating a new screen instance on re-selection
                                launchSingleTop = true
                                // Restore state when re-navigating
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppScreen.Notes.route,
            modifier = Modifier.padding(innerPadding),
            // Animate screen transitions
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(AppScreen.Notes.route) { NotesScreen() }
            composable(AppScreen.Tasks.route) { TasksScreen() }
            composable(AppScreen.Calendar.route) { CalendarScreen() }
        }
    }
}
