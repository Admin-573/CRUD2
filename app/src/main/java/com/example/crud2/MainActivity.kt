package com.example.crud2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.sql.RowId

class MainActivity : AppCompatActivity() {

    private lateinit var edName : EditText
    private lateinit var edEmail : EditText
    private lateinit var btnAdd : Button
    private lateinit var btnView : Button
    private lateinit var btnUpdate : Button

    private lateinit var sqlitehelper : SQLiteHelper
    private lateinit var recyclerView : RecyclerView
    private var adapter : StudentAdapter ?= null
    private var std : StudentModel ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerview()
        sqlitehelper = SQLiteHelper(this)

        btnAdd.setOnClickListener{
            addStudent()
        }
        btnView.setOnClickListener{
            getStudent()
        }
        btnUpdate.setOnClickListener{
            updateStudent()
        }
        adapter?.setOnClickItem{
            Toast.makeText(this,it.name,Toast.LENGTH_SHORT).show()
            // ok now we will update data
            edName.setText(it.name)
            edEmail.setText(it.email)
            std = it
        }
        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }

    }

    private fun getStudent() {
        val stdlist = sqlitehelper.getAllStudent()
        Log.e("pppp","${stdlist.size}")

        adapter?.addItems(stdlist)
    }

    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()
        if (name.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this,"Please Fill Up Required Fields",Toast.LENGTH_LONG).show()
        }
        else
        {
            val std = StudentModel(name = name, email = email)
            val status = sqlitehelper.insertStudent(std)
            if (status>-1)
            {
                Toast.makeText(this,"Student Added",Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudent()
            }
            else
            {
                Toast.makeText(this,"Record Not Saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStudent(){
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name == std?.name && email == std?.email)
        {
            Toast.makeText(this,"Record Not Changed",Toast.LENGTH_SHORT).show()
            return
        }

        if(std == null) return
        val std = StudentModel(id = std!!.id,name = name,email = email)
        val status = sqlitehelper.updateStudent(std)
        if (status > -1){
            clearEditText()
            getStudent()
        } else {
            Toast.makeText(this,"Update Failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteStudent(id: Int)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do You Want To Delete This Record ?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog,_->
            sqlitehelper.deleteStudentById(id)
            getStudent()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog,_->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()

    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerview()
    {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}


