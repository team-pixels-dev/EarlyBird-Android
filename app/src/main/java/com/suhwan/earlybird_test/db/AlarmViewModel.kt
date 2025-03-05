package com.suhwan.earlybird_test.db

class AlarmViewModel(private val alarmDao: AlarmDao) {
    val allAlarms = alarmDao.getAllAlarm()

    fun addAlarm(alarm:Alarm){

    }
}