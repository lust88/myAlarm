package com.lstojak.myalarm.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.lstojak.myalarm.R
import com.lstojak.myalarm.databinding.ActivityMainBinding
import com.lstojak.myalarm.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.sa_toolbar))
        binding.saToolbarLayout.title = title

        backAction()
        saveAction()
    }

    fun backAction() {
        binding.saBackBtn.setOnClickListener {
            this.finish()
        }
    }

    fun saveAction() {
        binding.saSaveBtn.setOnClickListener { view ->
            Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }
}