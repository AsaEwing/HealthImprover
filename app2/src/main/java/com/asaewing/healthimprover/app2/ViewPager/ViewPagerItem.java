package com.asaewing.healthimprover.app2.ViewPager;

import android.content.Context;
import android.support.v4.app.Fragment;

public class ViewPagerItem {

    private String mTitle;
    private String mTAG;
    private Fragment mFragment;

    public String getTitle() {
        return mTitle;
    }

    public String getFl_TAG(){
        return mTAG;
    }

    public void setTitle(Context context, int titleResource) {
        this.mTitle = context.getString(titleResource);
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setFl_TAG(String TAG) {
        this.mTAG = TAG;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

}
