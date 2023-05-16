package com.example.architectureexample

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/*
DAO have to be interface or abstract classes because we don't provide the
method body. Room will automatically generate all the code files.

@Dao lets room know this is a DAO. It's good to have one DAO per entity.
 */
@Dao
interface NoteDao {

    // Database operation
    @Insert
    suspend fun insert(note : Note)

    @Update
    suspend fun update(note : Note)

    @Delete
    suspend fun delete(note : Note)

    /*
    Delete all notes at once

    Since we don't already have an annotation here we use @Query
    We define the database operation as a string

    If we don't write anything between DELETE and FROM it means delete all
     */
    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    /*
    Returns all the notes so we can put them in our recycler view

    * means all columns

    At compile it'll check if note_table columns are in our Note object

    With LiveData any time there's a change to note_table, this value (return)
    will automatically be updated and our activity will be notified. Room takes
    cares of all necessary stuff to update the LiveData object, just declare
     */
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes() : LiveData<List<Note>>
}