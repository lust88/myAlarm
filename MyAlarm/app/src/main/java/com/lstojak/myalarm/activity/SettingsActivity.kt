package com.lstojak.myalarm.activity

import android.content.Intent
import android.os.Bundle
import android.service.autofill.Validators.or
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.lstojak.myalarm.R
import com.lstojak.myalarm.databinding.ActivityMainBinding
import com.lstojak.myalarm.databinding.ActivitySettingsBinding
import com.lstojak.myalarm.model.Area
import com.lstojak.myalarm.model.PhoneNumber
import com.lstojak.myalarm.util.DbHelper
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var dbHelper : DbHelper = DbHelper.getInstance(this)

//    private var phoneNumber: PhoneNumber = null
    private var areas : Map<Int, Area> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.sa_toolbar))
        binding.saToolbarLayout.title = title

        initAreas()
        backAction()
        saveAction()
    }

    fun initAreas() {
        var phoneNumber: PhoneNumber = dbHelper.readPhoneNumber();

        val areaList : List<Area> = dbHelper.readAreas()
        areas = areaList.map {it.id to it}.toMap()

        val editText: EditText = findViewById<View>(R.id.alarmPhoneNumber) as EditText
        editText.setText(phoneNumber!!.value)

        for (i in 1 .. areas.size) {
            val area: Area? = areas.get(i)

            val areaNameEditTextId : Int = findAreaNameEditTextId(i)!!
            val areaName: EditText = findViewById<View>(areaNameEditTextId) as EditText
            areaName.setText(area!!.name)

            val armMsgEditTextId : Int = findArmMsgEditTextId(i)!!
            val armMsg: EditText = findViewById<View>(armMsgEditTextId) as EditText
            armMsg.setText(area!!.armCommand)

            val disarmMsgEditTextId : Int = findDisarmMsgEditTextId(i)!!
            val disarmMsg: EditText = findViewById<View>(disarmMsgEditTextId) as EditText
            disarmMsg.setText(area!!.disarmCommand)

            val enabledCheckBoxId : Int = findEnabledCheckBoxId(i)!!
            val enabled: CheckBox = findViewById<View>(enabledCheckBoxId) as CheckBox
            enabled.setChecked(area.enabled == 1)
        }
    }

    fun findAreaNameEditTextId(i: Int) : Int? {
        when (i) {
            1 -> return R.id.areaName1
            2 -> return R.id.areaName2
            3 -> return R.id.areaName3
            4 -> return R.id.areaName4
            5 -> return R.id.areaName5
        }
        return null;
    }

    fun findArmMsgEditTextId(i: Int) : Int? {
        when (i) {
            1 -> return R.id.armMsg1
            2 -> return R.id.armMsg2
            3 -> return R.id.armMsg3
            4 -> return R.id.armMsg4
            5 -> return R.id.armMsg5
        }
        return null;
    }

    fun findDisarmMsgEditTextId(i: Int) : Int? {
        when (i) {
            1 -> return R.id.disarmMsg1
            2 -> return R.id.disarmMsg2
            3 -> return R.id.disarmMsg3
            4 -> return R.id.disarmMsg4
            5 -> return R.id.disarmMsg5
        }
        return null;
    }

    fun findEnabledCheckBoxId(i: Int) : Int? {
        when (i) {
            1 -> return R.id.enabled1
            2 -> return R.id.enabled2
            3 -> return R.id.enabled3
            4 -> return R.id.enabled4
            5 -> return R.id.enabled5
        }
        return null;
    }

    fun backAction() {
        binding.saBackBtn.setOnClickListener {
//            this.finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun saveAction() {
        binding.saSaveBtn.setOnClickListener { view ->
            val editText: EditText = findViewById<View>(R.id.alarmPhoneNumber) as EditText
            var phoneNumber = editText.text.toString()

            for (i in 1 .. areas.size) {
                val area: Area? = areas.get(i)

                val areaNameEditTextId: Int = findAreaNameEditTextId(i)!!
                val areaName: EditText = findViewById<View>(areaNameEditTextId) as EditText
                area?.name = areaName.text.toString()

                val armMsgEditTextId : Int = findArmMsgEditTextId(i)!!
                val armMsg: EditText = findViewById<View>(armMsgEditTextId) as EditText
                area?.armCommand = armMsg.text.toString()

                val disarmMsgEditTextId : Int = findDisarmMsgEditTextId(i)!!
                val disarmMsg: EditText = findViewById<View>(disarmMsgEditTextId) as EditText
                area?.disarmCommand = disarmMsg.text.toString()

                val enabledCheckBoxId : Int = findEnabledCheckBoxId(i)!!
                val enabled: CheckBox = findViewById<View>(enabledCheckBoxId) as CheckBox
                if (enabled.isChecked) {
                    area?.enabled = 1
                } else {
                    area?.enabled = 0
                }
            }

            dbHelper.saveData(phoneNumber, areas)

            Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }
}