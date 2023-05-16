package com.example.architectureexample

import androidx.lifecycle.LiveData

/*
MVVM Architecture
The repository decides when to fetch data from where (data base, online),
and with what API calls. Provides a clean API for the rest of the app

Application is used because later in our view model we'll get passed in application
and since application is subclass of context we can use it as our context to make
our database instance
 */
class NoteRepository(private val noteDao : NoteDao) {

    /*
    Room automatically executes the database operations that return live
    data on the background thread, so we don't take care of this.

    For our other database operations we have to execute the code on the
    background thread ourselves. Room doesn't allow database operations on the
    main thread, since it'll crash the app.

    We will use Coroutines to resolve this

    These are the APIs that repository exposes to the outside
     */

    suspend fun insert(note : Note) = noteDao.insert(note)

    suspend fun update(note : Note) = noteDao.update(note)

    suspend fun delete(note : Note) = noteDao.delete(note)

    fun deleteAllNotes() = noteDao.deleteAllNotes()

    fun getAllNotes() : LiveData<List<Note>> = noteDao.getAllNotes()
}