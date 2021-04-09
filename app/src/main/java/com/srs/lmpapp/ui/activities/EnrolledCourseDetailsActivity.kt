package com.srs.lmpapp.ui.activities

import android.os.Bundle
import com.squareup.picasso.Picasso
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course

class EnrolledCourseDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityCourseDetailsBinding
    private lateinit var currentCourse:Course
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentCourseId:String?=  intent.getStringExtra("currentCourseId")
        if(currentCourseId==null)
        {
            binding.btnEnrollOrUnEnroll.isEnabled=false
            showErrorSnackBar("CourseID is not found", true)
        }
        else {
            showProgressDialog("Please Wait...")
            FirestoreClass().getCourseDetails(this@EnrolledCourseDetailsActivity, currentCourseId)
        }
        binding.btnEnrollOrUnEnroll.setOnClickListener()
        {
           if(currentCourse?.id!=null && binding.btnEnrollOrUnEnroll.isEnabled) {
               showProgressDialog("Please Wait...")
               FirestoreClass().unEenrollCourse(
                   this@EnrolledCourseDetailsActivity,
                   currentCourse.id
               )
           }
        }
    }

    fun courseDetailsSuccess(course: Course) {
        hideProgressDialog()
        currentCourse=course
        println(currentCourse.id)
        println(currentCourse.name)
        binding.txtCourseName.text=currentCourse.name
        binding.txtCourseCategory.text=currentCourse.category
        binding.txtFaculty.text=currentCourse.takenby
        binding.txtSchedule.text="${currentCourse.startdate} - ${currentCourse.enddate}"
        binding.btnEnrollOrUnEnroll.setText("Un-Enroll")
        Picasso.get().load(currentCourse.courseimage).error(R.drawable.ic_user_placeholder).into(binding.imgCourseImage)

        binding.btnEnrollOrUnEnroll.isEnabled=true
    }

    fun allDetailsUpdatedSuccessfully() {
        hideProgressDialog()
        showErrorSnackBar("Course Un-Ennrolled!!", false)
        binding.btnEnrollOrUnEnroll.setText("Enroll")
        binding.btnEnrollOrUnEnroll.isEnabled=false
    }
}