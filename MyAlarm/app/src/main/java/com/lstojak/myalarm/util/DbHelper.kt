package com.lstojak.myalarm.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lstojak.myalarm.model.PhoneNumber

class DbHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "myAlarmDB"
        private val DATABASE_VERSION = 1

        private val PHONE_NUMBER_TABLE = "PhoneNumber"
        private val AREA_TABLE = "Area";

        private val ID = "id"

        private val PHONE_NUMBER = "phone_number"

        private val NAME = "name";
        private val ARM_CMD = "arm_cmd";
        private val DISARM_CMD = "disarm_cmd";
        private val ENABLED = "enabled";

        private val AREA_NUMBER = 5;
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PHONE_NUMBER_TABLE = StringBuilder("CREATE TABLE").append(PHONE_NUMBER_TABLE)
            .append("(").append(ID).append(" INTEGER PRIMARY KEY,").append(PHONE_NUMBER).append(" TEXT)").toString()

        val CREATE_AREA_TABLE = StringBuilder("CREATE TABLE").append(AREA_TABLE).append("(").append(ID)
            .append(" INTEGER PRIMARY KEY,").append(NAME).append(" TEXT,").append(ARM_CMD).append(" TEXT,")
            .append(DISARM_CMD).append(" TEXT,").append(ENABLED).append(" INTEGER)").toString()

        db.execSQL(CREATE_PHONE_NUMBER_TABLE)
        db.execSQL(CREATE_AREA_TABLE)
        createEmptyPhoneNumberRow()
        createEmptyAreaRows(AREA_NUMBER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PHONE_NUMBER_TABLE)
        db.execSQL("DROP TABLE IF EXISTS " + AREA_TABLE)
        onCreate(db)
    }

    fun readPhoneNumber(): PhoneNumber {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $PHONE_NUMBER_TABLE", null)

        var phoneNumber: List<PhoneNumber> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                phoneNumber += PhoneNumber(
                        cursor.getInt(1),
                        cursor.getString(2)
                    )

            } while (cursor.moveToNext())
        }

        cursor.close();
        return phoneNumber.get(0);
    }

    fun createEmptyPhoneNumberRow() {
        addPhoneNumber(null)
    }

    fun createEmptyAreaRows(number: Int) {
        for (i in 1..number) {
            addAlarmItem(i, null, null, null, 1)
        }
    }

    private fun addAlarmItem(id: Int, name: String?, armCmd: String?, disarmCmd: String?, enabled: Int) {
        val values = ContentValues()
        values.put(ID, id)
        values.put(NAME, name)
        values.put(ARM_CMD, armCmd)
        values.put(DISARM_CMD, disarmCmd)
        values.put(ENABLED, enabled)

        insertIntoDb(AREA_TABLE, values)
    }

    private fun addPhoneNumber(phoneNumber: String?) {
        val values = ContentValues()
        values.put(PHONE_NUMBER, phoneNumber)
        insertIntoDb(PHONE_NUMBER_TABLE, values)
    }

    private fun insertIntoDb(tableName: String, values: ContentValues) {
        val db = this.writableDatabase
        db.insert(tableName, null, values)
        db.close()
    }

    private fun updateIntoDb(id: String, tableName: String, values: ContentValues) {
        val db = this.writableDatabase
        db.update(tableName, values, "id = ?", arrayOf(id))
        db.close()
    }
}