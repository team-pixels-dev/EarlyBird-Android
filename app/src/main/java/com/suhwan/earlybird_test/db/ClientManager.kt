package com.suhwan.earlybird_test.db

import android.content.Context
import java.util.UUID

object ClientManager {
    private const val PREFS_NAME = "earlyBird_prefs" //SharedPreferences 파일 이름
    private const val KEY_UUID = "client_uuid" //저장할 때 사용하는 키

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