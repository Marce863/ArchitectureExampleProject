package com.example.architectureexample

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
We call this Note because it represents one note object and a database
table that'll contain all of these notes

Room annotation, at compile time it'll create all the code to create
an SQLite table for this object
Each SQLite table needs a primary key to uniquely identify each entry
 */
@Entity(tableName = "note_table")
class Note(
    @PrimaryKey val id: Int, val title: String, val description: String, val priority: Int
)
