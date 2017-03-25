package vn.sunnet.hungdh.socialmediaplatform.settings.mypost;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;
import vn.sunnet.hungdh.socialmediaplatform.MyViewPagerAdapter;
import vn.sunnet.hungdh.socialmediaplatform.R;

/**
 * Created by HUNGDH on 8/3/2015.
 */
public class MyPost extends FragmentActivity {

    private ViewPager viewPagerMyPost;

    private ImageButton ibtn_posted, ibtn_schedule, ibtn_error, ibtn_delete;
    private ImageButton bd_posted, bd_schedule, bd_error, bd_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost_layout);

        ibtn_posted = (ImageButton) findViewById(R.id.ibtn_posted);
        ibtn_schedule = (ImageButton) findViewById(R.id.ibtn_schedule);
        ibtn_error = (ImageButton) findViewById(R.id.ibtn_error);
        ibtn_delete = (ImageButton) findViewById(R.id.ibtn_delete);

        ibtn_posted.setOnClickListener(buttonClick);
        ibtn_schedule.setOnClickListener(buttonClick);
        ibtn_error.setOnClickListener(buttonClick);
        ibtn_delete.setOnClickListener(buttonClick);

        bd_posted = (ImageButton) findViewById(R.id.bd_posted);
        bd_schedule = (ImageButton) findViewById(R.id.bd_schedule);
        bd_error = (ImageButton) findViewById(R.id.bd_error);
        bd_delete = (ImageButton) findViewById(R.id.bd_delete);
    }

    View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_posted:
                    viewPagerMyPost.setCurrentItem(0);
                    break;
                case R.id.ibtn_schedule:
                    viewPagerMyPost.setCurrentItem(1);
                    break;
                case R.id.ibtn_error:
                    viewPagerMyPost.setCurrentItem(2);
                    break;
                case R.id.ibtn_delete:
                    viewPagerMyPost.setCurrentItem(3);
                    break;
            }
        }
    };

    private void initViewPagerMyPost() {
        viewPagerMyPost = (ViewPager)findViewById(R.id.view_pager_mypost);

        FragmentPosted fragmentPosted = new FragmentPosted();
        FragmentSchedule fragmentSchedule = new FragmentSchedule();
        FragmentError fragmentError = new FragmentError();
        FragmentDelete fragmentDelete = new FragmentDelete();

        List<Fragment> listFragment = new ArrayList<Fragment>();
        listFragment.add(fragmentPosted);
        listFragment.add(fragmentSchedule);
        listFragment.add(fragmentError);
        listFragment.add(fragmentDelete);

        MyViewPagerAdapter mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), listFragment);
        viewPagerMyPost.setAdapter(mViewPagerAdapter);


        viewPagerMyPost.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int currentItem) {
                switch (currentItem) {
                    case 0:
                        set0();
                        break;
                    case 1:
                        set1();
                        break;
                    case 2:
                        set2();
                        break;
                    case 3:
                        set3();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void set0() {
        ibtn_posted.setImageResource(R.drawable.posted_filter_icon_sel);
        ibtn_schedule.setImageResource(R.drawable.scheduled_filter_icon);
        ibtn_error.setImageResource(R.drawable.canceled_filter_icon);
        ibtn_delete.setImageResource(R.drawable.trashed_filter_icon);

        bd_posted.setBackgroundColor(getResources().getColor(R.color.blue));
        bd_schedule.setBackgroundColor(getResources().getColor(R.color.white));
        bd_error.setBackgroundColor(getResources().getColor(R.color.white));
        bd_delete.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void set1() {
        ibtn_posted.setImageResource(R.drawable.posted_filter_icon);
        ibtn_schedule.setImageResource(R.drawable.scheduled_filter_icon_sel);
        ibtn_error.setImageResource(R.drawable.canceled_filter_icon);
        ibtn_delete.setImageResource(R.drawable.trashed_filter_icon);

        bd_posted.setBackgroundColor(getResources().getColor(R.color.white));
        bd_schedule.setBackgroundColor(getResources().getColor(R.color.blue));
        bd_error.setBackgroundColor(getResources().getColor(R.color.white));
        bd_delete.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void set2() {
        ibtn_posted.setImageResource(R.drawable.posted_filter_icon);
        ibtn_schedule.setImageResource(R.drawable.scheduled_filter_icon);
        ibtn_error.setImageResource(R.drawable.canceled_filter_icon_sel);
        ibtn_delete.setImageResource(R.drawable.trashed_filter_icon);

        bd_posted.setBackgroundColor(getResources().getColor(R.color.white));
        bd_schedule.setBackgroundColor(getResources().getColor(R.color.white));
        bd_error.setBackgroundColor(getResources().getColor(R.color.blue));
        bd_delete.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void set3() {
        ibtn_posted.setImageResource(R.drawable.posted_filter_icon);
        ibtn_schedule.setImageResource(R.drawable.scheduled_filter_icon);
        ibtn_error.setImageResource(R.drawable.canceled_filter_icon);
        ibtn_delete.setImageResource(R.drawable.trashed_filter_icon_sel);

        bd_posted.setBackgroundColor(getResources().getColor(R.color.white));
        bd_schedule.setBackgroundColor(getResources().getColor(R.color.white));
        bd_error.setBackgroundColor(getResources().getColor(R.color.white));
        bd_delete.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewPagerMyPost();

        SharedPreferences preferences = this.getSharedPreferences("my_post_view_pager_current_item", Context.MODE_PRIVATE);
        int currentItem = preferences.getInt("currentItem", 0);
        viewPagerMyPost.setCurrentItem(currentItem, true);

        switch (currentItem) {
            case 0:
                ibtn_posted.setImageResource(R.drawable.posted_filter_icon_sel);
                bd_posted.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case 1:
                ibtn_schedule.setImageResource(R.drawable.scheduled_filter_icon_sel);
                bd_schedule.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case 2:
                ibtn_error.setImageResource(R.drawable.canceled_filter_icon_sel);
                bd_error.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case 3:
                ibtn_delete.setImageResource(R.drawable.trashed_filter_icon_sel);
                bd_delete.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = this.getSharedPreferences("my_post_view_pager_current_item", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        int currentItem = viewPagerMyPost.getCurrentItem();
        editor.putInt("currentItem", currentItem);
        editor.commit();
    }
}
