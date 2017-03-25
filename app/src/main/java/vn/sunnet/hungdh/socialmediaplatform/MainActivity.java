package vn.sunnet.hungdh.socialmediaplatform;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.sunnet.hungdh.socialmediaplatform.data.ConnectionCheck;
import vn.sunnet.hungdh.socialmediaplatform.settings.FragmentSettings;
import vn.sunnet.hungdh.socialmediaplatform.status.FragmentStatus;


public class MainActivity extends FragmentActivity {

    public ViewPager viewPager;
    MyViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewPager();

        ConnectionCheck connectionCheck = new ConnectionCheck(this);
        if (!connectionCheck.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "Kiểm tra lại kết nối internet!", Toast.LENGTH_LONG).show();
            Log.d("myLog", "Check internet!");
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        FragmentSettings fragmentSettings = new FragmentSettings();
        FragmentStatus fragmentStatus = new FragmentStatus();

        List<Fragment> listFragment = new ArrayList<Fragment>();
        listFragment.add(fragmentSettings);
        listFragment.add(fragmentStatus);

        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), listFragment);
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1, true);
        } else {
            if (((FragmentStatus) mViewPagerAdapter.getItem(1)).stateTagTrend != 0) {
                ((FragmentStatus) mViewPagerAdapter.getItem(1)).view_tag.setVisibility(View.INVISIBLE);
                ((FragmentStatus) mViewPagerAdapter.getItem(1)).view_trend.setVisibility(View.INVISIBLE);
                ((FragmentStatus) mViewPagerAdapter.getItem(1)).btn_tag.setImageResource(R.drawable.arroba_icon);
                ((FragmentStatus) mViewPagerAdapter.getItem(1)).btn_trend.setImageResource(R.drawable.hashtag_icon);
                ((FragmentStatus) mViewPagerAdapter.getItem(1)).stateTagTrend = 0;
            } else {
                AlertDialog.Builder delete = new AlertDialog.Builder(this);
                delete.setTitle("Thoát");
                delete.setMessage("Bạn muốn thoát?");
                delete.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                delete.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                delete.show();
            }
        }
    }
}
