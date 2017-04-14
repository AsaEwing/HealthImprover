package com.asaewing.healthimprover.app2.ViewPager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.asaewing.healthimprover.app2.fl.RootFragment;

import java.util.List;

/**
 *
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ViewPagerItem> mLF;
    Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm, List<ViewPagerItem> viewPagerItems) {
        super(fm);

        this.mContext = context;
        mLF = viewPagerItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLF.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mLF.get(position).getFragment();
    }

    public RootFragment getSelfFragment(int position) {
        return (RootFragment) mLF.get(position).getFragment();
    }

    public String getFl_TAG(int position) {
        return mLF.get(position).getFl_TAG();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return FragmentPagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mLF.get(position).getTitle();
    }
}