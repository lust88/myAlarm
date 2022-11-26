package com.lstojak.myalarm.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.lstojak.myalarm.R.id
import com.lstojak.myalarm.databinding.ActivityMainBinding
import com.lstojak.myalarm.model.Area
import com.lstojak.myalarm.model.PhoneNumber
import com.lstojak.myalarm.util.DbHelper
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        private val SMS_PERMISSION_CONSTANT = 100
    }

    private lateinit var binding : ActivityMainBinding

    private var dbHelper : DbHelper = DbHelper.getInstance(this)

    private var areas : Map<Int, Area> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(id.am_toolbar))
        binding.toolbarLayout.title = title

        initAreas();
    }

    fun initAreas() {
//        val area1 = Area(1, "DOM", "123", "321", 1)
//        val area2 = Area(2, "Garaz", "567", "765", 0)
//
//        areas = mapOf(1 to area1, 2 to area2)

        var phoneNumber : PhoneNumber = dbHelper.readPhoneNumber();

        val areaList : List<Area> = dbHelper.readAreas()
        areas = areaList.map {it.id to it}.toMap()

        for (i in 1 .. areas.size) {
            val id: Int = findRowId(i)!!
            val area: Area? = areas.get(i)

            if (id != 0 && Objects.nonNull(area)) {
                val row: TableRow = findViewById<View>(id) as TableRow
                if (area!!.enabled == 0 || area?.name?.isBlank() ?: false ||area?.armCommand?.isBlank() ?: false
                    || area?.disarmCommand?.isBlank() ?: false) {
                    row.visibility = View.INVISIBLE
                } else {
                    row.visibility = View.VISIBLE

                    val areaNameId: Int = findAreaNameId(i)!!
                    val armBtnId: Int = findArmBtnId(i)!!
                    val disarmBtnId: Int = findDisarmBtnId(i)!!

                    setAreaName(areaNameId, area.name)
                    setArmAction(armBtnId, phoneNumber.value, area.armCommand)
                    setDisarmAction(disarmBtnId, phoneNumber.value, area.disarmCommand)
                }
            }
        }
    }

    fun findRowId(i: Int) :Int? {
        when (i) {
            1 -> return id.row1
            2 -> return id.row2
            3 -> return id.row3
            4 -> return id.row4
            5 -> return id.row5
        }
        return null;
    }

    fun findAreaNameId(i: Int) : Int? {
        when (i) {
            1 -> return id.tv_area_name1
            2 -> return id.tv_area_name2
            3 -> return id.tv_area_name3
            4 -> return id.tv_area_name4
            5 -> return id.tv_area_name5
        }
        return null;
    }

    fun findArmBtnId(i: Int) : Int? {
        when (i) {
            1 -> return id.arm_btn1
            2 -> return id.arm_btn2
            3 -> return id.arm_btn3
            4 -> return id.arm_btn4
            5 -> return id.arm_btn5
        }
        return null;
    }

    fun findDisarmBtnId(i: Int) : Int? {
        when (i) {
            1 -> return id.disarm_btn1
            2 -> return id.disarm_btn2
            3 -> return id.disarm_btn3
            4 -> return id.disarm_btn4
            5 -> return id.disarm_btn5
        }
        return null;
    }

    fun setAreaName(id: Int, areaName: String?) {
        val textView: TextView = findViewById<View>(id) as TextView
        textView.setText(areaName)
    }

    fun setArmAction(id: Int, phoneNumber: String?, armMsg: String?) {
        val armBtn: ImageButton = findViewById(id)

        armBtn.setOnClickListener { view ->
            checkPermissionToSendSms()
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, armMsg, null, null)

            Toast.makeText(this, "Arming", Toast.LENGTH_SHORT).show()
        }
    }

    fun setDisarmAction(id: Int, phoneNumber: String?, disarmMsg: String?) {
        val disarmBtn: ImageButton = findViewById(id)

        disarmBtn.setOnClickListener { view ->
            checkPermissionToSendSms()
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, disarmMsg, null, null)

            Toast.makeText(this, "Disarming", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.lstojak.myalarm.R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissionToSendSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it")
                val permissions = arrayOf<String>(Manifest.permission.SEND_SMS)
                requestPermissions(permissions, SMS_PERMISSION_CONSTANT)
            }
        }
    }
}