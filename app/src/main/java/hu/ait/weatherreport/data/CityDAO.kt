package hu.ait.weatherreport.data

import android.arch.persistence.room.*


@Dao
interface ShoppingItemDAO {
    @Query("SELECT * FROM cities")
    fun getAllCities(): List<City>

    @Insert
    fun insertCity(city: City) : Long

    @Delete
    fun deleteCity(city: City)

    @Update
    fun updateCity(city: City)

}