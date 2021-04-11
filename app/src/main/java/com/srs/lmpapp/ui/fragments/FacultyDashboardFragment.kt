package com.srs.lmpapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.FragmentDashboardBinding
import com.srs.lmpapp.databinding.FragmentFacultyDashboardBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.User
import com.srs.lmpapp.ui.activities.LoginActivity

class FacultyDashboardFragment : BaseFragment() {
    private var _binding: FragmentFacultyDashboardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFacultyDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)
        showProgressDialog("Please Wait..")
        FirestoreClass().getSnopshotDetails(this@FacultyDashboardFragment)
        return view


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.btn_logout -> {


                // Logout from app.
                FirebaseAuth.getInstance().signOut()

                startActivity(Intent(activity, LoginActivity::class.java))



            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getUserDetailsSuccess(user:User,totcredits:Int,totcourses:Int)
    {
        hideProgressDialog()
        binding.tvName.setText("${user?.firstName}   ${user?.lastName}")
        binding.tvEmail.setText(user?.email)
        binding.tvMobileNumber.setText(user?.mobile.toString())
        binding.tvUserType.setText("Type: ${user.type}")
        binding.tvCourseEnrolled.setText("Courses Enrolled:${totcourses}")
        binding.tvGender.setText("Gender: ${user?.gender}")
        binding.tvTotCredits.setText("Total Credits: ${totcredits}")
       // Picasso.get().load(user.image).error(R.drawable.ic_user_placeholder).into(binding.ivUserPhoto)


    }
}