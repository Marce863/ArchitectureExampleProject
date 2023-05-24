package com.example.architectureexample

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architectureexample.ui.theme.ArchitectureExampleTheme
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : ComponentActivity() {

    companion object {
        const val ADD_NOTE_REQUEST: Int = 1
    }

    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Add the Add Note Button
        val buttonAddNote: FloatingActionButton = findViewById(R.id.button_add_note)
        buttonAddNote.setOnClickListener {
            // Intent is a Java class so we do it this way instead AddNoteActivity.class
            val intent: Intent = Intent(this, AddNoteActivity::class.java)

            // Typical way of calling an intent is doing startActivity(intent) but we
            // are expecting some data back so we do
            // Request code is used to distinguish between request
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

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

        // Swipe to delete functionality
        // ItemTouchHelper gives our recycler view the ability to swipe
        // The values passed into the simple call back are for swipe directions allowed
        val itemTouchHelperCallBack = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            // This is for drag and drop, not needed for our use case
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false;
            }

            // On swipe delete the note at the position given by the view holder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val notePosition = viewHolder.adapterPosition
                noteViewModel.delete(adapter.getNoteAt(notePosition))
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        actionBar?.show()
    }

    // This is where we get the result back when the Activity we called with an intent
    // is closed.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO: Update this deprecated override
        super.onActivityResult(requestCode, resultCode, data)

        // This handles what request we're dealing with
        // Check if we're dealing with this Activities request and if result is ok
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title: String = data?.getStringExtra(AddNoteActivity.EXTRA_TITLE)!!
            val description: String = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)!!
            val priority: Int = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1)

            val note: Note = Note(title, description, priority)
            noteViewModel.insert(note)

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else {
            // This is for when the cancel button is pressed instead
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    // Need to override this to add the menu item we created to delete all notes
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    // When delete
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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