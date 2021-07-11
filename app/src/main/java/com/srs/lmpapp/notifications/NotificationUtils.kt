package com.srs.lmpapp.utils

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection
const val SERVER_KEY="AAAABXJ3AF4:APA91bHGu3FuABPZh11tWl2gKyLLbLRHK1WDHLGszYVQKUIY8yR2et-auJzKERT_UrAhZMwxkDlfFzvQfcRUByx5KrBG-q9si6hhC-oVAuhuSSm3vaiVIPYwoCoY6KFTEpRb9twvGx2N"
const val TAG="NotificationUtils"
class NotificationUtils
{
    companion object {
        fun subscribeToTopic(topicname: String) {


            FirebaseMessaging.getInstance().subscribeToTopic(topicname)
                .addOnCompleteListener { task ->
                    var msg = "Subscribed to $topicname"
                    if (!task.isSuccessful) {
                        msg = "Failed to subscribe to $topicname"
                    }
                    Log.d(TAG, msg)

                }
        }
        fun unSubscribeToTopic(topicname: String) {


            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicname).addOnSuccessListener {
                Log.d(TAG, "Unsubscribed $topicname")
            }.addOnFailureListener {
                Log.d(TAG, "Failed to Unsubscribe $topicname")
            }
        }

        fun sendMessage(title: String, content: String, to: String) {
            GlobalScope.launch {
                val endpoint = "https://fcm.googleapis.com/fcm/send"
                try {
                    val url = URL(endpoint)
                    val httpsURLConnection: HttpsURLConnection =
                        url.openConnection() as HttpsURLConnection
                    httpsURLConnection.readTimeout = 10000
                    httpsURLConnection.connectTimeout = 15000
                    httpsURLConnection.requestMethod = "POST"
                    httpsURLConnection.doInput = true
                    httpsURLConnection.doOutput = true

                    // Adding the necessary headers
                    httpsURLConnection.setRequestProperty("authorization", "key=${SERVER_KEY}")
                    httpsURLConnection.setRequestProperty("Content-Type", "application/json")

                    // Creating the JSON with post params
                    val body = JSONObject()

                    val data = JSONObject()
                    data.put("title", title)
                    data.put("body", content)
                    body.put("data", data)

                    body.put("to", to)

                    val outputStream: OutputStream =
                        BufferedOutputStream(httpsURLConnection.outputStream)
                    val writer = BufferedWriter(OutputStreamWriter(outputStream, "utf-8"))
                    writer.write(body.toString())
                    writer.flush()
                    writer.close()
                    outputStream.close()
                    val responseCode: Int = httpsURLConnection.responseCode
                    val responseMessage: String = httpsURLConnection.responseMessage
                    Log.d("Response:", "$responseCode $responseMessage")
                    var result = String()
                    var inputStream: InputStream? = null
                    inputStream = if (responseCode in 400..499) {
                        httpsURLConnection.errorStream
                    } else {
                        httpsURLConnection.inputStream
                    }

                    if (responseCode == 200) {
                        Log.e("Success:", "notification sent $title \n $content")
                        // The details of the user can be obtained from the result variable in JSON format
                    } else {
                        Log.e("Error", "Error Response")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}