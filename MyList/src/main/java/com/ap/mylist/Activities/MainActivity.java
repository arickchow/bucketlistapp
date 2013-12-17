package com.ap.mylist.Activities;


import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.mylist.Dialogs.CreateListDialog;
import com.ap.mylist.ItemListArrayAdapter;
import com.ap.mylist.R;
import com.ap.mylist.sql.Buckets;
import com.ap.mylist.sql.DatabaseHandler;
import com.ap.mylist.sql.ListItem;

import java.util.ArrayList;

public class MainActivity extends ListActivity
        implements CreateListDialog.CreateListDialogListener{

    private ArrayList<ListItem> list = new ArrayList<ListItem>();
    private ItemListArrayAdapter<ListItem> adapter;
    public final static String ITEM_NAME = "ITEM_NAME";
    public final static String ITEM_ID = "ITEM_ID";
    private boolean mDelete = false;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.deleteDatabase("Buckets");
        db = DatabaseHandler.getInstance(getApplicationContext());
        list = db.getAllBuckets();

        setContentView(R.layout.activity_main);
        ListView listview = getListView();

        adapter = new ItemListArrayAdapter<ListItem>(this,R.layout.row_layout,R.id.item_description, list,db);
        listview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //create a new list
            case R.id.action_create:
                CreateListDialog dialog = new CreateListDialog();
                dialog.show(getFragmentManager(),"create_list");
                return true;
            //settings
            case R.id.action_delete:
                mDelete = !mDelete;
                adapter.deleteToggle(mDelete);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        EditText view = (EditText) dialog.getDialog().findViewById(R.id.list_name);
        String value = view.getText().toString();

        if(!value.equals("")){
            Buckets bucket = new Buckets(value);
            db.createBucket(bucket);
            list.add(bucket);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"List " + view.getText().toString() + " created!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        dialog.dismiss();
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        super.onListItemClick(list,view,position,id);

        Intent intent = new Intent(this, MyListActivity.class);
        TextView textView = (TextView) view.findViewById(R.id.item_description);
        intent.putExtra(ITEM_NAME,textView.getText().toString());
        intent.putExtra(ITEM_ID,this.list.get(position).getId());
        startActivity(intent);
    }

    /*@Override
    public void onStop(){
        db.close();
    }
*/
}
