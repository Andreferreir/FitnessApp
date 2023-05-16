package com.example.fitnessapp2

import androidx.lifecycle.ViewModel
import com.example.fitnessapp2.Database.MyDAO

class SharedViewModel: ViewModel() {
    private lateinit var myDAO: MyDAO

    public val sensorModel = SensorModel()

    public fun setMyDAO(myDAO: MyDAO) {
        this.myDAO = myDAO
    }

}