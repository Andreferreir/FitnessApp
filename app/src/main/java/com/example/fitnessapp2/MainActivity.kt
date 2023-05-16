package com.example.fitnessapp2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnessapp2.Database.MeasurementType
import com.example.fitnessapp2.Database.MyDatabase
import com.example.fitnessapp2.databinding.ActivityMainBinding

/*import com.example.fitnessapp2.Database.MeasurementType
import com.example.fitnessapp2.Database.MyDatabase
import com.example.fitnessapp2.Database.MyDatabase.MyDatabase.Companion.build
import com.example.fitnessapp2.Database.MyDatabase.MyDatabase.Companion.stepsType
import com.example.fitnessapp2.databinding.ActivityMainBinding
*/
class MainActivity : AppCompatActivity() {

    companion object {
        public var permissionsGranted = false

    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile,
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        obtainPermissions()
        verifyMeasurementTypes()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun verifyMeasurementTypes() {
        val myDao = MyDatabase.build(applicationContext).DAO()

        var stepsType = myDao.getMeasurementType(MyDatabase.stepsType)
        if (stepsType == null) {
            myDao.insertMeasurementType( MeasurementType(name = MyDatabase.stepsType, units = "Steps" ))
        }
        var distanceType = myDao.getMeasurementType(MyDatabase.distanceType)
        if (distanceType == null) {
            myDao.insertMeasurementType( MeasurementType(name = MyDatabase.distanceType , units = "km"))
        }
        var caloriesType = myDao.getMeasurementType(MyDatabase.caloriesType)
        if (caloriesType == null) {
            myDao.insertMeasurementType( MeasurementType(name = MyDatabase.caloriesType,  units = "kcal" ))
        }

        val fitnessTypes = myDao.getallMeasurementTypes()
        fitnessTypes.forEach{
            Log.e("fitness", "id: ${it.id}, name: ${it.name}, units: ${it.units}")
        }
    }


    private fun checkPermissionItem(permission: String): Boolean {
        if (ActivityCompat.checkSelfPermission(this, permission ) != PackageManager.PERMISSION_GRANTED)
            return false
        return true
    }

    private fun obtainPermissions() {
        var granted = true
        val permissions = arrayOf(  //ARRAY WITH ALL THE NECESSARY PERMISSIONS
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            //....
        )
        for(i in  permissions.indices){
            granted = granted && checkPermissionItem(permissions[i])
        }
        if (  !granted        ) {
            ActivityCompat.requestPermissions(this, permissions,0)
            return
        } else {
            permissionsGranted = true
            SensorModel.initializeSensorManager(applicationContext)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<out String>,  grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permission_granted = true
        for (i in 0 until permissions.size) {
            val grantResult = grantResults[i]
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                permission_granted = false
            }
        }
        if( permission_granted) {
            permissionsGranted = true
            SensorModel.initializeSensorManager(applicationContext)
        }
    }


}