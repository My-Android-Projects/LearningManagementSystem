package com.srs.lmpapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.ui.activities.LoginActivity
import com.srs.lmpapp.ui.adapters.CourseSearchResultAdapter
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.SharedViewModel


class CourseSearchResultFragment : BaseFragment() {

    lateinit var filter:HashMap<String, Any>
    lateinit var recyclerCourseSessions: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: CourseSearchResultAdapter
    lateinit var txtNumOfCourses: TextView
    private lateinit var model: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_course_search_result, container, false)
        model = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        recyclerCourseSessions= view.findViewById(R.id.recyclerCourseSessions)

        txtNumOfCourses= view.findViewById(R.id.txtNumOfCourses)

                model.appliedFilter.observe(viewLifecycleOwner, Observer<Map<String, Any>> { filterItem ->
                    Toast.makeText(activity, "Credits -> ${filterItem.get("Credits")}}", Toast.LENGTH_LONG).show()
                    Toast.makeText(activity, "Category -> ${filterItem.get("Credits")}}", Toast.LENGTH_LONG).show()
                    showProgressDialog("Please Wait..")
                    var credits: String =filterItem.get(Constants.FILTER_COURSE_CREDITS).toString()
                    var category:String=filterItem.get(Constants.FILTER_COURSE_CATEGORY).toString()
                    FirestoreClass().getCoursesList(this@CourseSearchResultFragment,
                        credits,category
                    )
                })




        return view
    }
    companion object {

        fun newInstance() =
                CourseSearchResultFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    fun successCoursesListFromFireStore(courselist: List<Course>)
    {
        hideProgressDialog()

        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = CourseSearchResultAdapter(activity as Context, courselist)
        recyclerCourseSessions.adapter = recyclerAdapter
        recyclerCourseSessions.layoutManager = layoutManager
        txtNumOfCourses.text="${recyclerAdapter.itemCount} course(s) found"

    }

}