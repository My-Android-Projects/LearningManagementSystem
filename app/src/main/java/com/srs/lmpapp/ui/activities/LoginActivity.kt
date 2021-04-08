package com.srs.lmpapp.ui.activities


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.RadioButton
import com.srs.lmpapp.R
import com.srs.lmpapp.utils.MSPButton
import com.srs.lmpapp.utils.MSPTextViewBold
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.User
import com.srs.lmpapp.utils.MSPTextView

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */

    lateinit var tv_register: MSPTextViewBold
    lateinit var btn_login: MSPButton
    lateinit var tv_forgot_password: MSPTextView
    lateinit var destIntent:Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_login)

        // This is used to hide the status bar and make the login screen as a full screen activity.
        // It is deprecated in the API level 30. I will update you with the alternate solution soon.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )



        // TODO Step 7: Assign a onclick event to the register text to launch the register activity.
        // START
        tv_register=findViewById(R.id.tv_register)
        tv_register.setOnClickListener {

            // Launch the register screen when the user clicks on the text.
            // validateRegisterDetails()
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)

        }
        btn_login=findViewById(R.id.btn_login)
        btn_login.setOnClickListener()
        {

            logInRegisteredUser()
        }
        tv_forgot_password=findViewById(R.id.tv_forgot_password)
        tv_forgot_password.setOnClickListener()
        {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    // TODO Step 6: Create an function to validate the register account fields.
    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
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
            val email=et_email.text.toString().trim { it <= ' ' }
            val password=et_password.text.toString().trim { it <= ' ' }
            var id: Int = rd_userType.checkedRadioButtonId
            val selectedType: RadioButton = findViewById(id)
            var userType = selectedType.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>
                { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        /*val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser

                        showErrorSnackBar("You are logged in successfully.", false)

                        if(userType.equals("Student",true))
                            destIntent=Intent(this@LoginActivity, StudentHomeActivity::class.java)
                        else
                            destIntent=Intent(this@LoginActivity, FacultyHomeActivity::class.java)
                        destIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        destIntent.putExtra("user_id",firebaseUser.uid)
                        destIntent.putExtra("email_id",email)
                        destIntent.putExtra("userType",userType)

                        startActivity(destIntent)
                        finish()
                        */
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    }
                    else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)

                    }
                }
            )
        }

    }
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        // Redirect the user to Main Screen after log in.
        val userType=user.type
        if(userType.equals("Student",true))
            destIntent=Intent(this@LoginActivity, StudentHomeActivity::class.java)
        else
            destIntent=Intent(this@LoginActivity, FacultyHomeActivity::class.java)

        destIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(destIntent)
        finish()
    }
}