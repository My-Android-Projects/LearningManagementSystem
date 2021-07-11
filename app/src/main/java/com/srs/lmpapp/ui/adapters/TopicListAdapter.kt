package com.srs.lmpapp.ui.adapters




import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.model.Module
import com.srs.lmpapp.model.Topic
import com.srs.lmpapp.ui.activities.FacultyAddTopicsActivity
import com.srs.lmpapp.ui.activities.TopicsListActivity
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.MSPEditText
import com.srs.lmpapp.utils.MSPTextView
import com.srs.lmpapp.utils.MSPTextViewBold


class TopicListAdapter(val context: Context, val itemList: ArrayList<Topic>) :
    RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicListViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_module_item,
            parent,
            false
        )
        return TopicListViewHolder(view)
    }
    class TopicListViewHolder(view: View): RecyclerView.ViewHolder(view)
    {

        val txtTopicName:MSPTextView=view.findViewById(R.id.txtTopicName)
        val txtTopicDuration: MSPTextView =view.findViewById(R.id.txtTopicDuration)

        val btn_delete_topic:ImageButton=view.findViewById(R.id.btn_delete_topic)
        val llTopicContent:CardView=view.findViewById(R.id.llTopicContent)

    }

    override fun onBindViewHolder(holder: TopicListViewHolder, position: Int)
    {
        val topic: Topic =itemList[position]
        holder.txtTopicName.text=topic.name
        //holder.txtTopicDuration.text= topic.topicvideo!!.toString()


        holder.btn_delete_topic.setOnClickListener{
            val topic: Topic =itemList[position]
            TopicsListActivity().deleteTopic(topic.id!!)
        }
        holder.llTopicContent.setOnClickListener{

        }
    }



    override fun getItemCount(): Int {
        return itemList.size
    }
}

