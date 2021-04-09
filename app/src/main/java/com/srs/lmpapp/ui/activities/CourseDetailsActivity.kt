package com.srs.lmpapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course

class CourseDetailsActivity : BaseActivity() {
    private lateinit var binding:ActivityCourseDetailsBinding
    private lateinit var currentCourse:Course
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
       val currentCourseId:String?=  intent.getStringExtra("currentCourseId")
        if(currentCourseId==null)
        {
            binding.btnEnrollOrUnEnroll.isEnabled=false
            showErrorSnackBar("CourseID is not pasesd", true)
        }
        else
        {
            showProgressDialog("Please Wait...")
            FirestoreClass().getCourseDetails(this@CourseDetailsActivity,currentCourseId)
        }
        binding.btnEnrollOrUnEnroll.setOnClickListener()
        {

                if(currentCourse?.id!=null && binding.btnEnrollOrUnEnroll.isEnabled) {
                    showProgressDialog("Please Wait...")
                    FirestoreClass().enrollCourse(this@CourseDetailsActivity, currentCourse.id)
                }
        }
    }
    fun courseDetailsSuccess(course: Course)
    {
        hideProgressDialog()
        currentCourse=course
        println(course.id)
        println(course.name)
        binding.btnEnrollOrUnEnroll.setText("Enroll")
        binding.txtCourseName.text=currentCourse.name
        binding.txtCourseCategory.text=currentCourse.category
        binding.txtFaculty.text=currentCourse.takenby
        binding.txtSchedule.text="${currentCourse.startdate} - ${currentCourse.enddate}"
        Picasso.get().load(currentCourse.courseimage).error(R.drawable.ic_user_placeholder).into(binding.imgCourseImage)
    }

    fun allDetailsUpdatedSuccessfully()
    {
        hideProgressDialog()
        showErrorSnackBar("Course Enrolled", false)
        binding.btnEnrollOrUnEnroll.setText("Enrolled")
        binding.btnEnrollOrUnEnroll.isEnabled=false
    }
}