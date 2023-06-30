package com.example.crud2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(){

    private var stdlist : ArrayList<StudentModel> = ArrayList()
    private var onClickItem : ((StudentModel) -> Unit)? =null
    private var onClickDeleteItem : ((StudentModel) -> Unit)? =null

    fun addItems(items: ArrayList<StudentModel>)
    {
        this.stdlist = items
        notifyDataSetChanged()

    }

    fun setOnClickItem(callback: ((StudentModel) -> Unit)? =null)
    {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: ((StudentModel) -> Unit))
    {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std,parent,false)
    )

    override fun getItemCount(): Int {
        return stdlist.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val std = stdlist[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{
            onClickItem?.invoke(std)
        }
        holder.btnDelete.setOnClickListener{
            onClickDeleteItem?.invoke(std)
        }
    }

    class StudentViewHolder(var view : View) :RecyclerView.ViewHolder(view){

        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var email = view.findViewById<TextView>(R.id.tvEmail)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std:StudentModel)
        {
            id.text = std.id.toString()
            name.text = std.name
            email.text = std.email
        }

    }
}