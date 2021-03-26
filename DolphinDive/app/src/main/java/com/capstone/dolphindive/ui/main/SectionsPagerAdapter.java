package com.capstone.dolphindive.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.capstone.dolphindive.DiveLog_Create_Free_Frag;
import com.capstone.dolphindive.DiveLog_Create_Scu_Frag;
import com.capstone.dolphindive.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Divelog_tab_text_1, R.string.Divelog_tab_text_2};
    private final Context mContext;
    private String num;

    public SectionsPagerAdapter(Context context, FragmentManager fm , String numlog) {
        super(fm);
        mContext = context;
        num = numlog;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putString("numlog", num);
                fragment = new DiveLog_Create_Scu_Frag();
                fragment.setArguments(bundle);
                break;
            case 1:
                bundle.putString("numlog", num);
                fragment = new DiveLog_Create_Free_Frag();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}