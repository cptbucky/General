package com.avantics.savingscalc.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.avantics.savingscalc.common.fragments.IncumbentFragment;
import com.avantics.savingscalc.common.fragments.ProposedFragment;
import com.avantics.savingscalc.common.fragments.SummaryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 02/06/13.
 */
public class vertCollectionPagerAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 3;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentManager mFM;

    public vertCollectionPagerAdapter(FragmentManager fm) {
        super(fm);

        mFM = fm;

        // add fragments
        mFragments.add(new IncumbentFragment());
        mFragments.add(new ProposedFragment());
        mFragments.add(new SummaryFragment());
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    // This is called by the ViewPager to get the fragment at tab position pos
    // ViewPager calls this when the Tab handler handles a tab change
    @Override
    public Fragment getItem(int pos) {
        Fragment f = mFragments.get(pos);

        UiBindingManager binder = new UiBindingManager(f.getView());

        return f;
    }

    public Fragment getActiveFragment(ViewPager container, int pos) {
        String name = "android:switcher:" + container.getId() + ":" + pos;

        return mFM.findFragmentByTag(name);
    }

}
