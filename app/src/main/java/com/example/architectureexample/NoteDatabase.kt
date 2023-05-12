package com.example.architectureexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
Class for the Room database that'll connect all the components inside of Room

We define what entities we want this database to contain.
Version number is for when we make changes to database we increment version
and provide a migration strategy.
 */
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    // To access our DAO
    abstract fun noteDao(): NoteDao

    companion object {

        // Turn the class into a singleton
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().build()
            }

            return instance!!
        }
    }
}