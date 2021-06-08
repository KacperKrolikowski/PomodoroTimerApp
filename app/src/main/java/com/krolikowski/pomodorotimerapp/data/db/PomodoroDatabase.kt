package com.krolikowski.pomodorotimerapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krolikowski.pomodorotimerapp.data.db.dao.SinglePomodoroDao
import com.krolikowski.pomodorotimerapp.data.db.entities.SinglePomodoro

@Database(
    entities = [SinglePomodoro::class],
    version = 1
)
abstract class PomodoroDatabase:RoomDatabase() {

    abstract fun getSinglePomodoroDao(): SinglePomodoroDao

    companion object{
        @Volatile
        private var instance: PomodoroDatabase? = null

        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PomodoroDatabase::class.java,
            "PomodoroDB.db"
        ).build()
    }
}