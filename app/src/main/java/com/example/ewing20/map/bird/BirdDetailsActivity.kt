@file:Suppress("DEPRECATION")

package com.example.ewing20.map.bird

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.drawToBitmap
import com.example.ewing20.R
import com.example.ewing20.map.MapsActivity
import com.example.ewing20.map.bird.birdDBHelper.DBHelper
import com.example.ewing20.utils.Utils

class BirdDetailsActivity : AppCompatActivity() {

    private val dbHandler = DBHelper(this, null)

    private lateinit var nameEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var toolBar: Toolbar
    private lateinit var uploadImageButton: Button
    private lateinit var uploadImageView: ImageView

    private lateinit var raritySpinner: Spinner

    private lateinit var modifyId: String
    private var latLng: String = ""
    private var address: String = ""

    private var rarityTypes =
        mapOf(Pair("Common", 0), Pair("Rare", 1), Pair("Extremely rare", 2))

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bird_details)

        // Setting up image related vars
        uploadImageButton = findViewById(R.id.addImageBtn)
        uploadImageView = findViewById(R.id.imageView)

        // Setting up Toolbar
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        nameEditText = findViewById(R.id.birdName)
        notesEditText = findViewById(R.id.birdNote)
        raritySpinner = findViewById(R.id.raritySpinner)


        raritySpinner.adapter =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_1, ArrayList(rarityTypes.keys)
            )

        raritySpinner.setSelection(rarityTypes["Common"]!!)


        /* Check if activity opened from List Item Click */
        if (intent.hasExtra("id")) {
            modifyId = intent.getStringExtra("id").toString()
            nameEditText.setText(intent.getStringExtra("name"))
            notesEditText.setText(intent.getStringExtra("notes"))
            raritySpinner.setSelection(rarityTypes[intent.getStringExtra("rarity")]!!)
            latLng = intent.getStringExtra("latLng").toString()
            address = intent.getStringExtra("address").toString()
            uploadImageView.setImageBitmap(Utils.getBitmapFromMemCache(intent.getStringExtra("id").toString()))

            findViewById<Button>(R.id.addBtn).visibility = View.GONE

        } else {
            findViewById<Button>(R.id.updateBtn).visibility = View.GONE
            findViewById<Button>(R.id.deleteBtn).visibility = View.GONE
        }

        uploadImageButton.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                getPhoto()
            }
        }
    }
    fun setBirdMarker(view: View) {

        val intent = Intent(applicationContext, BirdDetailsActivity::class.java)
        intent.putExtra("address", address)
        intent.putExtra("latLng", latLng)

        Log.i("LATLNG", "setBirdMarker: $latLng")
        Log.i("LATLNG", "setBirdMarker: $address")

        startActivityForResult(intent, 1)
    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage: Uri? = data?.data
        Log.i("REMOVING", "onActivityResult")

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && selectedImage != null) {
            try {
                val bitMap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                uploadImageView.setImageBitmap(bitMap)
                Utils.addBitmapToMemoryCache(intent.getStringExtra("id").toString(), bitMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data!!.hasExtra("latLng")) {
            Log.i("ADDRESS", "PERKELE")
            Log.i("LATLNG", "DetailsActivity: " + data.getStringExtra("latLng"))
            Log.i("LATLNG", "DetailsActivity: " + data.getStringExtra("address"))

            latLng = data.getStringExtra("latLng").toString()
            address = data.getStringExtra("address").toString()
        }
    }

    fun add(view: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        val image = uploadImageView

        dbHandler.insertRow(
            name,
            rarity.toString(),
            notes,
            Utils.getBytes(image.drawToBitmap()),
            latLng, address
        )

        Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
        finish()

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)

    }

    fun update(view: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        val image = uploadImageView

        dbHandler.updateRow(
            modifyId,
            name,
            rarity.toString(),
            notes,
            Utils.getBytes(image.drawToBitmap()),
            latLng, address
        )

        Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show()
        finish()

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun delete(view: View) {
        dbHandler.deleteRow(modifyId)
        Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
        finish()

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}