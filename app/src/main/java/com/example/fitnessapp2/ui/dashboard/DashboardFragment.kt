package com.example.fitnessapp2.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp2.SensorModel
import com.example.fitnessapp2.SharedViewModel
import com.example.fitnessapp2.databinding.FragmentDashboardBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        context?.let {
            SensorModel.initializeSensorManager(it)
        }

        val sensorModel = sharedViewModel.sensorModel

        Log.e("steps","onViewCreate")
        sensorModel.setRunningState(true)

        //observables here
        sensorModel.newStepsMeasurement.observe(viewLifecycleOwner){
            if (it != null){
                binding.textStepsIncrement.text = "" + it.value
                binding.textStartTime.text = it.startDate!!.time.toString()
                binding.textEndTime.text = it.endDate!!.time.toString()
            }
        }
        sensorModel.totalValue.observe(viewLifecycleOwner){
            if(it != null){

                binding.textStepsTotal.text = "" + it.toString()
            }
        }

        sensorModel.heartRate.observe(viewLifecycleOwner){
            if(it != null){
                binding.HR.text = "" + it.toString()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}