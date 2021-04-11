package com.srs.lmpapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.FragmentFacultyMyCoursesBinding
import com.srs.lmpapp.databinding.FragmentMycoursesBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.ui.adapters.MyCourseListAdapter
import com.srs.lmpapp.utils.Constants


// TODO Step 2: Rename the NotificationsFragment as OrdersFragment as well rename the xml files accordingly.
class FacultyMyCoursesFragment : BaseFragment() {
    lateinit var recyclerCourseSessions: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: MyCourseListAdapter
    private var _binding: FragmentFacultyMyCoursesBinding? = null
    private val binding get() = _binding!!
    lateinit var txtNumOfCourses: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFacultyMyCoursesBinding.inflate(inflater, container, false)
        val view = binding.root
        recyclerCourseSessions= view.findViewById(R.id.recyclerCourseSessions)
        txtNumOfCourses= view.findViewById(R.id.txtNumOfCourses)

        showProgressDialog("Please Wait..")
        FirestoreClass().getCoursesListForFaculty(this@FacultyMyCoursesFragment)
        return view
    }

    companion object {

        fun newInstance() =
            MyCoursesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    fun successCoursesListFromFireStore(enrolledCourseList:ArrayList<Course>)
    {
        hideProgressDialog()
        layoutManager = LinearLayoutManager(activity)
        val sharedPreferences =
            activity?.getSharedPreferences(Constants.MYLMSAPP_PREFERENCES, Context.MODE_PRIVATE)


        val userType= sharedPreferences?.getString(Constants.LOGGED_IN_USERTYPE,"")
        if(userType!=null) {
            recyclerAdapter = MyCourseListAdapter(activity as Context, enrolledCourseList, userType)
            recyclerCourseSessions.adapter = recyclerAdapter
            recyclerCourseSessions.layoutManager = layoutManager
            txtNumOfCourses.text = "${recyclerAdapter.itemCount} course(s) found"
        }
    }
}