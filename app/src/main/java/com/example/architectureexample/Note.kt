package com.example.architectureexample

import androidx.room.Entity
import androidx.room.PrimaryKey

/******** APPARENTLY THIS SHOULD BE A DATA CLASS ********/

/*
We call this Note because it represents one note object and a database
table that'll contain all of these notes

Room annotation, at compile time it'll create all the code to create
an SQLite table for this object
Each SQLite table needs a primary key to uniquely identify each entry
 */
@Entity(tableName = "note_table")
class Note(
    val title: String,
    val description: String,
    val priority: Int,
    @PrimaryKey(autoGenerate = false) var id: Int? = null
)
