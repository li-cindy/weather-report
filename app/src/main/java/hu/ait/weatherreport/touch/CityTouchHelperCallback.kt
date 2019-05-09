package hu.ait.weatherreport.touch


interface CityTouchHelperCallback {
    fun onDismissed(position : Int)
    fun onItemMoved(fromPosition: Int, toPosition : Int)
}