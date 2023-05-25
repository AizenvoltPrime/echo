package com.example.echo
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/* Database to insert/load data into/from UserFavorites
*/

class UserFavorites(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    /* Create database. Values are taken from 'static' object below */
    override fun onCreate(db: SQLiteDatabase) {
        val create_query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY, " +
                SONG_NAME + " TEXT," +
                SONG_INFO + " TEXT" + ")")
        db.execSQL(create_query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME) // Check whether table exists
        onCreate(db)
    }

    /* Read DB */
    fun getFavoriteSong(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    /* Add songName and songInfo values into the ContentValues set,
    *  make db writeable and write the above to it. */
    fun addToFavorites(songName : String, songInfo : String ){
        val db_info = ContentValues()
        db_info.put(SONG_NAME, songName)
        db_info.put(SONG_INFO, songInfo)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, db_info)
        db.close()
    }

    companion object{
        private val DATABASE_NAME = "USERFAVORITES"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "UserFavorites"
        val ID = "id"
        val SONG_NAME = "songName"
        val SONG_INFO = "songInfo"
    }
}