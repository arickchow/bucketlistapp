package com.ap.mylist;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ap.mylist.Activities.ItemListFragment;
import com.ap.mylist.Activities.MainActivity;

/**
 * Created by Arick Chow on 11/30/13.
 */
public class ItemPagerAdapter extends FragmentStatePagerAdapter {

    long item_id;

    public ItemPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i){
        Fragment fragment = new ItemListFragment(i);
        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.ITEM_ID,item_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public int getCount(){
        return 3;
    }

    public void setItemId(long id){
        this.item_id = id;
    }
}
