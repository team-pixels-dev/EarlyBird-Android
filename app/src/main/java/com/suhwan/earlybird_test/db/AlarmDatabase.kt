package com.suhwan.earlybird_test.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun getAlarmDao(): AlarmDao
    companion object{
        val databaseName = "db_todo"
        var alarmDatabase : AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase{
            if(alarmDatabase == null){
                alarmDatabase = Room.databaseBuilder(
                    context,
                    AlarmDatabase::class.java,
                    databaseName
                ).fallbackToDestructiveMigration().build()
            }
            return alarmDatabase as AlarmDatabase
        }
    }
}

