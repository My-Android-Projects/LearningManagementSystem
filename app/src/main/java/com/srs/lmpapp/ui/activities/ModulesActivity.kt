package com.srs.lmpapp.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityModulesBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.model.Module
import com.srs.lmpapp.ui.adapters.ModuleListAdapter
import com.srs.lmpapp.ui.adapters.MyCourseListAdapter
import com.srs.lmpapp.utils.Constants

class ModulesActivity : BaseActivity() {
     lateinit var binding:ActivityModulesBinding
    lateinit var recyclerModuleSession: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: ModuleListAdapter
      var modules:ArrayList<String> = ArrayList()
     private val ADD_MODULE_ACTIVITY_REQUEST_CODE=201
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityModulesBinding.inflate(layoutInflater)
        modules = intent.getStringArrayListExtra(Constants.MODULES)!!

        setContentView(binding.root)
        recyclerModuleSession= binding.recyclerModuleList
        layoutManager = LinearLayoutManager(this@ModulesActivity)
        setupActionBar()
        if(modules.size!=0) {
            showProgressDialog("Please Wait...")
            FirestoreClass().getModuleListFromFireStore(this@ModulesActivity, modules)
        }
    }
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarModulesActivity)
       // val actionBar = supportActionBar
        binding.btnModuleBack.setOnClickListener {

                /*val intent = Intent()
                intent.putExtra(Constants.MODULES, modules.toTypedArray())
                setResult(RESULT_OK, intent)
                */
            onBackPressed()
        }

        binding.btnAddModule.setOnClickListener{
            var destIntent = Intent(this@ModulesActivity, AddModuleActivity::class.java)
            startActivityForResult(destIntent,ADD_MODULE_ACTIVITY_REQUEST_CODE)
        }



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MODULE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val moduleId:String = data?.getStringExtra("MODULE")!!
                modules.add(moduleId)
            }
        }
    }
    fun deleteModule(moduleId:String)
    {
        FirestoreClass().deleteModule(this@ModulesActivity,moduleId)
    }
    override fun onResume() {
        if(modules.size!=0) {
            showProgressDialog("Please Wait...")
            FirestoreClass().getModuleListFromFireStore(this@ModulesActivity, modules)
        }
        super.onResume()
    }
    fun moduleDeleteSuccess()
    {
        hideProgressDialog()
    }

    fun successModuleListFromFireStore(moduleList:ArrayList<Module>)
    {
        hideProgressDialog()

        layoutManager = LinearLayoutManager(this@ModulesActivity)
        recyclerAdapter = ModuleListAdapter(this@ModulesActivity ,moduleList)
        recyclerModuleSession.adapter = recyclerAdapter
        recyclerModuleSession.layoutManager = layoutManager
    }
}