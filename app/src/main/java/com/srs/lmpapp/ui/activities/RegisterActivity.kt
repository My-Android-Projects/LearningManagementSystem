package com.srs.lmpapp.ui.activities



import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast

import com.srs.lmpapp.utils.MSPButton
import com.srs.lmpapp.utils.MSPTextViewBold
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.srs.lmpapp.R
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.User


// TODO Step 1: Create an RegisterActivity of the application.
// START
@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    lateinit var tv_login: MSPTextViewBold
    lateinit var  btn_register: MSPButton
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_register)

        // TODO Step 3: Hide the status bar for the LoginActivity to make it full screen activity.
        // START
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        // It is deprecated in the API level 30. I will update you with the alternate solution soon.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        tv_login=findViewById(R.id.tv_login)
        tv_login.setOnClickListener()
        {
            onBackPressed()
        }
        btn_register=findViewById(R.id.btn_register)
        btn_register.setOnClickListener()
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

        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }
    // END

    // TODO Step 6: Create an function to validate the register account fields.
    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
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

            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }
            var id: Int = rd_userType.checkedRadioButtonId
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
                                et_first_name.text.toString().trim { it <= ' ' },
                                et_last_name.text.toString().trim { it <= ' ' },
                                et_email.text.toString().trim { it <= ' ' }
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