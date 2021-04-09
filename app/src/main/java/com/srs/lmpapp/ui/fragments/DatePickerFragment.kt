package com.srs.lmpapp.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment(private val fieldName:String) : DialogFragment(), DatePickerDialog.OnDateSetListener {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(context as Context, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        var c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        val selectedDate: String =
            SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(c.getTime())
        var intent = Intent()
        intent.putExtra("key",fieldName)
        intent.putExtra("value",selectedDate)
        targetFragment!!.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            intent)
    }
}