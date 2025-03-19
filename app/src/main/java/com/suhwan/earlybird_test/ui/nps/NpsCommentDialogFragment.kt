package com.suhwan.earlybird_test.ui.nps

import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.FragmentNpsCommentDialogBinding
import com.suhwan.earlybird_test.db.ClientManager
import com.suhwan.earlybird_test.db.http.RetrofitClient
import com.suhwan.earlybird_test.db.http.model.NpsCommentRequest
import com.suhwan.earlybird_test.db.http.model.NpsScoreRequest
import com.suhwan.earlybird_test.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NpsCommentDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentNpsCommentDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNpsCommentDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edComment.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()){
                    binding.submitButton.setBackgroundResource(R.color.nps_button)
                }else{
                    binding.submitButton.setBackgroundResource(R.color.main_background)
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.submitButton.setOnClickListener {
            val comment = binding.edComment.text.toString()
            sendScoreEvent(comment)
            dismiss()
            parentFragmentManager.popBackStack()
        }
    }

    private fun sendScoreEvent(comment : String){
        val uuid = ClientManager.getOrCreateUUID(requireContext())
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = localDateTime.format(formatter)

        val client = NpsCommentRequest(comment, uuid, formatted)
        RetrofitClient.npsCommentInstance.npsCommentRequest(client).enqueue(object : Callback<Void> {
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NpsCommentDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}