@file:Suppress("DEPRECATION", "PrivatePropertyName")

package com.example.ewing20.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ewing20.R
import com.example.ewing20.databinding.ActivityMapsBinding
import com.example.ewing20.map.about.AboutActivity
import com.example.ewing20.map.bird.BirdActivity
import com.example.ewing20.map.profile.ProfileActivity
import com.example.ewing20.map.setting.SettingActivity
import com.example.ewing20.map.video.VideoActivity
import com.example.ewing20.map.web.WebActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap

    private val Richtersveld = LatLng(-28.4711057, 17.2169495)
    private val Solutions = LatLng(-26.0085957, 28.1264055)
    private val CradleMoon = LatLng(-25.9737221, 27.8354643)
    private val HickingTrails = LatLng(-32.9062431, 27.8688305)
    private val ADIWildlife = LatLng(-28.489279, 27.06084)
    private val AardvarkKloof = LatLng(-29.9380522, 18.4071314)
    private val AbeBailey = LatLng(-26.3097273, 27.3487473)
    private val AbelErasmus = LatLng(-24.4582639, 30.6111744)
    private val AddoEle = LatLng(-33.4909271, 25.7690709)
    private val AfricanDawn = LatLng(-33.9178294, 25.1114121)
    private val AfricanElegance = LatLng(-25.4461036, 27.0587254)
    private val AfricanPrideIrene = LatLng(-25.8754162, 28.2098578)
    private val Afrikaroo = LatLng(-31.9262, 23.3589)
    private val Agulhas = LatLng(-34.6518501, 19.9848175)
    private val AirportGameLodge = LatLng(-26.0723882, 28.2964776)
    private val AkkerenDam = LatLng(-31.4386523, 19.7719574)
    private val AlbasiniDam = LatLng(-23.107602, 30.1256704)
    private val AlbertFalls = LatLng(-29.4611333, 30.3756082)
    private val AlbertFarm = LatLng(-26.1557461, 27.9708481)
    private val AlexanderBay = LatLng(-28.6005, 16.4801)

    private var locationArrayList: ArrayList<LatLng>? = null

    private var mCurrentLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    private val defaultZoom = 15f
    private val REQUEST_LOCATION_PERMISSION = 1
    private val TAG = MapsActivity::class.java.simpleName

    private lateinit var mDistanceView: TextView
    private lateinit var mTimeView: TextView

    private var endLatitude = 0.0
    private var endlongitude = 0.0

    private var origin: MarkerOptions? = null
    private var destination: MarkerOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationArrayList = ArrayList()

        locationArrayList!!.add(Richtersveld)
        locationArrayList!!.add(Solutions)
        locationArrayList!!.add(CradleMoon)
        locationArrayList!!.add(HickingTrails)
        locationArrayList!!.add(ADIWildlife)
        locationArrayList!!.add(AardvarkKloof)
        locationArrayList!!.add(AbeBailey)
        locationArrayList!!.add(AbelErasmus)
        locationArrayList!!.add(AddoEle)
        locationArrayList!!.add(AfricanDawn)
        locationArrayList!!.add(AfricanElegance)
        locationArrayList!!.add(AfricanPrideIrene)
        locationArrayList!!.add(Afrikaroo)
        locationArrayList!!.add(Agulhas)
        locationArrayList!!.add(AirportGameLodge)
        locationArrayList!!.add(AkkerenDam)
        locationArrayList!!.add(AlbasiniDam)
        locationArrayList!!.add(AlbertFalls)
        locationArrayList!!.add(AlbertFarm)
        locationArrayList!!.add(AlexanderBay)

        mDistanceView = findViewById(R.id.distanceView)
        mTimeView = findViewById(R.id.timeView)

        binding.searchBtn.setOnClickListener {
            searchLocation()
        }

        binding.clearBtn.setOnClickListener {
            mapFragment.getMapAsync(this)
        }

        binding.birdsBtn.setOnClickListener {
            startActivity(Intent(this, BirdActivity::class.java))
        }

        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.settingBtn.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.zoomInBtn.setOnClickListener {
            zoomIn()
        }

        binding.zoomOutBtn.setOnClickListener {
            zoomOut()
        }

        binding.locationBtn.setOnClickListener {
            onLocationChanged()
        }

        binding.videoBtn.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }

        binding.webBtn.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java))
        }

        binding.aboutBtn.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("ObsoleteSdkInt")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Map of South Africa
        val mLatitude = -28.48322
        val mLongitude = 24.676997

        val latLng = LatLng(mLatitude, mLongitude)

        for (i in locationArrayList!!.indices) {
            mMap.addMarker(MarkerOptions()
                .position(locationArrayList!![i])
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5f))

        setMapStyle(mMap)
        enableMyLocation()

    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest .priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 3000
        mLocationRequest.smallestDisplacement = 10f
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(i: Int) {

    }

    private fun onLocationChanged() {
        //mLastLocation = location

        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker!!.remove()
        }

        // These coordinates represent the latitude and longitude of Varsity College Sandton
        val mLatitude = -26.0935824633
        val mLongitude = 28.0477972026

        // Place current location marker
        val latLng = LatLng(mLatitude, mLongitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Location")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mCurrentLocationMarker = mMap.addMarker(markerOptions)

        // Move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(defaultZoom))

        // Stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }

        mDistanceView.text = ""
        mTimeView.text = ""
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    private fun setMapStyle(mMap: GoogleMap) {
        try {
            // Customize the styling of the base map using JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun searchLocation() {

        val locationSearch: EditText = findViewById(R.id.searchView)
        val location: String = locationSearch.text.toString()
        var addressList: List<Address>? = null

        if (location == "") {
            Toast.makeText(applicationContext, "Provide location", Toast.LENGTH_SHORT)
                .show()
        } else {
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (addressList != null) {
                for (i in addressList.indices) {
                    val myAddress = addressList[i]
                    val latLng = LatLng(myAddress.latitude, myAddress.longitude)
                    val markerOptions = MarkerOptions()
                    markerOptions.position(latLng)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    mMap.addMarker(markerOptions)
                    endLatitude = myAddress.latitude
                    endlongitude = myAddress.longitude

                    // These coordinates represent the latitude and longitude of Varsity College Sandton.
                    val mLatitude = -26.0935824633
                    val mLongitude = 28.0477972026

                    val currentLocation = LatLng(mLatitude, mLongitude)

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom))

                    val mo = MarkerOptions()
                    mo.title("Distance")

                    val results = FloatArray(10)
                    Location.distanceBetween(
                        mLatitude,
                        mLongitude,
                        endLatitude,
                        endlongitude,
                        results
                    )

                    val s = ((results[0] / 1000) + 12).toInt()
                    val time = ((results[0] / 1000) / 60).toDouble()
                    val decimal = BigDecimal(time).setScale(2, RoundingMode.HALF_EVEN)

                    // Setting marker to draw route between these two points
                    origin = MarkerOptions().position(currentLocation)
                        .title("HSR Layout")
                        .snippet("origin")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    destination = MarkerOptions().position(LatLng(endLatitude, endlongitude))
                        .title(locationSearch.text.toString())
                        .snippet("Distance = $s KM")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    mMap.addMarker(destination!!)
                    mMap.addMarker(origin!!)

                    Toast.makeText(applicationContext,  "Distance = $s km, Time = $decimal", Toast.LENGTH_SHORT)
                        .show()

                    mDistanceView.text = "$s"
                    mTimeView.text = "$decimal"

                    /*
                    val polyLine = mMap.addPolyline(PolylineOptions().apply {
                        add(currentLocation, latLng)
                        width(10f)
                        color(Color.GREEN)
                        geodesic(true)
                    })

                    val newList = listOf(currentLocation, latLng)
                    polyLine.points  = newList
                     */

                    // Getting URL to the Google Directions API
                    val url = "https://maps.googleapis.com/maps/api/directions/json?origin=-26.0935824633,28.0477972026&destination=${myAddress.latitude},${myAddress.longitude}&mode=driving&key=AIzaSyAP5FRqOX5GLIAzzr3IY8SjcWJT6-fLbMU"
                        //getDirectionUrl(latLng)
                    GetDirection(url).execute()
                }
            }
        }
    }
    // origin: LatLng,
    /*
    private fun getDirectionUrl(dest: LatLng): String {

        // Origin of route
        //val strOrigin = "origin=${origin.latitude},${origin.longitude}"

        // Destination of route
        //val strDest = "destination=${dest.latitude},${dest.longitude}"

        // Setting transportation mode
        //val mode = "mode=driving"

        // Building the parameters to the web service
        //val parameters = "$strOrigin & $strDest & $mode"

        // Output format
        //val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/json?origin=-26.0935824633,28.0477972026&destination=${dest.latitude},${dest.longitude}&mode=driving&key=AIzaSyAP5FRqOX5GLIAzzr3IY8SjcWJT6-fLbMU"
    }*/

    @SuppressLint("StaticFieldLeak")
    inner class GetDirection(private val url: String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body().toString()
            val result = ArrayList<List<LatLng>>()

            try {
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until (respObj.routes[0].legs[0].steps.size-1)) {
                    /*val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble(),
                        respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble(),
                        respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.add(endLatLng)*/
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e:Exception) {
                e.printStackTrace()
            }
            return result
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineOption = PolylineOptions()
            for (i in result.indices) {
                lineOption.addAll(result[i])
                lineOption.width(10f)
                lineOption.color(Color.GREEN)
                lineOption.geodesic(true)
            }
            mMap.addPolyline(lineOption)
        }

    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            //val hm = java.util.HashMap<String, String>()
            //hm["lat"] = (lat.toDouble() / 1E5).toString()
            //hm["lng"] = (lng.toDouble() / 1E5).toString()
            poly.add(latLng)
        }
        return poly
    }

    private fun zoomIn() {
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
    }

    private fun zoomOut() {
        mMap.animateCamera(CameraUpdateFactory.zoomOut())
    }
}