package com.example.ewing20.map.hotspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ewing20.R
import com.example.ewing20.databinding.ActivityHotspotBinding
import com.example.ewing20.map.hotspot.hotspotAdapter.HotspotAdapter
import com.example.ewing20.map.hotspot.hotspotModel.HotspotModel
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class HotspotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotspotBinding
    private lateinit var adapter: HotspotAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instance of forecast list using the data model class.
        val hotspotList: ArrayList<HotspotModel> = ArrayList()

        try {
            // As we have JSON Object, so we are getting the object
            // Here we are calling a method which is returning the JSONObject
            val obj = JSONObject(getJSONFromAssets()!!)

            // Fetch JSON Array specific forecasts by using getJSONArray
            val hotspotArray = obj.getJSONArray("Hotspots")

            // Get the forecasts data using for loop
            for (i in 0 until hotspotArray.length()) {

                // Create a JSONObject for fetching single forecast's data
                val hotspots = hotspotArray.getJSONObject(i)

                // Fetch date store it in variable
                val hId = hotspots.getString("id")
                val hCountry = hotspots.getString("country")
                val hCountryCode = hotspots.getString("countryCode")
                val hLatitude = hotspots.getString("latitude")
                val hLongitude = hotspots.getString("longitude")
                val hLocation = hotspots.getString("location")
                val hCount = hotspots.getString("count")

                // Now add all the variables to the data model class and the data model class to the array list.
                val hotspotDetails = HotspotModel(
                    hId,
                    hCountry,
                    hCountryCode,
                    hLatitude,
                    hLongitude,
                    hLocation,
                    hCount)

                // Add the details in the list
                hotspotList.add(hotspotDetails)
            }
        } catch (e: JSONException) {
            // Exception
            e.printStackTrace()
        }

        adapter = HotspotAdapter(hotspotList)

        // Set the layoutManager that this RecyclerView will use.
        binding.hotspotList.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        binding.hotspotList.adapter = adapter
    }

    /**
     * Method to Load the JSON from the Assets file and return the object
     */
    private fun getJSONFromAssets(): String? {
        val json: String?
        val charset: Charset = Charsets.UTF_8
        try {
            val myHotspotJSONFile = assets.open("Hotspot.JSON")
            val size = myHotspotJSONFile.available()
            val buffer = ByteArray(size)
            myHotspotJSONFile.read(buffer)
            myHotspotJSONFile.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}