package com.ap.mylist.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ap.mylist.Dialogs.DatePickerFragment;
import com.ap.mylist.R;

import java.util.ArrayList;


/**
 * Created by Arick Chow on 12/3/13.
 */
public class ItemActivity extends Activity {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //dont care
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                onTextChange(charSequence,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //dont care
            }
        };

        EditText editText = (EditText)findViewById(R.id.edit_item_date);
        editText.addTextChangedListener(textWatcher);
    }

    public void showDatePickerDialog(View v){
        DialogFragment dialog = new DatePickerFragment();
        dialog.show(getFragmentManager(),"datepicker");
    }

    public void validate(View v){
        //get view contents
        EditText name = (EditText)findViewById(R.id.edit_item_name);
        EditText description = (EditText)findViewById(R.id.edit_item_description);
        EditText date = (EditText)findViewById(R.id.edit_item_date);


        EditText[] list = new EditText[]{name,date};

        //basic form validation
        for(EditText editText: list){
            if(editText.getText().length() == 0){
                Toast.makeText(getApplicationContext(),"Provide name and date", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //package results in intent
        Intent returnIntent = new Intent();
        returnIntent.putExtra(NAME,name.getText().toString());
        returnIntent.putExtra(DESCRIPTION,description.getText().toString());
        returnIntent.putExtra(DATE,date.getText().toString());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void cancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    //Handle Date input
    public void onTextChange(CharSequence s, int start, int before, int count){
        //TODO Insert /'s automatically when entering date
    }
}
