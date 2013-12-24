package com.ap.mylist.Activities;



import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
public class ItemListFragment extends Fragment {

    private DatabaseHandler db;
    long bucket_id;
    int fragment_id;
    ArrayList<ListItem> items;
    private ItemListArrayAdapter<ListItem> adapter;
    private static final int CREATE_ITEM = 1;
    private boolean mDelete = false;
    private ViewGroup mContainerView;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        private View currentView;
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.edit_item_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            Log.e("DEBUG","DESTROY ACTION");
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            getActivity().getCurrentFocus().clearFocus();
        }
    };

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
        //ListView listView = getListView();
        Bundle bundle = getArguments();
        bucket_id = bundle.getLong(MainActivity.ITEM_ID);
        items = db.getItemByBucketAndStatusId(bucket_id,fragment_id);
        mContainerView = (ViewGroup)getActivity().findViewById(R.id.animatino_list_container);
        //adapter = new ItemListArrayAdapter<ListItem>(this.getActivity(),R.layout.row_item_layout,R.id.item_description, items,db);
        //listView.setAdapter(adapter);

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
                //Intent intent = new Intent(getActivity(), ItemActivity.class);
                //startActivityForResult(intent, CREATE_ITEM);\
                Log.e("DEBUG",this.getClass().toString()+": Item Creation");
                addItem();
                return true;
            case R.id.action_delete:
                Log.e("DEBUG",this.getClass().toString()+": Fragment Deletion");
                mDelete = !mDelete;
                //adapter.deleteToggle(mDelete);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ViewGroup createView(ListItem item){
        final ListItem element = item;
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.row_item_layout,mContainerView,false);
        ((EditText)newView.findViewById(R.id.item_description)).setText(item.toString());
        View editText = newView.findViewById(R.id.item_description);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    view.onTouchEvent(motionEvent);
                    InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
                    getActivity().startActionMode(mActionModeCallback);
                }
                return false;
            }
        });
        ImageView imageView = (ImageView)newView.findViewById(R.id.item_icon);
        //imageView.setVisibility(showImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!items.isEmpty()){
                    items.remove(element);
                    mContainerView.removeView(newView);
                    //db.deleteBucket(value.getId());
                    //element.delete(db);
                    //notifyDataSetChanged();
                    Toast.makeText(getActivity(), element + " deleted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return newView;
    }

    public void addItem(){
        Items item = new Items();
        items.add(item);
        final ViewGroup view = createView(item);
        mContainerView.addView(view);
        view.requestFocus();
        View editText = view.findViewById(R.id.item_description);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);

        getActivity().startActionMode(mActionModeCallback);

    }

    public void addContextMenu(View view){
        Log.e("DEBUG","ITS WORKING");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.animation_layout_list, container,false);
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
