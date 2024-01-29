package com.example.remindertask.presentation.screens.intro.slide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.remindertask.R
import com.example.remindertask.databinding.FragmentSlideBinding

private val ARG_PARAM1: String = ""

class SlideShow : Fragment() {
    private var param1: Int? = null
    private lateinit var binding: FragmentSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlideBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = binding.text
        val description = binding.description
        val image = binding.introIcon
        when (param1) {
            0 -> {
                text.text = "Remind Task"
                description.text = "Reminding task app it just a normal reminder"
                image.setImageResource(R.drawable.idea_bulb)
            }

            1 -> {
                text.text = "Alert"
                description.text =
                    "We will alert you for what task don't forget to turn on the notifications"
                image.setImageResource(R.drawable.notification)
            }

            2 -> {
                text.text = "Track"
                description.text =
                    "We let you check what you have complete and ignore what you not. Feel free to be impress and guilt for your progress"
                image.setImageResource(R.drawable.sticky_notes)
            }

            else -> {
                text.text = "Ooops"
                description.text = "Something went wrong please restart the apps"
                image.setImageResource(R.drawable.oops)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Slide.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            SlideShow().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}