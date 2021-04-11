package com.srs.lmpapp.ui.adapters

import com.srs.lmpapp.model.User



import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.utils.Constants

class StudentListAdapter(val context: Context, val itemList:List<User>) :
    RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.fragment_student_item,parent,false)
        return StudentViewHolder(view)
    }
    class StudentViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtStudentName: TextView =view.findViewById(R.id.txtStudentName)
        val txtStudentEmail: TextView =view.findViewById(R.id.txtStudentEmail)
        val txtStudentMobile: TextView =view.findViewById(R.id.txtStudentMobile)
        val imgStudentPhoto: ImageView =view.findViewById(R.id.imgStudentPhoto)


    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student: User =itemList[position]
        holder.txtStudentName.text="${student.firstName}  ${student.lastName}"
        holder.txtStudentEmail.text=student.email
        holder.txtStudentMobile.text=student.mobile.toString()

        Picasso.get().load(student.image).error(R.drawable.default_book_cover).into(holder.imgStudentPhoto)


    }



    override fun getItemCount(): Int {
        return itemList.size
    }
}