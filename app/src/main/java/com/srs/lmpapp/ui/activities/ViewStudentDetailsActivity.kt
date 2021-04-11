package com.srs.lmpapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityEnrolledCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityFacultyCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityViewStudentDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.model.User
import com.srs.lmpapp.ui.adapters.MyCourseListAdapter
import com.srs.lmpapp.ui.adapters.StudentListAdapter
import com.srs.lmpapp.utils.Constants

class ViewStudentDetailsActivity : BaseActivity() {
    private lateinit var currentCourse: Course
    lateinit var recyclerStudentDetails: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: StudentListAdapter
    private lateinit var binding: ActivityViewStudentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        recyclerStudentDetails= findViewById(R.id.recyclerStudentDetails)
        layoutManager = LinearLayoutManager(this@ViewStudentDetailsActivity)
       //studentsList: List<String> =currentCourse.enrolledby!!
        showProgressDialog("Please Wait..")
        FirestoreClass().getStudentDetails(this@ViewStudentDetailsActivity,currentCourse.id)

    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarStudentDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarStudentDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }
    fun successStudentList(studentList:List<User>)
    {
        hideProgressDialog()
        recyclerAdapter = StudentListAdapter(this@ViewStudentDetailsActivity as Context , studentList)
        recyclerStudentDetails.adapter = recyclerAdapter
       recyclerStudentDetails.layoutManager = layoutManager


    }
}