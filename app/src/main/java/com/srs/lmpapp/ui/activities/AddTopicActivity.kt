package com.srs.lmpapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityAddTopicBinding
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Topic
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.GlideLoader
import java.io.IOException

class AddTopicActivity: BaseActivity() {
    lateinit var binding:ActivityAddTopicBinding
    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedVideoFileUri: Uri? = null

    private var mVideoURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAddTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivAddUpdateTopicVideo.setOnClickListener{

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            ==PackageManager.PERMISSION_GRANTED) {

            Constants.showVideoChooser(this@AddTopicActivity)

        } else {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE
            )
        }
           // configureVideoView()

        }
        binding.btnAddTopic.setOnClickListener {
            if(mSelectedVideoFileUri!=null)
                uploadCourseVideo()
            else
                addTopic()
        }
    }

    private fun uploadCourseVideo()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadVideoToCloudStorage(
            this@AddTopicActivity,
            mSelectedVideoFileUri,
            Constants.TOPIC_NAME
        )


    }
    fun videoUploadSuccess(uri:String)
    {
        hideProgressDialog()
        mVideoURL=uri
        addTopic()

    }
    fun addTopic()
    {
        val topic= Topic (
            "", binding.txtTopic.text.toString(),
            mVideoURL)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadTopicDetails(this@AddTopicActivity,topic)
    }
    fun topicUploadSuccess(topicId:String)
    {
        hideProgressDialog()

        val intent = Intent()
        intent.putExtra("TOPIC", topicId)
        setResult(RESULT_OK, intent)
        onBackPressed()
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_VIDEO_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedVideoFileUri = data.data!!
                      //  if(mSelectedVideoFileUri!=null)
                        //    uploadCourseVideo()
                        binding.videoView1.setVideoPath(
                            mSelectedVideoFileUri.toString()
                        )

                        binding.videoView1.start()

                        /*mSelectedVideoFileUri(this@AddTopicActivity).loadUserPicture(
                            mSelectedVideoFileUri!!,
                            binding.ivUserPhoto
                        )*/
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddTopicActivity,
                            "Video selection failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playVideo() {
        //val url = "http://........" // your URL here
        val mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            mSelectedVideoFileUri?.let { setDataSource(applicationContext, it) }
            prepare()
            start()

        }
    }
}