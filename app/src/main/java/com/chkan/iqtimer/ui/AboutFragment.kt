package com.chkan.iqtimer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.chkan.iqtimer.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedbackImage = view.findViewById<ImageView>(R.id.iv_feedback)
        val feedbackText = view.findViewById<TextView>(R.id.tv_feedback)
        val sendMail = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_mail))
                data = Uri.parse("mailto: chkan.dv@gmail.com")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)}
            startActivity(intent)
        }
        feedbackImage.setOnClickListener {
            sendMail.invoke()
        }
        feedbackText.setOnClickListener {
            sendMail.invoke()
        }

    }

}