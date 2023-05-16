package com.example.fitnessapp2.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface MyDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMeasurementType(type: MeasurementType?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMeasurement(type: Measurement?)

    @Query("select * from measurementtype where name = :name")
    fun getMeasurementType(name: String): MeasurementType?

    @Query("select * from measurementtype")
    fun getallMeasurementTypes(): List<MeasurementType>


    @Query("select * from measurement where typeId = :type and startDate >= :beginDate and startDate <= :endDate")
    fun getMeasurements(type: Long, beginDate: Date, endDate: Date): List<Measurement>

}
