package com.example.fitnessapp2.Database

import androidx.room.*
import java.util.*


object TimeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}


// steps, distance, calories
@Entity
data class MeasurementType(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var units: String = "" // steps, kcal, metres
    // other data here
)

@Entity
data class Measurement(
    @PrimaryKey var id: Long = 0,
    var typeId: Long = 0, // steps, distance
    var value: Float = 0.0f,

    @TypeConverters(TimeConverters::class)
    var startDate: Date?,

    @TypeConverters(TimeConverters::class)
    var endDate: Date?,
    // metadata here
)

// specifying the relationship 1:N
data class MeasurementsPerType(
    @Embedded
    // 1
    val type: MeasurementType,
    @Relation(
        parentColumn = "id",
        entityColumn = "typeId"
    )
    // to N, because it uses a List
    val measurements: List<Measurement>
)


@Entity
data class Setting(
    @PrimaryKey var id: Long = 0,

    var dailySteps: Float = 0.0f,
    var dailyDistance: Float = 0.0f,
    var dailyCalories: Float = 0.0f,

    @TypeConverters(TimeConverters::class)
    var instalationDate: Date?,

    var active: Boolean = true
)
