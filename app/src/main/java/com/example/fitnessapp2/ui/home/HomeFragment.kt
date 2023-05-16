package com.example.fitnessapp2.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp2.*
import com.example.fitnessapp2.databinding.ActivityMainBinding
import com.example.fitnessapp2.databinding.FragmentHomeBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        context?.let {
            SensorModel.initializeSensorManager(it)
        }
        val circularProgressBar = binding.circularProgressBar

        val sensorModel = sharedViewModel.sensorModel
        sensorModel.sensorValue.observe(viewLifecycleOwner){
            if(it != null){
                circularProgressBar.apply {
                    // Set Progress
                    progress =  it
                    // or with animation
                    setProgressWithAnimation(progress, 2000) // =1s

                    // Set Progress Max
                    progressMax = 10000f

                    // Set ProgressBar Color
                    progressBarColor = Color.BLACK
                    // or with gradient
                    progressBarColorStart = Color.CYAN
                    progressBarColorEnd = Color.RED
                    progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

                    // Set background ProgressBar Color
                    backgroundProgressBarColor = Color.YELLOW
                    // or with gradient
                    backgroundProgressBarColorStart = Color.WHITE
                    backgroundProgressBarColorEnd = Color.RED
                    backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

                    // Set Width
                    progressBarWidth = 7f // in DP
                    backgroundProgressBarWidth = 3f // in DP

                    // Other
                    roundBorder = true
                    progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                }
                binding.textView6.text = "${it.toString()} / 10000"
            }
        }

        binding.button.setOnClickListener {  val card1 = Intent(activity, proximity::class.java)
            activity?.startActivity(card1) }
        val root: View = binding.root
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}