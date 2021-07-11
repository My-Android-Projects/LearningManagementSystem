package com.srs.lmpapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityAddAssignmentBinding
import com.srs.lmpapp.databinding.ActivityFacultyCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Assignment
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.model.User
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.NotificationUtils

class AddAssignmentActivity : BaseActivity() {
    private lateinit var binding: ActivityAddAssignmentBinding
    private lateinit var currentCourse: Course
    private  var currentAssignment: Assignment =Assignment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        setupActionBar()
         binding.btnSubmit.setOnClickListener()
         {
             addAssignment()

         }
    }
    fun addAssignment()
    {
        currentAssignment.name=binding.txtAssignmentName.text.toString().trim { it <= ' ' }
        currentAssignment.id=""
        currentAssignment.description=binding.txtAssignmentDesc.text.toString().trim { it <= ' ' }
        currentAssignment.enddate=binding.txtAssignmentEndDate.text.toString().trim { it <= ' ' }
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addAssignment(this@AddAssignmentActivity,currentAssignment,currentCourse.id!!)
    }
    fun assignmentAddSuccess(assignmentInfo:Assignment)
    {
        currentAssignment=assignmentInfo
        FirestoreClass().createAssignmentInstance(this@AddAssignmentActivity,currentAssignment,currentCourse)



    }
    fun createAssignmentInstanceSuccess()
    {

        hideProgressDialog()
        Toast.makeText(this@AddAssignmentActivity, "Uploaded Assignment successfully", Toast.LENGTH_SHORT).show()
        pushNotification()

    }
    private fun pushNotification()
    {
        val topicName_Student="Student_${currentCourse.id}"
        NotificationUtils.sendMessage("Assignment","Assignment created for course'${currentCourse.name}'",topicName_Student)

        val sharedPreferences =getSharedPreferences(Constants.MYLMSAPP_PREFERENCES, Context.MODE_PRIVATE)
        val token=sharedPreferences.getString(Constants.APP_TOKEN,null);
        NotificationUtils.sendMessage("Assignment","Assignment created for course'${currentCourse.name}'",token!!)
        this.onBackPressed()
        finish()

    }
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddAssignmentActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddAssignmentActivity.setNavigationOnClickListener { onBackPressed() }
    }

}