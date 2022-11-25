package com.lstojak.myalarm.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.lstojak.myalarm.R
import com.lstojak.myalarm.databinding.ActivityMainBinding
import com.lstojak.myalarm.model.Area


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val areas : ArrayList<Area> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.am_toolbar))
        binding.toolbarLayout.title = title

        armAction()
        disarmAction()
    }

    fun armAction() {
        val armBtn: ImageButton = findViewById(R.id.img_lock_btn1)

//        val  viewPos : View = findViewById(R.id.myCoordinatorLayout);


        armBtn.setOnClickListener { view ->
            Snackbar.make(view, "Arming", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()

//            Snackbar.make(viewPos, R.string.app_name, Snackbar.LENGTH_LONG)
//                .show();

        }
    }

    fun disarmAction() {
        val disarmBtn: ImageButton = findViewById(com.lstojak.myalarm.R.id.img_unlock_btn1)

        disarmBtn.setOnClickListener { view ->
            Toast.makeText(this, "Disarming", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.lstojak.myalarm.R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.lstojak.myalarm.R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}