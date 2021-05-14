package com.somethingsimple.nasapod.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import com.somethingsimple.nasapod.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        animate()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }

    private fun animate() {
        val transition = Slide(Gravity.BOTTOM).setStartDelay(1000)
        TransitionManager.beginDelayedTransition(
            binding.splashLayoutContainer,
            transition
        )
        binding.nasaText.setVisibility(View.VISIBLE)
        binding.splashLogo.apply {
            animate().rotationBy(360f).setDuration(1000).start()
            animate().alpha(0.9f).setDuration(1000).start()
        }

    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}