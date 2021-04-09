package com.srs.lmpapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.ui.adapters.CourseSearchResultAdapter


class CourseSearchResultFragment : BaseFragment() {

    lateinit var filter:HashMap<String,Any>
    lateinit var recyclerCourseSessions: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: CourseSearchResultAdapter
    lateinit var txtNumOfCourses: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_course_search_result, container, false)
        recyclerCourseSessions= view.findViewById(R.id.recyclerCourseSessions)

        txtNumOfCourses= view.findViewById(R.id.txtNumOfCourses)

        showProgressDialog("Please Wait..")
        filter=HashMap()
        FirestoreClass().getCoursesList(this@CourseSearchResultFragment, filter);
        return view
    }
    companion object {

        fun newInstance() =
                CourseSearchResultFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    fun successCoursesListFromFireStore(courselist:List<Course>)
    {
        hideProgressDialog()

        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = CourseSearchResultAdapter(activity as Context, courselist)
        recyclerCourseSessions.adapter = recyclerAdapter
        recyclerCourseSessions.layoutManager = layoutManager
        txtNumOfCourses.text="${recyclerAdapter.itemCount} course(s) found"

    }

}