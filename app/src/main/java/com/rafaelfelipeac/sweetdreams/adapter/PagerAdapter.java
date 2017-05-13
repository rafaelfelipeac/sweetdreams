package com.rafaelfelipeac.sweetdreams.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rafaelfelipeac.sweetdreams.fragments.FormAudioFragment;
import com.rafaelfelipeac.sweetdreams.fragments.FormTextFragment;

/**
 * Created by Rafael Felipe on 25/01/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numTabs;

    public PagerAdapter(FragmentManager manager, int numTabs) {
        super(manager);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FormTextFragment ftf = new FormTextFragment();
                return ftf;
            case 1:
                FormAudioFragment faf = new FormAudioFragment();
                return faf;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
