package com.example.weatherapplication.FirstPage

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SelectWeatherOptionDialogFragment(
    private val onOnlineClick: () -> Unit,
    private val onStoredClick: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("انتخاب نوع نمایش")
                .setMessage("می‌خواهید اطلاعات آنلاین نمایش داده شود یا اطلاعات ذخیره‌شده؟")
                .setPositiveButton("Online") { _, _ -> onOnlineClick() }
                .setNegativeButton("Stored") { _, _ -> onStoredClick() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}
