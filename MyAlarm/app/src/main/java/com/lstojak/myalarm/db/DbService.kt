package com.lstojak.myalarm.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.FileObserver.CREATE

class DbService (context: Context) :
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
    }


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PHONE_NUMBER_TABLE = StringBuilder("CREATE TABLE").append(PHONE_NUMBER_TABLE)
            .append("(").append(ID).append(" INTEGER PRIMARY KEY,").append(PHONE_NUMBER).append(" TEXT)").toString()

        val CREATE_AREA_TABLE = StringBuilder("CREATE TABLE").append(AREA_TABLE).append("(").append(ID)
            .append(" INTEGER PRIMARY KEY,").append(NAME).append(" TEXT,").append(ARM_CMD).append(" TEXT,")
            .append(DISARM_CMD).append(" TEXT,").append(ENABLED).append(" INTEGER)").toString()

        db.execSQL(CREATE_PHONE_NUMBER_TABLE)
        db.execSQL(CREATE_AREA_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS " + PHONE_NUMBER_TABLE)
//        db.execSQL("DROP TABLE IF EXISTS " + AREA_TABLE)
//        onCreate(db)
    }



}