package com.srs.lmpapp.ui.fragments
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.srs.lmpapp.R
import com.srs.lmpapp.`interface`.CourseSearchDataCommunicator
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CourseSearchFragment : BaseFragment() {
    val REQUEST_CODE = 11 // Used to identify the result
    lateinit var filter:HashMap<String,Any>
    lateinit var btnCourseSearch: Button
    lateinit var txtStartDate: EditText
    lateinit var txtEndDate: EditText
    lateinit var txtCourseName: EditText
    lateinit var lstCourseCategory: Spinner
    lateinit var lstCourseCredits: Spinner
    lateinit var courseSearchDataCommunicator: CourseSearchDataCommunicator
    lateinit var txtCourseCategory:String
    lateinit var txtCourseCredits:String

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val  view= inflater.inflate(R.layout.fragment_coursesearch, container, false)
        btnCourseSearch=view.findViewById(R.id.btn_Search)
        txtStartDate=view.findViewById(R.id.txtStartDate)
        txtEndDate=view.findViewById(R.id.txtEndDate)
        txtCourseName=view.findViewById(R.id.txtCourseName)
        lstCourseCategory=view.findViewById(R.id.lstCategory)
        lstCourseCredits=view.findViewById(R.id.lstCredits)
        courseSearchDataCommunicator=activity as CourseSearchDataCommunicator


        // lstCourseCategory?.adapter = ArrayAdapter(activity?.applicationContext, R.layout.support_simple_spinner_dropdown_item, types) as SpinnerAdapter
        lstCourseCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                txtCourseCategory = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity,txtCourseCategory, Toast.LENGTH_LONG).show()
                println(txtCourseCategory)
            }

        }

        lstCourseCredits?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                txtCourseCredits = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity,txtCourseCredits, Toast.LENGTH_LONG).show()
                println(txtCourseCredits)
            }

        }
        btnCourseSearch.setOnClickListener(){
           courseSearchDataCommunicator.sendCourseFilterData(
                txtCourseName.text.toString(),
                txtCourseCategory,
                txtCourseCredits,
                txtStartDate.text.toString(),
                txtEndDate.text.toString()
            )

            /*getFragmentManager()?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, CourseSearchResultFragment())
                    ?.addToBackStack("Course Search")
                    ?.commit()

*/



        }

        txtStartDate.setOnClickListener(View.OnClickListener { // create the datePickerFragment
            txtStartDate.showSoftInputOnFocus = false

            val newFragment: DatePickerFragment = DatePickerFragment("StartDate")
            // set the targetFragment to receive the results, specifying the request code
            newFragment.setTargetFragment(this@CourseSearchFragment, REQUEST_CODE)
            // show the datePicker
            val fm: FragmentManager = (activity as AppCompatActivity?)!!.supportFragmentManager

            newFragment.show(fm, "datePicker")
        })

        txtEndDate.setOnClickListener(View.OnClickListener { // create the datePickerFragment
            txtEndDate.showSoftInputOnFocus = false

            val newFragment: DatePickerFragment = DatePickerFragment("EndDate")
            // set the targetFragment to receive the results, specifying the request code
            newFragment.setTargetFragment(this@CourseSearchFragment, REQUEST_CODE)
            // show the datePicker
            val fm: FragmentManager = (activity as AppCompatActivity?)!!.supportFragmentManager

            newFragment.show(fm, "datePicker")
        })
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            var fieldName = data?.getStringExtra("key")
            var selectedDate =data?.getStringExtra("value")
            if(fieldName=="StartDate")
                txtStartDate.setText(selectedDate)
            if(fieldName=="EndDate")
                txtEndDate.setText(selectedDate)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment sample.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}