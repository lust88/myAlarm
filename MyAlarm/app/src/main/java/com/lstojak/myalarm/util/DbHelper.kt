package com.lstojak.myalarm.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.SimPhonebookContract.SimRecords.PHONE_NUMBER
import androidx.core.database.getStringOrNull
import com.lstojak.myalarm.model.Area
import com.lstojak.myalarm.model.PhoneNumber
import com.lstojak.myalarm.service.SmsReceiver
import kotlin.reflect.KParameter

class DbHelper private constructor (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private var DB_HELPER : DbHelper? = null

        //operator
        @JvmStatic fun getInstance(context: Context) : DbHelper {
            if (DB_HELPER == null) {
                DB_HELPER = DbHelper(context)
            }
            return DB_HELPER!!
        }

        private val DATABASE_NAME = "myAlarmDB"
        private val DATABASE_VERSION = 2

        private val PHONE_NUMBER_TABLE = "PhoneNumber"
        private val AREA_TABLE = "Area"

        private val ID = "id"

        private val VALUE = "value"

        private val NAME = "name"
        private val ARM_CMD = "arm_cmd"
        private val DISARM_CMD = "disarm_cmd"
        private val ENABLED = "enabled"

        private val AREA_NUMBER = 5
    }



    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PHONE_NUMBER_TABLE = StringBuilder("CREATE TABLE ").append(PHONE_NUMBER_TABLE)
            .append("(").append(ID).append(" INTEGER PRIMARY KEY,").append(VALUE).append(" TEXT)").toString()

        val CREATE_AREA_TABLE = StringBuilder("CREATE TABLE ").append(AREA_TABLE).append("(").append(ID)
            .append(" INTEGER PRIMARY KEY,").append(NAME).append(" TEXT,").append(ARM_CMD).append(" TEXT,")
            .append(DISARM_CMD).append(" TEXT,").append(ENABLED).append(" INTEGER)").toString()


//        val db = this.writableDatabase

        db.execSQL(CREATE_PHONE_NUMBER_TABLE)
        db.execSQL(CREATE_AREA_TABLE)

        createEmptyPhoneNumberRow(db)
        createEmptyAreaRows(db, AREA_NUMBER)
//        db.close()
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
                phoneNumber += PhoneNumber(
                        cursor.getInt(0),
                        cursor.getStringOrNull(1)
                    )

            } while (cursor.moveToNext())
        }

        cursor.close();
        return phoneNumber.get(0);
    }

    fun readAreas(): List<Area> {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $AREA_TABLE", null)

        var areas: List<Area> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                areas += Area(
                    cursor.getInt(0),
                    cursor.getStringOrNull(1),
                    cursor.getStringOrNull(2),
                    cursor.getStringOrNull(3),
                    cursor.getInt(4)
                )
            } while (cursor.moveToNext())
        }

        cursor.close();
        return areas;
    }

    fun createEmptyPhoneNumberRow(db: SQLiteDatabase) {
        addPhoneNumber(db,null)
    }

    fun createEmptyAreaRows(db: SQLiteDatabase, number: Int) {
        for (i in 1..number) {
            addAlarmItem(db, i, null, null, null, 0)
        }
    }

    fun saveData(phoneNumber: String?, areas: Map<Int, Area>) {
        val db = this.writableDatabase

        val phoneNumberValues = ContentValues()
        phoneNumberValues.put(VALUE, phoneNumber ?: null)
        updateDbData(db, "1", PHONE_NUMBER_TABLE, phoneNumberValues)

        for (i in 1 .. areas.size) {
            val area: Area? = areas.get(i)

            val areaValues = ContentValues()
            areaValues.put(NAME, area?.name)
            areaValues.put(ARM_CMD, area?.armCommand)
            areaValues.put(DISARM_CMD, area?.disarmCommand)
            areaValues.put(ENABLED, area?.enabled)

            updateDbData(db, area?.id.toString(), AREA_TABLE, areaValues)
        }
    }

    private fun addAlarmItem(db: SQLiteDatabase, id: Int, name: String?, armCmd: String?, disarmCmd: String?, enabled: Int) {
        val values = ContentValues()
        values.put(ID, id)
        values.put(NAME, name)
        values.put(ARM_CMD, armCmd)
        values.put(DISARM_CMD, disarmCmd)
        values.put(ENABLED, enabled)

        insertIntoDb(db, AREA_TABLE, values)
    }

    private fun addPhoneNumber(db: SQLiteDatabase, phoneNumber: String?) {
        val values = ContentValues()
        values.put(ID, 1)
        values.put(VALUE, phoneNumber)
        insertIntoDb(db, PHONE_NUMBER_TABLE, values)
    }

    private fun insertIntoDb(db: SQLiteDatabase, tableName: String, values: ContentValues) {
//        val db = this.writableDatabase
        db.insert(tableName, null, values)
//        db.close()
    }

    private fun updateDbData(db: SQLiteDatabase, id: String, tableName: String, values: ContentValues) {
        db.update(tableName, values, "id = ?", arrayOf(id))
    }
}