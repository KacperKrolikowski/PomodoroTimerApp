package com.krolikowski.pomodorotimerapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.krolikowski.pomodorotimerapp.data.db.entities.SinglePomodoro

@Dao
interface SinglePomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoro(pomodoro: SinglePomodoro)

    @Delete
    suspend fun deletePomodoro(pomodoro: SinglePomodoro)

    @Query("SELECT * FROM pomodoros")
    fun getAllPomodoros(): LiveData<List<SinglePomodoro>>

}