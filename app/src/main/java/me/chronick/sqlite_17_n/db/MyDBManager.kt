package me.chronick.sqlite_17_n.db
// https://developer.android.com/training/data-storage/sqlite#kotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import me.chronick.sqlite_17_n.db.MyDBNameClass.COLUMN_NAME_CONTENT
import me.chronick.sqlite_17_n.db.MyDBNameClass.COLUMN_NAME_IMAGE_URI
import me.chronick.sqlite_17_n.db.MyDBNameClass.COLUMN_NAME_TITLE
import me.chronick.sqlite_17_n.db.MyDBNameClass.TABLE_NAME
import kotlin.collections.ArrayList


class MyDBManager(private val context: Context) {
    private val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDB() {
        db = myDbHelper.writableDatabase // открываем БД для записи
    }

    fun insertToDB(title: String, content: String, uri: String) {
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_CONTENT, content)
            put(COLUMN_NAME_IMAGE_URI, uri)

        }
        db?.insert(TABLE_NAME, null, values)
    }

    fun removeItemFromDB(id: String) {
        val selectItemID = BaseColumns._ID + "=$id"
        db?.delete(TABLE_NAME, selectItemID, null)
    }

    fun updateItemFromDB(id: Int, title: String, content: String, uri: String) {
        val selectItemID = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_CONTENT, content)
            put(COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.update(TABLE_NAME, values, selectItemID, null)
    }

    @SuppressLint("Range")
    fun readDBData(searchText: String): ArrayList<ListItem> { // ждем при чтении
        val dataList = ArrayList<ListItem>()
        val selection = "$COLUMN_NAME_TITLE LIKE ?"
        val cursor =
            db?.query(TABLE_NAME, null, selection, arrayOf("%$searchText%"), null, null, null, null)

        with(cursor) { // для получения доступа к функциям класса
            while (this?.moveToNext()!!) {
                val dataId = cursor?.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val dataTitle = cursor?.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE))
                val dataContent = cursor?.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT))
                val dataUri = cursor?.getString(cursor.getColumnIndex(COLUMN_NAME_IMAGE_URI))
                val item = ListItem()
                if (dataId != null) {
                    item.id = dataId
                }
                if (dataTitle != null) {
                    item.title = dataTitle
                }
                if (dataContent != null) {
                    item.desc = dataContent
                }
                if (dataUri != null) {
                    item.uri = dataUri
                }
                dataList.add(item)
            }
        }
        cursor?.close()
        return dataList
    }

    fun closeDB() {
        myDbHelper.close()
    }
}