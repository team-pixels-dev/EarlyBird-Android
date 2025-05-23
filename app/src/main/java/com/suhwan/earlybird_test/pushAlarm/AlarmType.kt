package com.suhwan.earlybird_test.pushAlarm

enum class AlarmType(
    val requestCode: Int,
    val defaultHour: Int,
    val defaultMinute: Int,
    val pa: String,
    val vibration: Boolean
)
{
    MORNING(1001, 9, 0, "AM", true),
    NIGHT(1002, 9, 0, "PM", true),
    USER(1003, 7, 30, "AM", true); // 기본값, 실제 사용자가 바꿀 수 있음

    companion object {
        const val MORNING_REQUEST_CODE = 1001
        const val NIGHT_REQUEST_CODE = 1002
        const val USER_REQUEST_CODE = 1003
        fun fromRequestCode(code: Int): AlarmType? =
            entries.find { it.requestCode == code }
    }

}