package com.suhwan.earlybird_test.ui.nps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.FragmentNpsBinding
import com.suhwan.earlybird_test.db.ClientManager
import com.suhwan.earlybird_test.db.http.RetrofitClient
import com.suhwan.earlybird_test.db.http.model.NpsScoreRequest
import com.suhwan.earlybird_test.db.http.model.VisitRequest
import com.suhwan.earlybird_test.ui.add.AddAlarmActivity
import com.suhwan.earlybird_test.ui.timer.TimerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NpsDialogFragment : DialogFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentNpsBinding
    private var selectedButton: RadioButton? = null
    private var selectedScore: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNpsBinding.inflate(inflater)
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
                binding.submitButton.isClickable = true

                selectedScore = score
            }
        }
        binding.submitButton.setOnClickListener {
            sendScoreEvent()
            dismiss()
            parentFragmentManager.popBackStack()
        }
    }
    private fun sendScoreEvent(){
        val uuid = ClientManager.getOrCreateUUID(requireContext())
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = localDateTime.format(formatter)

        val client = NpsScoreRequest(selectedScore, uuid, formatted, 1)
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
            NpsDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}