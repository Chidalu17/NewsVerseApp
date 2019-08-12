package com.example.newsverse.adapter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsverse.R;
import com.example.newsverse.fragment.BusinessFragment;
import com.example.newsverse.fragment.EntertainmentFragment;
import com.example.newsverse.fragment.HealthFragment;
import com.example.newsverse.fragment.HomeFragment;
import com.example.newsverse.fragment.ScienceFragment;
import com.example.newsverse.fragment.SportsFragment;
import com.example.newsverse.fragment.TechnologyFragment;
import com.example.newsverse.utils.Constants;

public class CategoryFragmentAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;

    /**
     * Create a new {@link CategoryFragmentAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public CategoryFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.HOME:
                return new HomeFragment();
            case Constants.BUSINESS:
                return new BusinessFragment();
            case Constants.ENTERTAINMENT:
                return new EntertainmentFragment();
            case Constants.HEALTH:
                return new HealthFragment();
            case Constants.SCIENCE:
                return new ScienceFragment();
            case Constants.SPORTS:
                return new SportsFragment();
            case Constants.TECHNOLOGY:
                return new TechnologyFragment();
            default:
                return null;
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 7;
    }

    /**
     * Return page title of the tap
     */
    @Override
    public CharSequence getPageTitle(int position) {
        int titleResId;
        switch (position) {
            case Constants.HOME:
                titleResId = R.string.ic_title_home;
                break;
            case Constants.BUSINESS:
                titleResId = R.string.ic_title_business;
                break;
            case Constants.ENTERTAINMENT:
                titleResId = R.string.ic_title_entertainment;
                break;
            case Constants.HEALTH:
                titleResId = R.string.ic_title_health;
                break;
            case Constants.SCIENCE:
                titleResId = R.string.ic_title_science;
                break;
            case Constants.SPORTS:
                titleResId = R.string.ic_title_sport;
                break;
            case Constants.TECHNOLOGY:
                titleResId = R.string.ic_title_technology;
                break;
            default:
                titleResId = R.string.ic_title_world;
                break;

        }

        return mContext.getString(titleResId);
    }
}
