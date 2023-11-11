package com.example.ewing20.map.hotspot.hotspotAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ewing20.databinding.ItemHotspotLayoutBinding
import com.example.ewing20.map.hotspot.hotspotModel.HotspotModel

class HotspotAdapter (private val dataSets: ArrayList<HotspotModel>):
    RecyclerView.Adapter<HotspotAdapter.ViewHolder>() {

    private lateinit var binding: ItemHotspotLayoutBinding

    /**
     * Inflates the item views which is designed in xml layout file.
     *
     * create a new
     * {@Link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemHotspotLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@Link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * Layout file.
     */

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {

        val dataSet = dataSets[position]

        //viewholder.id.text = dataSet.id
        //viewholder.country.text = dataSet.country
        //viewholder.countryCode.text = dataSet.countryCode
        viewholder.latitude.text = dataSet.latitude.toString()
        viewholder.longitude.text = dataSet.longitude.toString()
        //viewholder.location.text = dataSet.location
        //viewholder.count.text = dataSet.count
    }

    /**
     * Gets the number of items in the list.
     */

    override fun getItemCount() = dataSets.size

    /**
     * A ViewerHolder describes on item view and metadata about its place within the Recyclerview.
     */

    class ViewHolder(view: ItemHotspotLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        //val id: TextView
        //val country: TextView
        //val countryCode: TextView
        val latitude: TextView
        val longitude: TextView
        //val location: TextView
        //val count: TextView

        // Holds the TextView that will add each item to
        init {
            //id = view.id
            //country = view.country
            //countryCode = view.countryCode
            latitude = view.latitude
            longitude = view.longitude
            //location = view.location
            //count = view.count
        }
    }
}