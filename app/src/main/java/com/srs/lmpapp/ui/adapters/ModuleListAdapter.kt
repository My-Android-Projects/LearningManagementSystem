package com.srs.lmpapp.ui.adapters




import android.R.attr.data
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.srs.lmpapp.R
import com.srs.lmpapp.utils.MSPEditText


class ModuleListAdapter(val context: Context, val itemList: ArrayList<String>) :
    RecyclerView.Adapter<ModuleListAdapter.ModuleListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleListViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(
            R.layout.editable_list_item,
            parent,
            false
        )
        return ModuleListViewHolder(view)
    }
    class ModuleListViewHolder(view: View): RecyclerView.ViewHolder(view)
    {

            val txtmoduleName:MSPEditText=view.findViewById(R.id.txtModule)
            val btn_delete:ImageButton=view.findViewById(R.id.btn_delete_module)
            val btn_confirm:ImageButton=view.findViewById(R.id.btn_confirm)

    }

    override fun onBindViewHolder(holder: ModuleListViewHolder, position: Int)
    {
        val module: String =itemList[position]
        holder.txtmoduleName.setText(module)
        holder.btn_confirm.isVisible=false

        holder.btn_delete.setOnClickListener{
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
            holder.itemView.visibility = View.GONE
        }
        holder.txtmoduleName.setOnClickListener{
            holder.txtmoduleName.isEnabled=true
            holder.btn_confirm.isVisible=true
        }
        holder.btn_confirm.setOnClickListener{
            holder.txtmoduleName.isEnabled=false
            holder.btn_confirm.isVisible=false
            itemList[position]=holder.txtmoduleName.text.toString()
            notifyItemChanged(position)
            notifyItemRangeChanged(position,1)

        }

    }
    fun addItem(moduleName: String)
    {

        itemList.add(itemList.size, moduleName)
        notifyItemInserted(itemList.size)

        notifyItemRangeInserted(itemList.size+1, 1)
    }
    fun getItems() : ArrayList<String>
    {   return itemList

    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}