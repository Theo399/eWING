package com.example.ewing20.map.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ewing20.authentication.LoginActivity
import com.example.ewing20.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    /**
    private lateinit var dbHelper: DBHelper
    private lateinit var mBinding: ActivityLoginBinding

    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private lateinit var email: String
    private lateinit var password: String
    **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
        dbHelper = DBHelper(this)

        mEmail = mBinding.email.toString()
        mPassword = mBinding.password.toString()

        email = findViewById<TextView?>(R.id.email).toString()
        password = findViewById<TextView?>(R.id.password).toString()

        profile()
        **/

        binding.logoutBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    /*private fun profile() {
        val dbHelper = DBHelper(this)
        val db = dbHelper.readableDatabase
        val selection = "${DBHelper.COLUMN_EMAIL} = ?"
        val selectionArgs = arrayOf(mEmail)
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_NAME} WHERE $selection", selectionArgs)
        if (cursor != null && cursor.moveToFirst()) {
            val storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PASSWORD))
            if (storedPassword == mPassword) {

                email = mEmail
                password = mPassword

                Toast.makeText(this, "Profile set successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Profile set unsuccessfully", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Profile set unsuccessfully", Toast.LENGTH_SHORT).show()
        }
        cursor?.close()
        db.close()
        dbHelper.close()
    }*/
}