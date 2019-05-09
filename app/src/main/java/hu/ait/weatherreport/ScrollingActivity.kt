package hu.ait.weatherreport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import hu.ait.weatherreport.adapter.CityAdapter
import hu.ait.weatherreport.data.AppDatabase
import hu.ait.weatherreport.data.City
import hu.ait.weatherreport.touch.CityTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), CityDialog.CityHandler  {

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
        val KEY_CITY = "KEY_CITY"
    }

    lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            showCitiesDialog()
        }

        initRecyclerViewFromDB()
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var listCities =
                AppDatabase.getInstance(this@ScrollingActivity).cityDao().getAllCities()

            runOnUiThread {
                // UI code here
                cityAdapter = CityAdapter(this, listCities)

                recyclerCityList.layoutManager = LinearLayoutManager(this)

                recyclerCityList.adapter = cityAdapter


                val callback = CityTouchCallback(cityAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerCityList)
            }

        }.start()
    }
    private fun showCitiesDialog() {
        CityDialog().show(supportFragmentManager, "TAG_ITEM_DIALOG")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun cityCreated(city: City) {
        Thread {
            var newId = AppDatabase.getInstance(this).cityDao().insertCity(city)

            city.itemId = newId

            runOnUiThread {
                cityAdapter.addItem(city)
            }
        }.start()
    }

}
