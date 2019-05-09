package hu.ait.weatherreport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import hu.ait.weatherreport.data.WeatherResult
import kotlinx.android.synthetic.main.activity_weather_details_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherDetailsActivity : AppCompatActivity() {

    private val HOST_URL = "https://api.openweathermap.org/"
    private val UNITS = "metric"
    private val API_KEY = "46264d99e454403ba639c6daab5241fc"

    lateinit var weatherCall: Call<WeatherResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details_activity)

        val retrofit = Retrofit.Builder()
            .baseUrl(HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPI = retrofit.create(WeatherAPI::class.java)

        weatherCall = weatherAPI.getRates(intent.getStringExtra(ScrollingActivity.KEY_CITY), UNITS, API_KEY)

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                tvName.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                tvName.text = weatherResult?.name
                tvDescription.text = weatherResult?.weather?.get(0)?.description.toString()
                tvCurrentTemp.text = String.format("%s°C", weatherResult?.main?.temp.toString())
                tvTempRange.text = String.format("%s°C | %s°C", weatherResult?.main?.temp_max.toString(), weatherResult?.main?.temp_min.toString())
                tvHumidity.text = String.format("Humidity: %s", weatherResult?.main?.humidity.toString())
                tvPressure.text = String.format("Pressure: %s", weatherResult?.main?.pressure.toString())
                tvWind.text = String.format("Wind: %s", weatherResult?.wind?.speed.toString())
                tvSunrise.text = String.format("Sunrise: %s", weatherResult?.sys?.sunrise.toString())
                tvSunset.text = String.format("Sunset: %s", weatherResult?.sys?.sunset.toString())
                Glide.with(this@WeatherDetailsActivity)
                    .load(
                        ("https://openweathermap.org/img/w/" +
                                response.body()?.weather?.get(0)?.icon
                                + ".png"))
                    .into(ivIcon)

            }
        })


    }
}
