package com.srs.lmpapp.ui.activities


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.utils.Constants


class CourseDetailsActivity : BaseActivity() {
    private lateinit var binding:ActivityCourseDetailsBinding
    private lateinit var currentCourse:Course
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        displayDetails()
        binding.btnEnroll.setOnClickListener()
        {

                if(currentCourse?.id != null &&
                    binding.btnEnroll.isEnabled) {
                    if (binding.btnEnroll.text.equals("Enroll"))
                    {
                        showProgressDialog("Please Wait...")
                        FirestoreClass().enrollCourse(this@CourseDetailsActivity, currentCourse.id)
                    }

                }

        }
    }

    fun displayDetails()
    {

        if(currentCourse.remainingseats.toInt()==0)
        {
            binding.btnEnroll.setText("Seats Full")
            val disabledColor =
                ContextCompat.getColor(applicationContext, R.color.button_disabled_color)
            binding.btnEnroll.setBackgroundColor(disabledColor)
        }
        binding.txtCourseName.text=currentCourse.name
        binding.txtCourseCategory.text=currentCourse.category
        binding.txtFaculty.text=currentCourse.takenby
        binding.txtSchedule.text="${currentCourse.startdate} - ${currentCourse.enddate}"
        binding.txtCourseDesc.text=currentCourse.description
        Picasso.get().load(currentCourse.courseimage).error(R.drawable.ic_user_placeholder).into(
            binding.imgCourseImage)

        if(currentCourse.modules!=null) {
            val modules:Array<String> =currentCourse.modules!!.toTypedArray()
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this@CourseDetailsActivity,R.layout.listitem, modules)
            binding.lstModuleList.adapter=adapter;

        }



    }
    fun courseDetailsSuccess(course: Course)
    {
        hideProgressDialog()
        currentCourse=course

    }

    fun allDetailsUpdatedSuccessfully()
    {
        hideProgressDialog()
        showErrorSnackBar("Course Enrolled", false)
        binding.btnEnroll.setText("Enrolled")
        binding.btnEnroll.isEnabled=false
        val disabledColor =
            ContextCompat.getColor(applicationContext, R.color.button_disabled_color)
        binding.btnEnroll.setBackgroundColor(disabledColor)
    }
}