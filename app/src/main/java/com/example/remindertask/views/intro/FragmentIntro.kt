package com.example.remindertask.views.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.remindertask.databinding.FragmentIntroBinding
import com.example.remindertask.viewmodel.IntroViewModel

class FragmentIntro : Fragment() {
    private lateinit var pageChangeCallback: OnPageChangeCallback
    private lateinit var binding: FragmentIntroBinding
    private lateinit var introViewModel: IntroViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        introViewModel = ViewModelProvider(this)[IntroViewModel::class.java]
        binding = FragmentIntroBinding.inflate(layoutInflater)
        val vp = binding.viewPage
        val dotIndicator = binding.dotsIndicator
        val finBtn = binding.finishBtn
        val nextBtn = binding.nextBtn

        vp.adapter = ViewPageAdapter(this.requireActivity())
        dotIndicator.attachTo(vp)

        pageChangeCallback = object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    finBtn.text = "Finish"
                    nextBtn.visibility = View.GONE
                    return
                }
                finBtn.text = "Skip"
                nextBtn.visibility = View.VISIBLE
            }
        }
        vp.registerOnPageChangeCallback(pageChangeCallback)

        nextBtn.setOnClickListener {
            vp.currentItem += 1
        }

        finBtn.setOnClickListener {
            introViewModel.finishIntro()
            this.findNavController().navigate(FragmentIntroDirections.introToMain())
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPage.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}