package com.srs.lmpapp.ui.activities


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.RadioButton
import com.srs.lmpapp.R

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

import com.srs.lmpapp.databinding.ActivityLoginBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.User
import com.srs.lmpapp.utils.Constants


@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    private lateinit var binding: ActivityLoginBinding

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
        if(user.profileCompleted==0)
        {
            destIntent = Intent(this@LoginActivity, UserProfileActivity::class.java)
        }
        else
        {
            if (userType.equals("Student", true))
                destIntent = Intent(this@LoginActivity, StudentHomeActivity::class.java)
            else
                destIntent = Intent(this@LoginActivity, FacultyHomeActivity::class.java)
        }
        destIntent.putExtra(Constants.EXTRA_USER_DETAILS,user)

        startActivity(destIntent)
        finish()
    }
}