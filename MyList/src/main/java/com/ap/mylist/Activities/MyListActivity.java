package com.ap.mylist.Activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.ap.mylist.Activities.MainActivity;
import com.ap.mylist.Dialogs.CreateItemDialog;
import com.ap.mylist.ItemPagerAdapter;
import com.ap.mylist.R;


/**
 * Created by Arick Chow on 11/28/13.
 */
public class MyListActivity extends FragmentActivity {

    ViewPager mViewPager;
    ItemPagerAdapter mItemPager;
    String[] tabs = new String[] {"In Progress","Completed","Incomplete"};
    int mCurrentTab;
    long item_id;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        ActionBar actionBar = getActionBar();
        Intent intent = getIntent();
        String list_name = intent.getStringExtra(MainActivity.ITEM_NAME);
        item_id = intent.getLongExtra(MainActivity.ITEM_ID,-1);
        actionBar.setTitle(list_name);

        setupTabs();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        MenuItem create = menu.findItem(R.id.action_create);
        MenuItem delete = menu.findItem(R.id.action_delete);
        MenuItem[] mMenuItems = new MenuItem[] {create,delete};

        for(MenuItem item: mMenuItems){
            if(mCurrentTab != 0){
                item.setVisible(false);
                item.setEnabled(false);
            }
            else{
                item.setVisible(true);
                item.setEnabled(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_create:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    public void setupTabs(){
        mItemPager = new ItemPagerAdapter(getSupportFragmentManager());
        mItemPager.setItemId(item_id);
        mViewPager = (ViewPager) findViewById(R.id.item_container);
        mViewPager.setAdapter(mItemPager);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                getActionBar().setSelectedNavigationItem(position);
                mCurrentTab = position;
                invalidateOptionsMenu();
            }

        });

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
                mCurrentTab = tab.getPosition();
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        for(String tabName : tabs){
            actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(tabListener));
        }

    }


}
