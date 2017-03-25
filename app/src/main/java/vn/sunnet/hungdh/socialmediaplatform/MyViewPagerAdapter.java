package vn.sunnet.hungdh.socialmediaplatform;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by HUNGDH on 7/17/2015.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> listFragment;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("myLog", "Fragment Name: " + listFragment.get(position).toString());
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
