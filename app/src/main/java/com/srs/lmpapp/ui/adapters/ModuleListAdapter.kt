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
import com.srs.lmpapp.firestore.FirestoreClass
import com.srs.lmpapp.model.Module
import com.srs.lmpapp.ui.activities.BaseActivity
import com.srs.lmpapp.ui.activities.FacultyAddTopicsActivity
import com.srs.lmpapp.ui.activities.ModulesActivity
import com.srs.lmpapp.utils.Constants
import com.srs.lmpapp.utils.MSPEditText
import com.srs.lmpapp.utils.MSPTextView
import com.srs.lmpapp.utils.MSPTextViewBold


class ModuleListAdapter(val context: Context, val itemList: ArrayList<Module>) :
    RecyclerView.Adapter<ModuleListAdapter.ModuleListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleListViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_module_item,
            parent,
            false
        )
        return ModuleListViewHolder(view)
    }
    class ModuleListViewHolder(view: View): RecyclerView.ViewHolder(view)
    {

            val txtModuleName:MSPTextView=view.findViewById(R.id.txtModuleName)
            val txtModuleDuration: MSPTextView =view.findViewById(R.id.txtModuleDuration)
             val txtNoOfTopics: MSPTextView =view.findViewById(R.id.txtNoOfTopics)
        val btn_delete_module:ImageButton=view.findViewById(R.id.btn_delete_module)
        val llModuleContent:CardView=view.findViewById(R.id.llModuleContent)

    }

    override fun onBindViewHolder(holder: ModuleListViewHolder, position: Int)
    {
        val module: Module =itemList[position]
        holder.txtModuleName.text=module.name
        holder.txtModuleDuration.text= module.duration!!.toString()
        holder.txtNoOfTopics.text=module.topics?.size.toString()

        holder.btn_delete_module.setOnClickListener{
            ModulesActivity().deleteModule(module.id!!)


        }

    }


    override fun getItemCount(): Int {
        return itemList.size
    }
}

