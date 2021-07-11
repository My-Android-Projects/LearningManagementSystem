package com.srs.lmpapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R

import com.srs.lmpapp.databinding.ActivityAddModuleBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.model.Module
import com.srs.lmpapp.ui.adapters.CourseSearchResultAdapter
import com.srs.lmpapp.ui.adapters.ModuleListAdapter
import com.srs.lmpapp.utils.Constants


class AddModuleActivity : BaseActivity() {
    private lateinit var binding: ActivityAddModuleBinding
    private  var topicList:ArrayList<String> = ArrayList<String>()
    private  var noOfTopics=0
    private var moduleDuration=0

    private val TOPICS_ACTIVITY_REQUEST_CODE=402


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        binding.btnSubmit.setOnClickListener {

            moduleDuration=binding.txtmoduleDuration.text.toString().toInt()
            binding.txtmoduleNoOfTopics.setText(topicList?.size.toString())
            showProgressDialog("Please Wait...")
            val module= Module(
               "",
                binding.txtModuleName.text.toString(),
                moduleDuration,"",
                topicList )
           FirestoreClass().uploadModuleDetails(this@AddModuleActivity,module)
        }
        binding.tvViewTopics.setOnClickListener {
            val destIntent=Intent(this@AddModuleActivity,TopicsListActivity::class.java)
            destIntent.putStringArrayListExtra(Constants.TOPICS,topicList)
            startActivityForResult(destIntent,TOPICS_ACTIVITY_REQUEST_CODE)
        }




    }
    fun  moduleUploadSuccess(moduleId:String)
    {
        hideProgressDialog()
        val intent = Intent()
        intent.putExtra("MODULE", moduleId)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setupActionBar()
    {

        setSupportActionBar(binding.toolbarAddModuleActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddModuleActivity.setNavigationOnClickListener { onBackPressed() }

    }
    override fun onResume() {
        //getMyModuleListFromFireStore()
        super.onResume()
    }






}