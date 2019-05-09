package hu.ait.weatherreport

import hu.ait.weatherreport.data.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun getRates(@Query("q") city: String, @Query("units") units: String, @Query("appid") app_id: String) : Call<WeatherResult>
}