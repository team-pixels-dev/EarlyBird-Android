package com.suhwan.earlybird_test.db

import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

object ClientManager {
    private const val PREFS_NAME = "earlyBird_prefs" //SharedPreferences 파일 이름
    private const val KEY_UUID = "client_uuid" //저장할 때 사용하는 키

    fun getVisitDays(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val nowDate: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nowDateFormatted = nowDate.format(formatter)

        val lastVisitDate = sharedPreferences.getString("last_visit_date","")

        var visitDays = sharedPreferences.getInt("visit_days",0)

        if(lastVisitDate != nowDateFormatted){
            visitDays += 1
            sharedPreferences.edit().putString("last_visit_date", nowDateFormatted).apply()
            sharedPreferences.edit().putInt("visit_days", visitDays).apply()
            return visitDays
        }
        return -1
    }

    fun getOrCreateUUID(context: Context): String{
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var uuid = sharedPreferences.getString(KEY_UUID, null)

        if(uuid==null){
           uuid = UUID.randomUUID().toString()
           sharedPreferences.edit().putString(KEY_UUID,uuid).apply()
        }
        return uuid
    }
}