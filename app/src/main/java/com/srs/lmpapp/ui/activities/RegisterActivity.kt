package com.srs.lmpapp.ui.activities



import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityRegisterBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.User


// TODO Step 1: Create an RegisterActivity of the application.
// START
@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()


        binding.tvLogin.setOnClickListener()
        {
            onBackPressed()
        }
        binding.btnRegister.setOnClickListener()
        {
            if(validateRegisterDetails())
            {
                registerUser()
            }
        }
    }
    // TODO Step 1: Create a function to set up an action bar.
    // START
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }
    // END

    // TODO Step 6: Create an function to validate the register account fields.
    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            binding.etPassword.text.toString().trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !binding.cbTermsAndCondition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {

                true
            }
        }
    }
    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim { it <= ' ' }
            var id: Int = binding.rdUserType.checkedRadioButtonId
            val selectedType: RadioButton = findViewById(id)
            var userType = selectedType.text.toString()
            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // TODO Step 9: Remove the hide progress dialog.
                        // START
                        /*// Hide the progress dialog
                        hideProgressDialog()*/
                        // END

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            // TODO Step 2: Create an instance of the user data model class. And, pass the values in the constructor.
                            // Here we have passed only four values in the constructor as there are only four values at registration. So, instead of giving it blank or default.
                            // We have already added the default values in the data model class itself. Make sure the passing value order is correct.
                            // START
                            // Instance of User data model class.
                            val user = User(
                                firebaseUser.uid,
                                userType,
                                binding.etFirstName.text.toString().trim { it <= ' ' },
                                binding.etLastName.text.toString().trim { it <= ' ' },
                                binding.etEmail.text.toString().trim { it <= ' ' }
                            )
                            // END

                            // TODO Step 4: Move the success message and the Sign out piece of code to the success function.
                            // START
                            /*Toast.makeText(
                                this@RegisterActivity,
                                resources.getString(R.string.register_success),
                                Toast.LENGTH_SHORT
                            ).show()


                            *//**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Intro Screen for Sign-In
                             *//*
                            FirebaseAuth.getInstance().signOut()
                            // Finish the Register Screen
                            finish()*/
                            // END

                            // TODO Step 8: Call the function of FirestoreClass to make an entry in the Cloud Firestore of registered user.
                            // START
                            // Pass the required values in the constructor.
                            FirestoreClass().registerUser(this@RegisterActivity, user)
                            // END

                        } else {

                            // TODO Step 10: Hide the progress dialog when the task is unsuccessful.
                            // START
                            // Hide the progress dialog
                            hideProgressDialog()
                            // END

                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    // TODO Step 3: Create a function to notify the success result of Firestore entry.
    // START
    /**
     * A function to notify the success result of Firestore entry when the user is registered successfully.
     */
    fun userRegistrationSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        // TODO Step 5: Replace the success message to the Toast instead of Snackbar.
        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()


        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        finish()
    }
    // END
}
// END