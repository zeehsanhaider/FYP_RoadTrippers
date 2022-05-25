package com.example.letsgoadmin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.letsgoadmin.ui.activity.MainActivity
import com.example.letsgoadmin.R
import com.example.letsgoadmin.databinding.FragmentSplashBinding
import com.example.letsgoadmin.ui.activity.HomeActivity
import com.example.letsgoadmin.util.animation.AnimationUtils
import com.example.letsgoadmin.util.navigation.NavigationUtils
import com.example.letsgoadmin.util.preference.RememberMePreference
import org.koin.android.ext.android.inject

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val rememberMePreference: RememberMePreference by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        AnimationUtils.scaleAnim(binding.cardLogo, 0f, 0f, 1f, 1f, 500L)
        // Implement remember me functionality here....
        if (rememberMePreference.isRememberMeChecked() && rememberMePreference.isCurrUserCredentialsSaved()) {
            // Go to home
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                startActivity(
                    Intent(requireActivity() as MainActivity, HomeActivity::class.java)
                )
                (requireActivity() as MainActivity).finish()
            }, NavigationUtils.SCREEN_DELAY)
        } else {
            // Go to login
            NavigationUtils.navigateForwardAfterDelay(
                binding.root,
                R.id.action_splashFragment_to_loginFragment
            )
        }
    }

    companion object {
        private const val TAG = "SplashFragment"
    }
}