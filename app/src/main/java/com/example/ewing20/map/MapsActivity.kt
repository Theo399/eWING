@file:Suppress("DEPRECATION", "PrivatePropertyName")

package com.example.ewing20.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import com.example.ewing20.map.bird.BirdActivity
import com.example.ewing20.map.hotspot.HotspotActivity
import com.example.ewing20.map.profile.ProfileActivity
import com.example.ewing20.map.setting.SettingActivity
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
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var binding: ActivityMapsBinding
    private var mMap: GoogleMap? = null

    private var mCurrentLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    private val defaultZoom = 15f
    private val REQUEST_LOCATION_PERMISSION = 1
    private val TAG = MapsActivity::class.java.simpleName

    private lateinit var mDistanceView: TextView

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

        mDistanceView = findViewById(R.id.distanceView)

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

        binding.hotspotBtn.setOnClickListener {
            startActivity(Intent(this, HotspotActivity::class.java))
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
            mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            mMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            mMap!!.mapType = GoogleMap.MAP_TYPE_TERRAIN
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

        setMapStyle(mMap!!)
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
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrentLocationMarker = mMap!!.addMarker(markerOptions)

        // Move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(defaultZoom))

        // Stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
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
            mMap!!.isMyLocationEnabled = true
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
                    mMap!!.addMarker(markerOptions)
                    endLatitude = myAddress.latitude
                    endlongitude = myAddress.longitude

                    // These coordinates represent the latitude and longitude of Varsity College Sandton.
                    val mLatitude = -26.0935824633
                    val mLongitude = 28.0477972026

                    val currentLocation = LatLng(mLatitude, mLongitude)

                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom))

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

                    val s = String.format("%1f", results[0] / 1000)

                    // Setting marker to draw route between these two points
                    origin = MarkerOptions().position(currentLocation)
                        .title("HSR Layout")
                        .snippet("origin")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    destination = MarkerOptions().position(LatLng(endLatitude, endlongitude))
                        .title(locationSearch.text.toString())
                        .snippet("Distance = $s KM")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    mMap!!.addMarker(destination!!)
                    mMap!!.addMarker(origin!!)

                    Toast.makeText(applicationContext,  "Distance = $s KM", Toast.LENGTH_SHORT)
                        .show()

                    mDistanceView.text = "$s Km"

                    // Getting URL to the Google Directions API
                    //val url: String = getDirectionUrl(origin!!.position, destination!!.position)

                    //val downloadTask = DownloadTask()

                    // Start downloading json data from Google Directions API
                    //downloadTask.execute(url)
                }
            }
        }
    }
/*
    @SuppressLint("StaticFieldLeak")
    inner class DownloadTask: AsyncTask<String?, Void?, String>() {

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try {
                data = downloadUrl(url[0].toString())
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }
    }

    /**
     * A class to parse the JSON format
     */
    @SuppressLint("StaticFieldLeak")
    inner class ParserTask: AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {

        // Parsing the data in non-ui thread
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jObject = jsonData[0]?.let { JSONObject(it) }!!
                val parser = DataParser()
                routes = parser.parse(jObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return routes
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {

            val points = ArrayList<LatLng>()
            val lineOptions = PolylineOptions()
            for (i in result!!.indices) {
                val path = result[i]
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.GREEN)
                lineOptions.geodesic(true)
            }
            // Drawing polyline in the Google Map for the i-th route
            if (points.size != 0) {
                mMap!!.addPolyline(lineOptions)
            }
        }
    }

    /**
     * A class to download the URL
     */
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream

            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }

            data = sb.toString()
            br.close()
        } catch (e: java.lang.Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    private fun getDirectionUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val strOrigin = "origin=" + origin.latitude + ", " + origin.longitude

        // Destination of route
        val strDest = "destination=" + dest.latitude + ", " + dest.longitude

        // Setting transportation mode
        val mode = "mode=driving"

        // Building the parameters to the web service
        val parameters = "$strOrigin & $strDest & $mode"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyBvMOr4ftJjlnIG4Kx_D6XPcx67o4Mu1G0"
    }
*/
     private fun zoomIn() {
        mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
    }

    private fun zoomOut() {
        mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
    }
}