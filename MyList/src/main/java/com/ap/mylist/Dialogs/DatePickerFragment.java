package com.ap.mylist.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ap.mylist.R;

import java.util.Calendar;

/**
 * Created by Arick Chow on 12/3/13.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog,this,year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        EditText editText = (EditText) getActivity().findViewById(R.id.edit_item_date);
        String date = String.format("%d/%d/%d",month,day,year);
        editText.setText(date);
    }

}
