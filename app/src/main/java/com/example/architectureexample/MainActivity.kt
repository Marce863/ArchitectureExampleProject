package com.example.architectureexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architectureexample.ui.theme.ArchitectureExampleTheme

class MainActivity : ComponentActivity() {
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // To use a recycler view you must provide an adapter and a layout manager
        // RecyclerView is our main_activity.xml
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // If you know the RecyclerView size wont change to make it more efficient
        recyclerView.setHasFixedSize(true)

        val adapter: NoteAdapter = NoteAdapter()
        recyclerView.adapter = adapter

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]

        // When we have a change in our live data (data in our corresponding notes table
        // changes). Our adapter will be updated with our new list of notes and refresh
        // it's statues
        noteViewModel.getAllNotes().observe(
            this,
            Observer { list ->
                list?.let {
                    // Update RecyclerView
                    adapter.notes = list
                }
            })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArchitectureExampleTheme {
        Greeting("Android")
    }
}