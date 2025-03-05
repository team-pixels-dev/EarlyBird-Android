package com.suhwan.earlybird_test.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val todo: String,
    val hour: Int,
    val minute: Int,
    val pa: String,
    val sound: Boolean,
    val vibration: Boolean
)
