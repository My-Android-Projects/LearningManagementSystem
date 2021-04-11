package com.srs.lmpapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityAddCourseBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.model.User
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.GlideLoader
import java.io.IOException

class AddCourseActivity : BaseActivity(),View.OnClickListener {
    private var mCourseImageURL: String = ""
    private var mSelectedImageFileUri: Uri? = null


    private lateinit var binding:ActivityAddCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        // Assign the click event to iv_add_update_product image.
        binding.ivAddUpdateCourse.setOnClickListener(this)

        // Assign the click event to submit button.
        binding.btnSubmit.setOnClickListener(this)
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

                R.id.btn_submit -> {
                    if (validateCourseDetails()) {
                        uploadCourseImage()
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
                ContextCompat.getDrawable(this@AddCourseActivity,R.drawable.ic_vector_edit)
            )
            mSelectedImageFileUri = data.data!!
            try {
                // Load the product image in the ImageView.
                GlideLoader(this@AddCourseActivity).loadCoursePicture( mSelectedImageFileUri!!, binding.ivCourseImage )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * A function for actionBar Setup.
     */    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddProductActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddProductActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to validate the product details.
     */
    private fun validateCourseDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar("select course image", true)
                false
            }

            TextUtils.isEmpty(binding.txtCourseName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("course name empty", true)
                false
            }

            TextUtils.isEmpty(binding.txtCourseCategory.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("course category empty", true)
                false
            }

            TextUtils.isEmpty(binding.txtCourseCredits.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("credit is empty",true )
                false
            }

            TextUtils.isEmpty(binding.txtCourseDesc.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("course Description empty", true )
                false
            }


            TextUtils.isEmpty(binding.txtTotSeats.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("tot.seats empty",true)
                false
            }
            TextUtils.isEmpty(binding.txtStartDate.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("start date empty",true )
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
        Toast.makeText(this@AddCourseActivity,"Uploaded Course successfully",Toast.LENGTH_SHORT).show()


    }
    fun imageUploadSuccess(imageURL: String)
    {
        mCourseImageURL = imageURL
        uploadCourseDetails()
    }
    private fun uploadCourseImage()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this@AddCourseActivity, mSelectedImageFileUri,  Constants.COURSE_IMAGE)
    }
    private fun uploadCourseDetails()
    {
        // Here we get the text from editText and trim the space
        val course = Course(

            binding.txtCourseName.text.toString().trim { it <= ' ' }, //course name
            binding.txtCourseCategory.text.toString().trim { it <= ' ' }, //course category
            binding.txtCourseCredits.text.toString().trim{ it <= ' ' }.toLong(), //course credits
            FirestoreClass().getCurrentUserID(), //faculty doc id
            binding.txtTotSeats.text.toString().trim { it <= ' ' }.toLong(),
            binding.txtTotSeats.text.toString().trim { it <= ' ' }.toLong(),
            binding.txtStartDate.text.toString().trim{ it <= ' '},
            binding.txtEndDate.text.toString().trim{ it <= ' '},
            mCourseImageURL,
                "",
            listOf(),
            binding.txtCourseDesc.text.toString().trim{it <=' '},
            listOf()
        )

        FirestoreClass().uploadCourseDetails(this@AddCourseActivity, course)
    }
}