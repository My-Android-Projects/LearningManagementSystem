package com.srs.lmpapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityAddTopicBinding
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.utils.Constants

class FacultyAddTopicsActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddTopicBinding
    lateinit var currentModule: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        currentModule = intent.getParcelableExtra(Constants.CURRENT_MODULE)!!

    }
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddTopicActivity)
        val actionBar = supportActionBar
        binding.btnAddTopic.setOnClickListener {
            val intent = Intent(this@FacultyAddTopicsActivity, AddTopicActivity::class.java)
            intent.putExtra(Constants.CURRENT_MODULE, currentModule)
            startActivity(intent)
        }

    }
    override fun onResume() {
        super.onResume()
    }

}