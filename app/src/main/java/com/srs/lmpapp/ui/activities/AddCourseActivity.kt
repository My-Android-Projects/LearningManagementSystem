package com.srs.lmpapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityAddCourseBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.GlideLoader
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : BaseActivity(),View.OnClickListener {
    private var mCourseImageURL: String = ""
    private var mSelectedImageFileUri: Uri? = null

    private lateinit var modules : List<String>
    private lateinit var binding:ActivityAddCourseBinding
    private  var currentCourse:Course=Course()
    private val ADD_MODULE_ACTIVITY_REQUEST_CODE = 200
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        val categoryItems = getResources().getStringArray(
            R.array.category
        );
        val category_adapter = ArrayAdapter(
            this@AddCourseActivity,
            R.layout.drop_down_item,
            categoryItems
        )
        (binding.lstCategory as? AutoCompleteTextView)?.setAdapter(category_adapter)


        val credits_items =  getResources().getStringArray(
            R.array.credits
        );
        val credits_adapter = ArrayAdapter(
            this@AddCourseActivity,
            R.layout.drop_down_item,
            credits_items
        )
        (binding.lstCredits as? AutoCompleteTextView)?.setAdapter(credits_adapter)


        binding.txtStartDate.setOnClickListener(this)
        binding.txtEndDate.setOnClickListener(this)
        binding.ivAddUpdateCourse.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.tvViewModules.setOnClickListener(this)
    }
    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_course -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddCourseActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.tv_viewModules-> {
                    var destIntent = Intent(this@AddCourseActivity, AddModuleActivity::class.java)
                    destIntent.putExtra(
                        com.srs.lmpapp.utils.Constants.CURRENT_COURSE,
                        currentCourse
                    )

                    startActivityForResult(destIntent, ADD_MODULE_ACTIVITY_REQUEST_CODE)
                }
                R.id.btn_submit -> {
                    if (validateCourseDetails()) {
                        uploadCourseImage()
                    }
                }
                R.id.txtStartDate-> {
                    val constraintsBuilder = CalendarConstraints.Builder().setValidator(
                        DateValidatorPointForward.now())
                    val materialStartDateBuilder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(
                        constraintsBuilder.build())
                    materialStartDateBuilder.setTitleText("Select Course Start Date");
                    val materialStartDatePicker  =  materialStartDateBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

                    materialStartDatePicker.show(supportFragmentManager, materialStartDatePicker.toString())
                    materialStartDatePicker.addOnPositiveButtonClickListener {
                        val timeZoneUTC = TimeZone.getDefault()
                        val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
                        val simpleFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                        val date = Date(it + offsetFromUTC)
                        binding.txtStartDate.setText(simpleFormat.format(date))
                    }

                }
                R.id.txtEndDate-> {

                    val constraintsBuilder = CalendarConstraints.Builder().setValidator(
                        DateValidatorPointForward.now())
                    val materialEndDateBuilder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(
                        constraintsBuilder.build())
                    materialEndDateBuilder.setTitleText("Select Course End Date");
                    val materialEndDatePicker  =  materialEndDateBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

                    materialEndDatePicker.show(supportFragmentManager, materialEndDatePicker.toString())
                    materialEndDatePicker.addOnPositiveButtonClickListener {
                        val timeZoneUTC = TimeZone.getDefault()
                        val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
                        val simpleFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                        val date = Date(it + offsetFromUTC)
                        binding.txtEndDate.setText(simpleFormat.format(date))
                    }

                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddCourseActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            binding.ivAddUpdateCourse.setImageDrawable(
                ContextCompat.getDrawable(this@AddCourseActivity, R.drawable.ic_vector_edit)
            )
            mSelectedImageFileUri = data.data!!
            try {
                // Load the product image in the ImageView.
                GlideLoader(this@AddCourseActivity).loadCoursePicture(
                    mSelectedImageFileUri!!,
                    binding.ivCourseImage
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        if (requestCode == ADD_MODULE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //currentCourse = data?.getParcelableExtra(Constants.CURRENT_COURSE)!!
                 currentCourse = data?.getParcelableExtra(Constants.CURRENT_COURSE)!!

            }
        }
    }

    /**
     * A function for actionBar Setup.
     */    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddCourseActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddCourseActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to validate the product details.
     */
    private fun validateCourseDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
               // showErrorSnackBar("select course image", true)

                //false
                true
            }

            TextUtils.isEmpty(binding.txtCourseName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("course name empty", true)
                false
            }

            TextUtils.isEmpty(binding.lstCategory.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("course category empty", true)
                false
            }

            TextUtils.isEmpty(binding.lstCredits.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("credit is empty", true)
                false
            }

            TextUtils.isEmpty(binding.txtCourseDesc.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("course Description empty", true)
                false
            }


            TextUtils.isEmpty(binding.txtTotSeats.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("tot.seats empty", true)
                false
            }
            TextUtils.isEmpty(binding.txtStartDate.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("start date empty", true)
                false
            }


            TextUtils.isEmpty(binding.txtEndDate.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    "end date empty",
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }
    fun courseUploadSuccess() {
        hideProgressDialog()
        Toast.makeText(this@AddCourseActivity, "Uploaded Course successfully", Toast.LENGTH_SHORT).show()


    }
    fun imageUploadSuccess(imageURL: String)
    {

        mCourseImageURL = imageURL
        uploadCourseDetails()
    }
    private fun uploadCourseImage()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(
            this@AddCourseActivity,
            mSelectedImageFileUri,
            Constants.COURSE_IMAGE
        )


    }
    private fun uploadCourseDetails()
    {
        // Here we get the text from editText and trim the space
        /* currentCourse = Course(

            binding.txtCourseName.text.toString().trim { it <= ' ' }, //course name
            binding.lstCategory.text.toString().trim { it <= ' ' }, //course category
            binding.lstCredits.text.toString().trim { it <= ' ' }.toLong(), //course credits
            FirestoreClass().getCurrentUserID(), //faculty doc id
            binding.txtTotSeats.text.toString().trim { it <= ' ' }.toLong(),
            binding.txtTotSeats.text.toString().trim { it <= ' ' }.toLong(),
            binding.txtStartDate.text.toString().trim { it <= ' ' },
            binding.txtEndDate.text.toString().trim { it <= ' ' },
            mCourseImageURL,
            "",
            listOf(),
            binding.txtCourseDesc.text.toString().trim { it <= ' ' },
            modules
        )*/
        currentCourse.name=binding.txtCourseName.text.toString().trim { it <= ' ' }
        currentCourse.description=binding.txtCourseDesc.text.toString().trim { it <= ' ' }
        currentCourse.category=binding.lstCategory.text.toString().trim { it <= ' ' }
        currentCourse.credits=binding.lstCredits.text.toString().trim { it <= ' ' }.toLong()
        currentCourse.takenby=FirestoreClass().getCurrentUserID()
        currentCourse.totseats=binding.txtTotSeats.text.toString().trim { it <= ' ' }.toLong()
        currentCourse.remainingseats=binding.txtTotSeats.text.toString().trim { it <= ' ' }.toLong()
        currentCourse.startdate=binding.txtStartDate.text.toString().trim { it <= ' ' }
        currentCourse.enddate=binding.txtEndDate.text.toString().trim { it <= ' ' }
        currentCourse.enrolledby=listOf()
        currentCourse.id=""
        //currentCourse.modules=modules
        currentCourse.courseimage= mCourseImageURL



        FirestoreClass().uploadCourseDetails(this@AddCourseActivity, currentCourse)
    }
}