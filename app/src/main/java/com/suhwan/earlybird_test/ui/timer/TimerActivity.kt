package com.suhwan.earlybird_test.ui.timer
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityTimerBinding
import com.suhwan.earlybird_test.db.ClientManager
import com.suhwan.earlybird_test.db.http.RetrofitClient
import com.suhwan.earlybird_test.db.http.model.ClickRequest
import com.suhwan.earlybird_test.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            if(binding.btnStart.text == getString(R.string.timer_btn_start)){
                checkPermission()
                sendClickEvent()
                isRunning = true
            }else{
                isRunning = false
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {  // 오버레이 권한 체크
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                val serviceIntent = Intent(this, TimerService::class.java)
                startService(serviceIntent)
            }
        } else {
            val serviceIntent = Intent(this, TimerService::class.java)
            startService(serviceIntent)
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리 (예: 사용자에게 다시 요청)
            } else {
                val serviceIntent = Intent(this, TimerService::class.java)
                startService(serviceIntent)
            }
        }
    }
    private fun FullScreenMode(){
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
            ViewCompat.onApplyWindowInsets(view, windowInsets)
        }
    }
    override fun onBackPressed() {
        if(isRunning == false){
            super.onBackPressed()
        }
    }
    private fun sendClickEvent(){
        val uuid = ClientManager.getOrCreateUUID(this)
        val clickType = "timer-start-button-click"
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = localDateTime.format(formatter)

        val client = ClickRequest(uuid, clickType, formatted)
        RetrofitClient.clickInstance.clickRequest(client).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("click-event", uuid)
                    Log.d("click-event", clickType)
                    Log.d("click-event", formatted)
                }
                else{
                    val error = response.errorBody()
                    Log.d("click-event", "error body : $error")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("click-event", t.message.toString())
            }
        })
    }
}