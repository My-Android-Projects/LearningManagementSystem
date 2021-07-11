package com.srs.lmpapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityEnrolledCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityFacultyCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.utils.Constants

class FacultyCourseDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityFacultyCourseDetailsBinding
    private lateinit var currentCourse:Course
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        setupActionBar()
        displayDetails()
        binding.tvViewStudent.setOnClickListener()
        {
           val destIntent = Intent(this@FacultyCourseDetailsActivity, ViewStudentDetailsActivity::class.java)
            destIntent.putExtra(Constants.CURRENT_COURSE,currentCourse)
            startActivity(destIntent)
        }
        binding.btnAddAssignment.setOnClickListener()
        {
            val destIntent = Intent(this@FacultyCourseDetailsActivity, AddAssignmentActivity::class.java)
            destIntent.putExtra(Constants.CURRENT_COURSE,currentCourse)
            startActivity(destIntent)
        }
    }

    fun displayDetails()
    {
        binding.txtCourseName.text=currentCourse.name
        binding.txtCourseCategory.text=currentCourse.category
        binding.txtNoStudentsEnrolled.text=currentCourse.enrolledby?.size.toString()
        binding.txtNoSeats.text=currentCourse.totseats.toString()
        binding.txtSchedule.text="${currentCourse.startdate} - ${currentCourse.enddate}"
        binding.txtCourseDesc.text=currentCourse.description
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this,R.layout.listitem,currentCourse.modules!!)
        binding.lstModuleList.adapter=adapter;
        Picasso.get().load(currentCourse.courseimage).error(R.drawable.ic_user_placeholder).into(binding.imgCourseImage)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarFacultyCourseDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarFacultyCourseDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun courseDetailsSuccess(course: Course) {
        hideProgressDialog()
        currentCourse=course


    }

    fun allDetailsUpdatedSuccessfully() {
        hideProgressDialog()

    }
}