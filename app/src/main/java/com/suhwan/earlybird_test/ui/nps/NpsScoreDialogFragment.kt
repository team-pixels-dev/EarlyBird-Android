package com.suhwan.earlybird_test.ui.nps

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.FragmentNpsScoreBinding
import com.suhwan.earlybird_test.db.ClientManager
import com.suhwan.earlybird_test.db.http.RetrofitClient
import com.suhwan.earlybird_test.db.http.model.NpsScoreRequest
import com.suhwan.earlybird_test.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class NpsScoreDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentNpsScoreBinding
    private var selectedButton: RadioButton? = null
    private var selectedScore: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNpsScoreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNpsClickListener()
    }

    private fun setupNpsClickListener(){
        val buttons = mapOf(
            binding.button1 to 0,
            binding.button2 to 1,
            binding.button3 to 2,
            binding.button4 to 3,
            binding.button5 to 4,
            binding.button6 to 5,
            binding.button7 to 6,
            binding.button8 to 7,
            binding.button9 to 8,
            binding.button10 to 9,
            binding.button11 to 10
        )
        buttons.forEach { (button, score) ->
            button.setOnClickListener {
                selectedButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                selectedButton?.setBackgroundResource(R.drawable.shape_button_nps_circle_unselected)
                selectedButton?.isChecked = false  // 이전 선택된 버튼 체크 해제
                button.isChecked = true  // 현재 선택한 버튼 체크
                selectedButton = button

                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                button.setBackgroundResource(R.drawable.shape_button_nps_circle_selected)
                binding.submitButton.setBackgroundResource(R.color.main_background)
                binding.submitButton.isEnabled = true

                selectedScore = score
            }
        }
        binding.submitButton.setOnClickListener {
            sendScoreEvent()

            val message = Message.obtain()
            message.what = 1
            MainActivity.handler?.sendMessage(message)

            dismiss()
            parentFragmentManager.popBackStack()
        }
    }
    private fun sendScoreEvent(){
        val uuid = ClientManager.getOrCreateUUID(requireContext())
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = localDateTime.format(formatter)
        val days = ClientManager.getVisitDays(requireContext())
        val client = NpsScoreRequest(selectedScore, uuid, formatted, days)
        RetrofitClient.npsScoreInstance.npsScoreRequest(client).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("nps-event", uuid)
                }
                else{
                    val error = response.errorBody()
                    Log.d("nps-event", "error body : $error")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("nps-event", t.message.toString())
            }
        })
    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NpsScoreDialogFragment().apply {

            }
    }
}