package com.srs.lmpapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.FragmentDashboardBinding
import com.srs.lmpapp.databinding.FragmentFacultyAddCoursesBinding
import com.srs.lmpapp.databinding.FragmentFacultyMyCoursesBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course

import com.srs.lmpapp.ui.activities.AddCourseActivity
import com.srs.lmpapp.ui.adapters.MyCourseListAdapter

class FacultyAddCourses : BaseFragment() {

    private lateinit var mRootView: View
    private var _binding: FragmentFacultyAddCoursesBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFacultyAddCoursesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_course_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_course) {
            startActivity(Intent(activity, AddCourseActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        getMyCourseListFromFireStore()
    }

    private fun getMyCourseListFromFireStore() {
        showProgressDialog("Please Wait...")
        FirestoreClass().getCoursesListForFaculty(this@FacultyAddCourses)
    }


    fun successCoursesListFromFireStore(productsList: ArrayList<Course>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (productsList.size > 0) {
            binding.rvMyProductItems.visibility = View.VISIBLE
            binding.tvNoCoursesFound.visibility = View.GONE

            binding.rvMyProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyProductItems.setHasFixedSize(true)

            val adapterProducts =
                MyCourseListAdapter(requireActivity(), productsList, "Faculty")
            binding.rvMyProductItems.adapter = adapterProducts
        } else {
            binding.rvMyProductItems.visibility = View.GONE
            binding.rvMyProductItems.visibility = View.VISIBLE
        }
    }


    fun deleteProduct(courseID: String) {

        showAlertDialogToDeleteCourse(courseID)
    }


    fun courseDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            "Course Deleted!!!",
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getMyCourseListFromFireStore()
    }

    private fun showAlertDialogToDeleteCourse(courseID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteCourse(this@FacultyAddCourses, courseID)
            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    // END
}