package com.ap.mylist.Activities;


import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
    private int showImage = 0;
    private DatabaseHandler db;
    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.deleteDatabase("Buckets");

        db = DatabaseHandler.getInstance(getApplicationContext());
        list = db.getAllBuckets();

        setContentView(R.layout.activity_main);
        //mContainerView = (ViewGroup)findViewById(R.id.animatino_list_container);
        ListView listview = getListView();
        //showItems(list);


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
                showImage = mDelete ? View.VISIBLE : View.INVISIBLE;
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
            //addItem(bucket);
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

    public ViewGroup createView(ListItem item){
        final ListItem element = item;
        final ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.row_layout,mContainerView,false);
        ((TextView)view.findViewById(R.id.item_description)).setText(item.toString());
        ImageView imageView = (ImageView)view.findViewById(R.id.item_icon);
        imageView.setVisibility(showImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!list.isEmpty()){
                    list.remove(element);
                    mContainerView.removeView(view);
                    //db.deleteBucket(value.getId());
                    element.delete(db);
                    //notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), element + " deleted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void addItem(ListItem bucket){
        final ViewGroup view = createView(bucket);
        mContainerView.addView(view);

    }

    public void showItems(ArrayList<ListItem> list){
        //if(mContainerView)
        mContainerView.removeAllViews();
        Log.e("DEBUG",list.toString());
        for(ListItem item : list){
            final ViewGroup view =  createView(item);
            mContainerView.addView(view);
        }

    }

    /*@Override
    public void onStop(){
        db.close();
    }
*/
}
