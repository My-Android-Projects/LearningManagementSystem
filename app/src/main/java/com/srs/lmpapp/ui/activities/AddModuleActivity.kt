package com.srs.lmpapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityAddModuleBinding
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.ui.adapters.CourseSearchResultAdapter
import com.srs.lmpapp.ui.adapters.ModuleListAdapter
import com.srs.lmpapp.utils.Constants


class AddModuleActivity : BaseActivity() {
    private lateinit var binding: ActivityAddModuleBinding
    private lateinit var currentCourse:Course
    private  var counter=1

    lateinit var recyclerCourseSessions: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: ModuleListAdapter


    private var modules:ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        if(currentCourse.modules!=null) {
            modules.addAll(currentCourse.modules!!.toTypedArray())
            Toast.makeText(this, "Module size ${modules.size}", Toast.LENGTH_SHORT)
                .show();
        }

        recyclerCourseSessions= findViewById(R.id.recyclerModuleList)
        layoutManager = LinearLayoutManager(this@AddModuleActivity)
        recyclerAdapter = ModuleListAdapter(this@AddModuleActivity as Context, modules)
        recyclerCourseSessions.adapter = recyclerAdapter
        recyclerCourseSessions.layoutManager = layoutManager




    }


    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddModuleActivity)
        val actionBar = supportActionBar
        binding.btnModuleBack.setOnClickListener {

            val moduleList = recyclerAdapter.getItems()

            currentCourse.modules=moduleList

            val intent = Intent()

            intent.putExtra(Constants.CURRENT_COURSE, currentCourse)
            setResult(RESULT_OK, intent)
            finish() }
        binding.btnAddModule.setOnClickListener{
            Toast.makeText(this, "Add Module Selected", Toast.LENGTH_SHORT)
                .show();


            recyclerAdapter.addItem("enter module...")




        }
    }

    override fun onResume() {
        //getMyModuleListFromFireStore()
        super.onResume()
    }






}