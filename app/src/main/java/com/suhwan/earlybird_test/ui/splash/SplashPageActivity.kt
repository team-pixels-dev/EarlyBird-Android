package com.suhwan.earlybird_test.ui.splash
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.db.ClientManager
import com.suhwan.earlybird_test.db.http.RetrofitClient
import com.suhwan.earlybird_test.db.http.model.VisitRequest
import com.suhwan.earlybird_test.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sendVisitEvent()

        Handler(Looper.getMainLooper()).postDelayed({

            // 일정 시간이 지나면 MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // 이전 키를 눌렀을 때 스플래스 스크린 화면으로 이동을 방지하기 위해
            // 이동한 다음 사용안함으로 finish 처리
            finish()

        }, 2000) // 시간 2초 이후 실행

        setContentView(R.layout.activity_splash_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun sendVisitEvent(){
        val uuid = ClientManager.getOrCreateUUID(this)
        val client = VisitRequest(uuid)
        try {
            RetrofitClient.visitInstance.visitRequest(client).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        Log.d("visit-event", "success")
                    }
                    else{
                        Log.d("visit-event", "error : 실패")
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("visit-event", t.message.toString())
                }
            })
        }catch (e:Exception){
            Toast.makeText(this, "${e.message}",Toast.LENGTH_LONG).show()
        }

    }
}