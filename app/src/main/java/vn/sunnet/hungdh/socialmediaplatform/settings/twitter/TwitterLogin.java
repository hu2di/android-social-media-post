package vn.sunnet.hungdh.socialmediaplatform.settings.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import io.fabric.sdk.android.Fabric;
import vn.sunnet.hungdh.socialmediaplatform.R;

/**
 * Created by HUNGDH on 8/12/2015.
 */
public class TwitterLogin extends Activity {

    private TextView tv_twitter_name;
    private TextView tv_twitter_username;
    private ImageView iv_profile_picture;
    private TextView tv_twitter_remove;

    private TwitterLoginButton loginButton;

    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {
            String output = "Status: " + "Your login was successful " + twitterSessionResult.data.getUserName() + "\nAuth Token Received: " + twitterSessionResult.data.getAuthToken().token;
            getInfo();
            Log.d("myLog", "On Success Login: " + output);
        }

        @Override
        public void failure(TwitterException e) {
            Log.d("myLog", "On Failure Login: " + e.toString());
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.TWITTER_KEY), getResources().getString(R.string.TWITTER_SECRET));
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());

        setContentView(R.layout.activity_twitter_login);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new LoginHandler());
        loginButton.setVisibility(View.INVISIBLE);

        tv_twitter_name = (TextView) findViewById(R.id.tv_twitter_name);
        tv_twitter_username = (TextView) findViewById(R.id.tv_twitter_username);
        iv_profile_picture = (ImageView) findViewById(R.id.iv_profile_picture);
        tv_twitter_remove = (TextView) findViewById(R.id.tv_twitter_remove);
        tv_twitter_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_twitter_name.setText("");
                tv_twitter_username.setText("");
                iv_profile_picture.setImageBitmap(null);
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
                finish();
            }
        });
    }

    private void saving() {
        SharedPreferences preferences = this.getSharedPreferences("my_twitter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        String username = tv_twitter_username.getText().toString();
        editor.putString("username", username);
        String name = tv_twitter_name.getText().toString();
        editor.putString("name", name);

        /*Bitmap realImage = ((BitmapDrawable)iv_profile_picture.getDrawable()).getBitmap();
        if (realImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            editor.putString("profile_picture",encodedImage);
        }*/

        editor.commit();
    }

    private void getInfo() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if (session != null) {
            Log.d("myLog", "Session TT: " + session);

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("my_twitter", Context.MODE_PRIVATE);
            String username = preferences.getString("username", "");
            tv_twitter_username.setText(username);
            String name = preferences.getString("name","");
            tv_twitter_name.setText(name);

            /*String profile_picture = preferences.getString("profile_picture", "");
            if( !profile_picture.equalsIgnoreCase("") ){
                byte[] b = Base64.decode(profile_picture, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                iv_profile_picture.setImageBitmap(bitmap);
            }*/

            Twitter.getApiClient(session).getAccountService()
                    .verifyCredentials(true, false, new Callback<User>() {
                        @Override
                        public void success(Result<User> userResult) {
                            User user = userResult.data;
                            String name = user.name;
                            tv_twitter_name.setText(name);
                            String imageUrl = user.profileImageUrl;
                            Picasso.with(getApplicationContext()).load(imageUrl).into(iv_profile_picture);
                            String username = user.screenName;
                            tv_twitter_username.setText("@" + username);
                        }
                        @Override
                        public void failure(TwitterException e) {
                            Log.d("myLog", "On Failure getInfo: " + e.toString());
                        }
                    });
        } else {
            Twitter.logIn(this, new LoginHandler());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saving();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            loginButton.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Log.d("myLog", "Error Login TW: " + e.toString());
        }
    }
}
