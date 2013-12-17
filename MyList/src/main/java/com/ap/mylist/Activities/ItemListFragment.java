package com.ap.mylist.Activities;



import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ap.mylist.Activities.ItemActivity;
import com.ap.mylist.Activities.MainActivity;
import com.ap.mylist.Activities.MyListActivity;
import com.ap.mylist.Dialogs.CreateItemDialog;
import com.ap.mylist.ItemListArrayAdapter;
import com.ap.mylist.R;
import com.ap.mylist.sql.DatabaseHandler;
import com.ap.mylist.sql.Items;
import com.ap.mylist.sql.ListItem;

import java.util.ArrayList;


/**
 * Created by Arick Chow on 11/30/13.
 */
public class ItemListFragment extends ListFragment {

    private DatabaseHandler db;
    long bucket_id;
    int fragment_id;
    ArrayList<ListItem> items;
    private ItemListArrayAdapter<ListItem> adapter;
    private static final int CREATE_ITEM = 1;
    private boolean mDelete = false;

    public ItemListFragment(int id){
        this.fragment_id = id;
        if(this.fragment_id == Items.INPROGRESS){
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        db = DatabaseHandler.getInstance(getActivity().getApplicationContext());
        ListView listView = getListView();
        Bundle bundle = getArguments();
        bucket_id = bundle.getLong(MainActivity.ITEM_ID);

        items = db.getItemByBucketAndStatusId(bucket_id,fragment_id);
        adapter = new ItemListArrayAdapter<ListItem>(this.getActivity(),R.layout.row_layout,R.id.item_description, items,db);
        listView.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_create:
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                startActivityForResult(intent, CREATE_ITEM);
                Log.e("DEBUG",this.getClass().toString()+": Item Creation");
                return true;
            case R.id.action_delete:
                Log.e("DEBUG",this.getClass().toString()+": Fragment Deletion");
                mDelete = !mDelete;
                adapter.deleteToggle(mDelete);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_list, container,false);
        return rootView;
    }

    public void onListItemClick(ListView list, View view, int position, long id){
        //TODO
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CREATE_ITEM){
            if(resultCode == getActivity().RESULT_OK){
                //CREATE LIST AND ADD TO DATA BASE
                Items item = new Items();
                item.setBucket_id(bucket_id);
                item.setStatus(Items.INPROGRESS);
                item.setName(data.getStringExtra(ItemActivity.NAME));
                items.add(db.createItem(item));
                adapter.notifyDataSetChanged();
                Log.e("DEBUG","Item Created");
            }
        }
    }

}
