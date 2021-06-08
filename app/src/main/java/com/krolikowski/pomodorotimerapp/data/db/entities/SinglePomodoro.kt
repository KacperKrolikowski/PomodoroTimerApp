package com.krolikowski.pomodorotimerapp.data.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pomodoros")
data class SinglePomodoro(
    @ColumnInfo(name = "pomodoro_name", defaultValue = "New Pomodoro")
    var name: String,
    @ColumnInfo(name = "pomodoro_work_time")
    var taskTime: Int,
    @ColumnInfo(name = "pomodoro_work_amount")
    var workAmount: Int,
    @ColumnInfo(name = "pomodoro_short_break_time")
    var shortBreakTime: Int,
    @ColumnInfo(name = "pomodoro_short_break_amount")
    var shortBreakAmount: Int,
    @ColumnInfo(name = "pomodoro_long_break_time")
    var longBreakTime: Int,
    @ColumnInfo(name = "pomodoro_long_break_often")
    var longBreakOften: Int,
): Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
