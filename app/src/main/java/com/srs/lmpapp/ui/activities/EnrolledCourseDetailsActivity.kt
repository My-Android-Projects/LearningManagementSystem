package com.srs.lmpapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityEnrolledCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.NotificationUtils

class EnrolledCourseDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityEnrolledCourseDetailsBinding
    private lateinit var currentCourse:Course
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrolledCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        displayDetails()
        binding.btnUnEnroll.setOnClickListener()
        {
           if(currentCourse?.id!=null && binding.btnUnEnroll.isEnabled) {
               showProgressDialog("Please Wait...")
               FirestoreClass().unEnrollCourse(
                   this@EnrolledCourseDetailsActivity,
                   currentCourse.id
               )
           }
        }
        binding.btnOpenCourse.setOnClickListener()
        {
            val destIntent = Intent(this@EnrolledCourseDetailsActivity, CurrentCourseDetailsActivity::class.java)
            destIntent.putExtra(Constants.CURRENT_COURSE,currentCourse)
            startActivity(destIntent)
        }
    }

    fun displayDetails()
    {
        binding.txtCourseName.text=currentCourse.name
        binding.txtCourseCategory.text=currentCourse.category
        binding.txtFaculty.text=currentCourse.takenby
        binding.txtSchedule.text="${currentCourse.startdate} - ${currentCourse.enddate}"
        binding.txtCourseDesc.text=currentCourse.description
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this,R.layout.listitem,currentCourse.modules!!)
        binding.lstModuleList.adapter=adapter;
        Picasso.get().load(currentCourse.courseimage).error(R.drawable.ic_user_placeholder).into(binding.imgCourseImage)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarEnrolledCourseDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarEnrolledCourseDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun courseDetailsSuccess(course: Course) {
        hideProgressDialog()
        currentCourse=course


    }

    fun allDetailsUpdatedSuccessfully() {
        hideProgressDialog()
        showErrorSnackBar("Course Un-Ennrolled!!", false)
        binding.btnUnEnroll.setText("UnEnrolled")
        binding.btnUnEnroll.isEnabled=false
        val disabledColor =
            ContextCompat.getColor(applicationContext, R.color.button_disabled_color)
        binding.btnUnEnroll.setBackgroundColor(disabledColor)
        pushNotification()
    }

    private fun pushNotification()
    {
        val topicName_Student="Student_${currentCourse.id}"
        NotificationUtils.unSubscribeToTopic(topicName_Student)

        val topicName_Faculty="Faculty_${currentCourse.id}"
        NotificationUtils.sendMessage("Un-Enrollment","Un-enrolled to course '${currentCourse.name}'",topicName_Faculty)

        val sharedPreferences =getSharedPreferences(Constants.MYLMSAPP_PREFERENCES, Context.MODE_PRIVATE)
        val token=sharedPreferences.getString(Constants.APP_TOKEN,null);
        NotificationUtils.sendMessage("Un-Enrollment","Un-enrolled to course '${currentCourse.name}'",token!!)
    }
}