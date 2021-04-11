package com.srs.lmpapp.ui.adapters

import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import com.srs.lmpapp.ui.activities.CourseDetailsActivity




import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.model.Course
import com.srs.lmpapp.ui.activities.EnrolledCourseDetailsActivity
import com.srs.lmpapp.utils.Constants

class MyCourseListAdapter(val context: Context, val itemList:List<Course>,val userType:String) :
    RecyclerView.Adapter<MyCourseListAdapter.MyCourseViewHolder>(){
    private lateinit var  destIntent:Intent
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCourseViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.fragment_course_item,parent,false)
        return MyCourseViewHolder(view)
    }
    class MyCourseViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtCourseName: TextView =view.findViewById(R.id.txtCourseName)
        val txtCourseCategory: TextView =view.findViewById(R.id.txtCourseCategory)
        val txtCourseTimeLine: TextView =view.findViewById(R.id.txtCourseTimeLine)
        val txtCourseCredits: TextView =view.findViewById(R.id.txtCourseCredits)
        val imgCourseImage: ImageView =view.findViewById(R.id.imgCourseImage)
        val llContent: LinearLayout =view.findViewById(R.id.llContent)


    }

    override fun onBindViewHolder(holder: MyCourseViewHolder, position: Int) {
        val course: Course =itemList[position]
        holder.txtCourseName.text=course.name
        holder.txtCourseCategory.text=course.category
        holder.txtCourseTimeLine.text="${course.startdate.toString()} - ${course.enddate.toString()}"
        holder.txtCourseCredits.text= "Credits - ${course.credits.toString()}"
        Picasso.get().load(course.courseimage).error(R.drawable.default_book_cover).into(holder.imgCourseImage)

        holder.llContent.setOnClickListener(){

            if(userType.equals("Student",true))
            {
                 destIntent = Intent(context, EnrolledCourseDetailsActivity::class.java)
                destIntent.putExtra(Constants.CURRENT_COURSE, course)
                //intent.putExtra("currentCourseId",course.id)
                context.startActivity(destIntent)

            }
            else if(userType.equals("Faculty",true))
            {

                    destIntent = android.content.Intent(
                        context,
                        com.srs.lmpapp.ui.activities.FacultyCourseDetailsActivity::class.java
                    )
                    destIntent.putExtra(com.srs.lmpapp.utils.Constants.CURRENT_COURSE, course)
                    //intent.putExtra("currentCourseId",course.id)
                    context.startActivity(destIntent)
            }

        }

    }



    override fun getItemCount(): Int {
        return itemList.size
    }
}