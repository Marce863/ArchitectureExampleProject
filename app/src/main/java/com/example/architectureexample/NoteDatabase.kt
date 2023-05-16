package com.example.architectureexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
Class for the Room database that'll connect all the components inside of Room

We define what entities we want this database to contain.
Version number is for when we make changes to database we increment version
and provide a migration strategy.
 */
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    // To access our DAO, since it's abstract our room database builder will implement the logic
    abstract fun noteDao(): NoteDao

    companion object {

        // Turn the class into a singleton
        private var INSTANCE: NoteDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context, scope: CoroutineScope): NoteDatabase {
            if (INSTANCE == null) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().addCallback(InitializeDbCallBack(scope)).build()

                INSTANCE = instance
            }

            return INSTANCE!!
        }

        /*
        If we want to initialize the DB we need to do it on create as it's the only time we'll call
        the database, every other time the DB is already created.
         */
        private class InitializeDbCallBack(val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { roomDb ->
                    // Every coroutine needs a scope that'll manage its lifecycle
                    scope.launch {
                        populateDb(roomDb)
                    }
                }
            }

            suspend fun populateDb(db: NoteDatabase) {
                val noteDao: NoteDao = db.noteDao()
                noteDao.insert(Note("Title 1", "Description 1", 1))
                noteDao.insert(Note("Title 2", "Description 2", 2))
                noteDao.insert(Note("Title 3", "Description 3", 3))
            }
        }
    }
}