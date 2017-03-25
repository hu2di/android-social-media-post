package vn.sunnet.hungdh.socialmediaplatform.settings.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import vn.sunnet.hungdh.socialmediaplatform.R;

/**
 * Created by HUNGDH on 7/20/2015.
 */
public class FacebookLogin extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("myLog", "Flag onActivityResult 1");
        FacebookLoginFragment fragment = (FacebookLoginFragment) getSupportFragmentManager().findFragmentById(R.id.fr_facebook_login);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
            Log.d("myLog", "Flag onActivityResult 2");
        }
        Log.d("myLog", "Flag onActivityResult 3");
    }
}
