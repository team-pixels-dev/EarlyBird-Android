package com.suhwan.earlybird_test.ui.timer

interface TimerListener {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}