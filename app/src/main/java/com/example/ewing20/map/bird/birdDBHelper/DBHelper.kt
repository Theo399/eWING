package com.example.ewing20.map.bird.birdDBHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        // create a birds table to store bird data
        db.execSQL(
            "CREATE TABLE $TABLE_BIRDS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT," +
                    " $COLUMN_RARITY INT, $COLUMN_NOTE TEXT, $COLUMN_IMAGE BLOB, $COLUMN_LATLONG TEXT, $COLUMN_ADDRESS TEXT, $COLUMN_DATE DATETIME)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BIRDS")
        onCreate(db)
    }


    fun insertRow(
        name: String,
        rarity: String,
        note: String,
        image: ByteArray?,
        latLng: String?,
        address: String?
    ) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_RARITY, rarity)
        values.put(COLUMN_NOTE, note)
        values.put(COLUMN_IMAGE, image)
        values.put(COLUMN_LATLONG, latLng)
        values.put(COLUMN_ADDRESS, address)
        values.put(COLUMN_DATE, getDateTime())

        val db = this.writableDatabase
        db.insert(TABLE_BIRDS, null, values)
        db.close()
    }

    fun updateRow(
        rowID: String,
        name: String,
        rarity: String,
        note: String,
        image: ByteArray?,
        latLng: String?,
        address: String?
    ) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_RARITY, rarity)
        values.put(COLUMN_NOTE, note)
        values.put(COLUMN_IMAGE, image)
        values.put(COLUMN_LATLONG, latLng)
        values.put(COLUMN_ADDRESS, address)

        val db = this.writableDatabase
        db.update(TABLE_BIRDS, values, "$COLUMN_ID = ?", arrayOf(rowID))
        db.close()
    }

    fun deleteRow(rowID: String) {
        val db = this.writableDatabase
        db.delete(TABLE_BIRDS, "$COLUMN_ID = ?", arrayOf(rowID))
        db.close()
    }

    fun getAllRow(orderBy: String): Cursor? {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_BIRDS $orderBy", null)
    }

    private fun getDateTime(): String? {

        val dateFormat = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss", Locale.getDefault()
        )
        val date = Date()
        return dateFormat.format(date)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "myDBfile.db"

        const val TABLE_BIRDS = "birds"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_RARITY = "rarity"
        const val COLUMN_NOTE = "note"
        const val COLUMN_DATE = "date"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_LATLONG = "latLng"
        const val COLUMN_ADDRESS = "address"
    }
}