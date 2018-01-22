package com.blackwell.blackwell_hw9;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class TabsPageAdapter extends FragmentPagerAdapter
{
    private final ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
    private final ArrayList<String> fragmentNameList=new ArrayList<String>();

    public TabsPageAdapter(FragmentManager frag)
    {
        super(frag);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragmentNameList.get(position);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String name)
    {
        fragmentList.add(fragment);
        fragmentNameList.add(name);
    }
}
