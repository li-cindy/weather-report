package hu.ait.weatherreport.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.ait.weatherreport.R
import hu.ait.weatherreport.ScrollingActivity
import hu.ait.weatherreport.WeatherDetailsActivity
import hu.ait.weatherreport.data.AppDatabase
import hu.ait.weatherreport.data.City
import hu.ait.weatherreport.touch.CityTouchHelperCallback
import kotlinx.android.synthetic.main.list_row.view.*
import java.util.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>, CityTouchHelperCallback {

    var cities = mutableListOf<City>()

    private val context: Context

    constructor(context: Context, listCities: List<City>) : super() {
        this.context = context
        cities.addAll(listCities)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        val itemRowView = LayoutInflater.from(context).inflate(
            R.layout.list_row, viewGroup, false
        )

        return ViewHolder(itemRowView)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(viewHolder: CityAdapter.ViewHolder, position: Int) {
        val city = cities[position]

        viewHolder.tvCityName.text = city.name


        viewHolder.itemView.setOnClickListener {

            var weatherIntent = Intent()
            weatherIntent.setClass(
                viewHolder.itemView.context,
                WeatherDetailsActivity::class.java
            )

            weatherIntent.putExtra(ScrollingActivity.KEY_CITY, viewHolder.tvCityName.text.toString())


            viewHolder.itemView.context.startActivity(weatherIntent)
        }

    }

    fun addItem(city: City) {
        cities.add(city)
        notifyDataSetChanged()
    }

    fun deleteCity(deletePosition: Int) {
        Thread {
            AppDatabase.getInstance(context).cityDao().deleteCity(
                cities[deletePosition]
            )

            (context as ScrollingActivity).runOnUiThread {
                cities.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

//
//    fun updateCity(city: City) {
//        Thread {
//            AppDatabase.getInstance(context).cityDao().updateCity(city)
//        }.start()
//    }
//
//    fun updateCity(city: City, editIndex: Int) {
//        cities[editIndex] = city
//        notifyItemChanged(editIndex)
//    }

    override fun onDismissed(position: Int) {
        deleteCity(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(cities, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCityName = itemView.tvCity
    }

}