package com.suhwan.earlybird_test.ui.timer

import android.os.CountDownTimer

object TwoMinutesTimer {
    private var timer: CountDownTimer? = null
    var listener: TimerListener ?= null
    fun start() {
        timer = object : CountDownTimer(1200, 1){
            override fun onTick(millisUntilFinished: Long) {
                listener?.onTick(millisUntilFinished)
            }
            override fun onFinish() {
                listener?.onFinish()
            }
        }.start()
    }
}