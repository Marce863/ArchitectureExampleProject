package com.example.architectureexample

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

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
                ).fallbackToDestructiveMigration().addCallback(roomCallback).build()
            }

            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance!!).execute()
            }
        }

        /*
        Initialize the Database with some information
         */
        private class PopulateDbAsyncTask(db: NoteDatabase) : AsyncTask<Void, Void, Void>() {
            private val noteDao: NoteDao = db.noteDao()
            override fun doInBackground(vararg voids: Void?): Void? {
                noteDao.insert(Note("Title 1", "Description 1", 1))
                noteDao.insert(Note("Title 2", "Description 2", 2))
                noteDao.insert(Note("Title 3", "Description 3", 3))
                return null
            }
        }
    }
}