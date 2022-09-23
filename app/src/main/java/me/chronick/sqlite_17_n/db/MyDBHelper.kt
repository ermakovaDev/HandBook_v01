package me.chronick.sqlite_17_n.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.chronick.sqlite_17_n.db.MyDBNameClass.CREATE_TABLE
import me.chronick.sqlite_17_n.db.MyDBNameClass.DATABASE_NAME
import me.chronick.sqlite_17_n.db.MyDBNameClass.DATABASE_VERSION
import me.chronick.sqlite_17_n.db.MyDBNameClass.DELETE_TABLE

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DELETE_TABLE)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}