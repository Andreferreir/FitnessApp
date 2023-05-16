package com.example.fitnessapp2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(exportSchema = false, version = 1,
    entities = [(MeasurementType::class), (Measurement::class)])

@TypeConverters(TimeConverters::class)
public abstract class MyDatabase : RoomDatabase() {

    abstract fun DAO(): MyDAO

    companion object {
        // TODO
        val stepsType                      = "Steps"
        val distanceType                = "Distance"
        val caloriesType                 = "Calories"
        var database: MyDatabase? = null
        public fun build(context: Context): MyDatabase {
            if (database == null) {
                database = Room.databaseBuilder(context,MyDatabase::class.java, "mydb")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return database!!
        }
    }
}
