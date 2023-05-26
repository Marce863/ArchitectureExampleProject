package com.example.architectureexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast

/*
This Activity is supposed to hold the UI that displays
EditText: The title you want for this Note
EditText: The description you want for this Note
NumberPicker: The priority level for this Note

NOTE: It would be best practice to create another ViewModel to send our
data to the database. We wouldn't want to reuse the NoteViewModel we already
have since this Activity only inserts, it doesn't do any of the other operations.

Instead we're going to send the data from this Activity to the MainActivity and
let it handle it with NoteViewModel which has the insert operation.

This add note activity is acting as an input form and doesn't communication with
any other layers. To do this we will use the start activity for result method.
Allows us to start the activity from our main activity then do our logic and send
data back to our main activity when this one is closed.
We do this over intent extras, for intent extras we need a key and for
best practice we use constants string that uses the package name.
 */
class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        // Key for sending our title, description and priority over Intents
        const val EXTRA_ID: String = "com.exmaple.architectureexample.EXTRA_ID"
        const val EXTRA_TITLE: String = "com.example.architectureexample.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION: String = "com.example.architectureexample.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY: String = "com.example.architectureexample.EXTRA_PRIORITY"
    }

    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    lateinit var numberPickerPriority: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        numberPickerPriority = findViewById(R.id.number_picker_priority)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 666

        // In order to get the X in to top left corner
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close)

        // Our title now changes based on how we got to this Activity
        // The intent will only have an ID when we update
        val intent: Intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            numberPickerPriority.value = intent.getStringExtra(EXTRA_PRIORITY)?.toInt() ?: 1
            numberPickerPriority.value = intent.getIntExtra(EXTRA_PRIORITY, -1)
        } else {
            // This is the title for the ActionBar
            title = "Add Note"
        }
    }

    /*
    We want to confirm the input, when we click the save menu icon in the action bar.
    We get the icon there by override and adding our own menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val menuInflater: MenuInflater = menuInflater
        // This tells the system to use our add note menu, as the menu of this activity
        menuInflater.inflate(R.menu.add_note_menu, menu)

        return true
    }

    /*
    To handle click on our items we have to override this method.
    The **item** is the menu item that's clicked
    When our save button is clicked we want to save the note
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun saveNote() {
        val title: String = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority: Int = numberPickerPriority.value

        // Invalid state, don't save the Note and show a toast
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        // We want to send our information back to the MainActivity so it can
        // insert this Note into the database
        // We use an Intent to send data back to the Activity that started this one
        val data: Intent = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        // TODO: I need to figure out how to get ID so I can send it back to MainActivity
        val id: Int = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }

        // We indicate if the input was successful or not. If user hits back button
        // it wasn't successful but if user hits save button and everything is fine
        // than set result as ok, and pass our data intent. finish() closes activity
        // Use these two values in the MainActivity to see if everything worked
        setResult(RESULT_OK, data)
        finish()
    }
}