package com.example.ewing20.map.bird

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.R
import com.example.ewing20.databinding.ActivityBirdBinding
import com.example.ewing20.map.bird.birdAdapter.BirdAdapter
import com.example.ewing20.map.bird.birdDBHelper.DBHelper
import com.example.ewing20.map.bird.birdVariables.Bird
import com.example.ewing20.utils.Utils
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class BirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBirdBinding

    private val dbHandler = DBHelper(this, null)
    private var birdList = ArrayList<Bird>()

    private lateinit var mSpinner: Spinner
    private lateinit var mTextView: TextView

    private lateinit var mListView: ListView
    private var customAdapter: BirdAdapter? = null

    private var rarityTypes = mapOf(Pair(0, "Common"), Pair(1, "Rare"), Pair(2, "Extremely rare"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailsBtn.setOnClickListener {
            startActivity(Intent(this, BirdDetailsActivity::class.java))

          //Add button to trigger notification
            /*binding.sendNotificationBtn.setOnClickListener {
            sendBirdObservationNotification()*/
        }

        mTextView = findViewById(R.id.textView)
        mSpinner = findViewById(R.id.sortSpinner)
        mListView = findViewById(R.id.listView)

        val sortNames = resources.getStringArray(R.array.sort_types)

        val myAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            sortNames
        )

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner.adapter = myAdapter

        mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {

                when (sortNames[position]) {
                    "SORT BY NAME" -> loadIntoList("ORDER BY LENGTH(name) DESC")
                    "SORT BY RARITY" -> loadIntoList("ORDER BY rarity DESC")
                    "SORT BY NOTE" -> loadIntoList("ORDER BY LENGTH(note) DESC")
                    "SORT BY DATE" -> loadIntoList("ORDER BY date DESC")
                }
                Toast.makeText(
                    this@BirdActivity,
                    "" + sortNames[position], Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun loadIntoList(orderBy: String) {
        birdList.clear()
        val cursor: Cursor? = dbHandler.getAllRow(orderBy)
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {

            val id = cursor.getString(0)
            val name = cursor.getString(1)
            val rarity = cursor.getInt(2)
            val note = cursor.getString(3)
            val image = cursor.getBlob(4)
            val latLng = cursor.getString(5)
            val address = cursor.getString(6)
            val date = cursor.getString(7)

            birdList.add(Bird(id, name, rarity, note, image, latLng, address, date))
            cursor.moveToNext()
        }

        customAdapter?.notifyDataSetChanged()

        if (birdList.isEmpty()) {
            mTextView.text = "Add a bird."
        } else {
            mTextView.visibility = View.GONE

            customAdapter = BirdAdapter(this@BirdActivity, birdList)
            mListView.adapter = customAdapter

            mListView.setOnItemClickListener { _, _, i, _ ->

                val id = birdList[i].id
                val image = birdList[i].image
                val name = birdList[i].name
                val note = birdList[i].note
                val rarity = birdList[i].rarity
                val latLng = birdList[i].latLng
                val address = birdList[i].address

                val intent = Intent(this, BirdDetailsActivity::class.java)

                intent.putExtra("id", id)
                intent.putExtra("name", name)
                intent.putExtra("notes", note)
                intent.putExtra("rarity", rarityTypes[rarity])
                intent.putExtra("latLng", latLng)
                intent.putExtra("address", address)

                Utils.addBitmapToMemoryCache(id, Utils.getImage(image))

                startActivity(intent)
            }
        }
    }
    public override fun onResume() {
        super.onResume()
    }
    
}
/* private fun sendBirdObservationNotification() {
                // Create a notification channel 
                val channelId = "bird_observation_channel"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Bird Observation Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    val notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)
                }

                // Build the notification
                val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("New Bird Observation")
                    .setContentText("A new bird has been observed!")
                    .setSmallIcon(R.drawable.ic_notification)
                    .build()

                // Display the notification
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification)
            }*/ 
