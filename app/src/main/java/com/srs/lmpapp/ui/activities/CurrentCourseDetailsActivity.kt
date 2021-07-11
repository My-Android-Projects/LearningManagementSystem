package com.srs.lmpapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityCurrentCourseDetailsBinding
import com.srs.lmpapp.databinding.ActivityEnrolledCourseDetailsBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Assignment
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.utils.Constants

class CurrentCourseDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityCurrentCourseDetailsBinding
    private lateinit var currentCourse: Course
    private  var assignmentList: List<Assignment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCurrentcCourseDetails(this@CurrentCourseDetailsActivity,currentCourse.id)

    }
    fun displayDetails()
    {

        val adapterModule: ArrayAdapter<String> =
            ArrayAdapter<String>(this,R.layout.listitem,currentCourse.modules!!)
        binding.lstModuleList.adapter=adapterModule;
        val assignmentNames:ArrayList<String> = ArrayList()

        for(assignment in assignmentList)
        {
            assignmentNames.add(assignment.name!!)
        }
        val adapterAssignment: ArrayAdapter<String> =
            ArrayAdapter<String>(this,R.layout.listitem,assignmentNames!!)
        binding.lstAssignmentList.adapter=adapterAssignment;
    }
    fun courseDetailsSuccess(course:Course, assignments: List<Assignment>)
    {
        this.hideProgressDialog()
        currentCourse=course
        assignmentList=assignments
        displayDetails()
    }
}