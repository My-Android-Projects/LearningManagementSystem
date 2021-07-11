package com.srs.lmpapp.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityTopicsListBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Topic
import com.srs.lmpapp.ui.adapters.ModuleListAdapter
import com.srs.lmpapp.ui.adapters.TopicListAdapter
import com.srs.lmpapp.utils.Constants

class TopicsListActivity : BaseActivity() {
    lateinit var binding:ActivityTopicsListBinding
    private val ADD_TOPIC_ACTIVITY_REQUEST_CODE=402
    lateinit var recyclerTopicSession: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: TopicListAdapter
    var  topics:ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTopicsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        topics = intent.getStringArrayListExtra(Constants.TOPICS)!!

        recyclerTopicSession= binding.recyclerTopicsList
        layoutManager = LinearLayoutManager(this@TopicsListActivity)
        setupActionBar()
        if(topics.size!=0) {
            showProgressDialog("Please Wait...")
            FirestoreClass().getTopicListFromFireStore(this@TopicsListActivity, topics)
        }

    }
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarTopicsActivity)
        // val actionBar = supportActionBar
        binding.btnTopicsBack.setOnClickListener {

            val intent = Intent()
            intent.putStringArrayListExtra(Constants.TOPICS, topics)
            setResult(RESULT_OK, intent)

            onBackPressed()
        }

        binding.btnAddTopics.setOnClickListener{
            var destIntent = Intent(this@TopicsListActivity, AddTopicActivity::class.java)
            startActivityForResult(destIntent,ADD_TOPIC_ACTIVITY_REQUEST_CODE)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TOPIC_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val topicId:String = data?.getStringExtra("TOPIC")!!
                topics.add(topicId)
                if(topics.size!=0) {
                    showProgressDialog("Please Wait...")
                    FirestoreClass().getTopicListFromFireStore(this@TopicsListActivity, topics)
                }
            }
        }
    }
    override fun onResume() {
        if(topics.size!=0) {
            showProgressDialog("Please Wait...")
            FirestoreClass().getTopicListFromFireStore(this@TopicsListActivity, topics)
        }
        super.onResume()
    }
    fun deleteTopic(topicId:String)
    {
        showProgressDialog("Please Wait...")
        showAlertDialogToDeleteTopic(topicId)
    }
    fun topicDeleteSuccess()
    {
        hideProgressDialog()
        FirestoreClass().getTopicListFromFireStore(this@TopicsListActivity, topics)
    }

    private fun showAlertDialogToDeleteTopic(topicId: String) {

        val builder = AlertDialog.Builder(this@TopicsListActivity)
        //set title for alert dialog
        builder.setTitle("Delete Topic")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete the topic?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            // Show the progress dialog.
            showProgressDialog("Please Wait...")

            // Call the function of Firestore class.
            FirestoreClass().deleteTopic(this@TopicsListActivity,topicId)

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun successTopicListFromFireStore(topics:ArrayList<Topic>)
    {
        hideProgressDialog()

        layoutManager = LinearLayoutManager(this@TopicsListActivity)
        recyclerAdapter = TopicListAdapter(this@TopicsListActivity ,topics)
        recyclerTopicSession.adapter = recyclerAdapter
        recyclerTopicSession.layoutManager = layoutManager
    }


}