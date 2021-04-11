package com.srs.lmpapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_student_details)
        currentCourse = intent.getParcelableExtra(Constants.CURRENT_COURSE)!!
        recyclerStudentDetails= findViewById(R.id.recyclerStudentDetails)
        layoutManager = LinearLayoutManager(this@ViewStudentDetailsActivity)
       //studentsList: List<String> =currentCourse.enrolledby!!
        showProgressDialog("Please Wait..")
        FirestoreClass().getStudentDetails(this@ViewStudentDetailsActivity,currentCourse.id)

    }
    fun successStudentList(studentList:List<User>)
    {
        hideProgressDialog()
        recyclerAdapter = StudentListAdapter(this@ViewStudentDetailsActivity as Context , studentList)
        recyclerStudentDetails.adapter = recyclerAdapter
       recyclerStudentDetails.layoutManager = layoutManager


    }
}