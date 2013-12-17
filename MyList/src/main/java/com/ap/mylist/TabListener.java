package com.ap.mylist;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;



/**
 * Created by Arick Chow on 11/29/13.
 */
public class TabListener<T> extends Fragment implements ActionBar.TabListener {

    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    public TabListener(Activity activity, String tag, Class<T> clz){
        mActivity = activity;
        mTag = tag;
        mClass = clz;
    }

    public void onTabSelected(Tab tab, FragmentTransaction transaction){
        if(mFragment == null){
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            //TODO
            transaction.add(R.id.item_container,mFragment,mTag);
        }
        else{
            transaction.attach(mFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction transaction){
        if(mFragment != null){
            transaction.detach(mFragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction transaction){
        //Do nothing
    }
}
