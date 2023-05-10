package com.example.architectureexample

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
We call this Note because it represents one note object and a database
table that'll contain all of these notes
 */

// Room annotation, at compile time it'll create all the code to create
// an SQLite table for this object
@Entity(tableName = "note_table")
class Note (var title: String, var description: String, var priority: Int){
    // Each SQLite table needs a primary key to uniquely identify each entry
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0;

    // Primary constructor parameters are available in property initializers
    init {
        // Create member variables for all the fields we want our note to contain
        // This will be the name of the columns
        var title: String

        var description: String

        var priority: Int
    }

    // Kotlin provides getters and setters
}