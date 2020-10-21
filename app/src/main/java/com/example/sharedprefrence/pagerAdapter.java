package com.example.sharedprefrence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class pagerAdapter extends FragmentPagerAdapter {

    private List<String> titles = new ArrayList<>();
    private List<Fragment>fragments = new ArrayList<>();

    public void addFragment(Fragment fragment,String title){
        fragments.add(fragment);
        titles.add(title);
    }

    public pagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if(fragments == null)
            return 0;
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public List<String> getTitles() {
        return titles;
    }
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
    public List<Fragment> getFragments() {
        return fragments;
    }
    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }
}
