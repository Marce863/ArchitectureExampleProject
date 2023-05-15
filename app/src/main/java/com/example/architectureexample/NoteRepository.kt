package com.example.architectureexample

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Delete

/*
MVVM Architecture
The repository decides when to fetch data from where (data base, online),
and with what API calls. Provides a clean API for the rest of the app

Application is used because later in our view model we'll get passed in application
and since application is subclass of context we can use it as our context to make
our database instance
 */
class NoteRepository(application: Application) {
    private var noteDao : NoteDao
    private var allNotes : LiveData<List<Note>>

    init {
        val database : NoteDatabase = NoteDatabase.getInstance(application)

        // Abstract method from NoteDatabase
        // Since we used Room builder it auto generates the code for this method
        noteDao = database.noteDao()

        // Room auto generates the code for this as well
        allNotes = noteDao.getAllNotes()
    }


    /*
    Room automatically executes the database operations that return live
    data on the background thread, so we don't take care of this.

    For our other database operations we have to execute the code on the
    background thread ourselves. Room doesn't allow database operations on the
    main thread, since it'll crash the app.

    We will use Async task to resolve this

    These are the APIs that repository exposes to the outside
     */
    fun insert(note : Note) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note : Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao).execute()
    }

    // We don't need this because Kotlin provides getters and setters
    fun getAllNotes() : LiveData<List<Note>> {
        return allNotes
    }

    // Inner class are static by default
    // Make an asyn task to run in the background
    // Three Params, Progress, Result
    class InsertNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        private var noteDao : NoteDao = noteDao

        @Override
        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.insert(it) }
            return null
        }
    }

    class UpdateNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        private var noteDao : NoteDao = noteDao

        @Override
        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.update(it) }
            return null
        }
    }

    class DeleteNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        private var noteDao : NoteDao = noteDao

        @Override
        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.delete(it) }
            return null
        }
    }

    class DeleteAllNotesAsyncTask(noteDao: NoteDao) : AsyncTask<Void, Void, Void>() {
        private var noteDao : NoteDao = noteDao

        @Override
        override fun doInBackground(vararg void: Void?): Void? {
            noteDao.deleteAllNotes()
            return null
        }
    }
}