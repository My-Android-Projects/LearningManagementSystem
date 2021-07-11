package com.srs.lmpapp.ui.activities


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import com.srs.lmpapp.R

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

import com.srs.lmpapp.databinding.ActivityLoginBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.User
import com.srs.lmpapp.utils.Constants

private const val CURRENT_USER = "currentUser"
private const val TAG="LOGINACTIVITY"
@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    private lateinit var binding: ActivityLoginBinding
    private  var currentUser:User?=null
    lateinit var destIntent:Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.tvRegister.setOnClickListener {

            // Launch the register screen when the user clicks on the text.
            // validateRegisterDetails()
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)

        }

        binding.btnLogin.setOnClickListener()
        {

            logInRegisteredUser()
        }
        binding.tvForgotPassword.setOnClickListener()
        {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val sharedPreferences =getSharedPreferences(Constants.MYLMSAPP_PREFERENCES,  Context.MODE_PRIVATE )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(Constants.APP_TOKEN,token)
            editor.apply()
            // Log and toast
            val msg =  token!!
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }


    // TODO Step 6: Create an function to validate the register account fields.
    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty( binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {

                true
            }
        }
    }
    private fun logInRegisteredUser()
    {
        if(validateLoginDetails())
        {
            showProgressDialog(resources.getString(R.string.please_wait))
            val email=binding.etEmail.text.toString().trim { it <= ' ' }
            val password=binding.etPassword.text.toString().trim { it <= ' ' }
            var id: Int = binding.rdUserType.checkedRadioButtonId
            val selectedType: RadioButton = findViewById(id)
            var userType = selectedType.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>
                { task ->
                  hideProgressDialog()
                    if (task.isSuccessful) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FirestoreClass().getSnopshotDetails(this@LoginActivity)
                    }else
                        showErrorSnackBar(task.exception!!.message.toString(), true)


                }
            )
        }

    }

    /*fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()
        currentUser=user
        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        // Redirect the user to Main Screen after log in.
        val userType=user.type
       /* if(user.profileCompleted==0)
        {
            destIntent = Intent(this@LoginActivity,UserProfileActivity::class.java)
        }
        else
        {
            if (userType.equals("Student", true))
                destIntent = Intent(this@LoginActivity, StudentDashboardActivity::class.java)
            else
                destIntent = Intent(this@LoginActivity, FacultyHomeActivity::class.java)
        }*/
        if (userType.equals("Student", true))
            destIntent = Intent(this@LoginActivity, StudentDashboardActivity::class.java)
        else
            destIntent = Intent(this@LoginActivity, AddCourseActivity::class.java)

        destIntent.putExtra(Constants.EXTRA_USER_DETAILS,user)

        startActivity(destIntent)
        finish()
    }
   */

    fun userLoggedInSuccess(user: User,totCredits:Int,totCourses:Int) {

        // Hide the progress dialog.
        hideProgressDialog()
        currentUser=user
        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)
        Log.i("totCredits: ", totCredits.toString())
        Log.i("totCourses: ", totCourses.toString())
        // Redirect the user to Main Screen after log in.
        val userType=user.type
        val sharedPreferences =
            getSharedPreferences(
                Constants.MYLMSAPP_PREFERENCES,
                Context.MODE_PRIVATE
            )

        // Create an instance of the editor which is help us to edit the SharedPreference.
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(
            Constants.LOGGED_IN_USERNAME,
            "${user.firstName} ${user.lastName}"
        )
        editor.putString(
            Constants.LOGGED_IN_USER_EMAIL,
            "${user.email}"
        )
        editor.putString(
            Constants.LOGGED_IN_USER_PHONE,
            " ${user.mobile}"
        )

        editor.putString(
            Constants.LOGGED_IN_USERID,
            "${user.id}"
        )
        editor.putString(
            Constants.LOGGED_IN_USERTYPE,
            "${user.type}"
        )
        editor.apply()
        if(user.profileCompleted==0)
         {
             destIntent = Intent(this@LoginActivity,UserProfileActivity::class.java)
         }
         else
         {
             if (userType.equals("Student", true))
                 destIntent = Intent(this@LoginActivity, StudentDashboardActivity::class.java)
             else
                 destIntent = Intent(this@LoginActivity, FacultyDashboardActivity::class.java)
         }
        /*if (userType.equals("Student", true))
            destIntent = Intent(this@LoginActivity, StudentDashboardActivity::class.java)
        else
            destIntent = Intent(this@LoginActivity, AddCourseActivity::class.java)
*/
        destIntent.putExtra(Constants.EXTRA_USER_DETAILS,user)
        destIntent.putExtra(Constants.TOT_COURSES,totCourses)
        destIntent.putExtra(Constants.TOT_CREDITS,totCredits)
        startActivity(destIntent)
        finish()
    }






}