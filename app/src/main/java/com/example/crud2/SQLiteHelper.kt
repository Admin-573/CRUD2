package com.example.crud2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

@SuppressLint("Range")
class SQLiteHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "student.db"
        private const val DATABASE_VERSION = 1
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTblStudent = "CREATE TABLE $TBL_STUDENT($ID INTEGER PRIMARY KEY,$NAME TEXT,$EMAIL TEXT);"
        p0?.execSQL(createTblStudent)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(p0)
    }

    fun insertStudent(std : StudentModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)
        contentValues.put(EMAIL,std.email)

        val success = db.insert(TBL_STUDENT,null,contentValues)
        db.close()
        return success
    }

    fun getAllStudent() : ArrayList<StudentModel>
    {
        val stdlist : ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_STUDENT"
        val db = this.writableDatabase

        val cursor : Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        } catch (e : Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var name : String
        var email : String

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))

                val std = StudentModel (id=id,name=name,email=email)
                stdlist.add(std)
            } while (cursor.moveToNext())
        }
        return stdlist
    }

    fun updateStudent(std:StudentModel) :Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)
        contentValues.put(EMAIL,std.email)

        val success = db.update(TBL_STUDENT,contentValues,"id="+std.id,null)
        db.close()
        return success
    }

    fun deleteStudentById(id:Int) : Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(TBL_STUDENT,"id=$id",null)
        db.close()
        return success
    }
}