package vn.sunnet.hungdh.socialmediaplatform.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.File;
import java.io.FileOutputStream;

import twitter4j.StatusUpdate;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.settings.mypost.PostManager;
import vn.sunnet.hungdh.socialmediaplatform.status.CustomService;
import vn.sunnet.hungdh.socialmediaplatform.status.MyTwitterApiClient;

/**
 * Created by HUNGDH on 8/21/2015.
 */
public class MyTwitter {

    Context context;
    ConnectionCheck connectionCheck;

    public MyTwitter(Context context) {
        this.context = context;
        connectionCheck = new ConnectionCheck(context);
    }

    public void postTwitter(final Status status) {

        if (connectionCheck.isConnectingToInternet()) {
            String tweet = status.getStatus();
            Log.d("myLog", "Status: " + tweet);

            if (status.getImage() == null) {
                TwitterSession session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
                MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
                CustomService customService = myTwitterApiClient.getCustomService();
                customService.update(tweet, null, null, null, null,
                        null, null, null, new Callback<Tweet>() {
                            @Override
                            public void success(Result<Tweet> result) {
                                Toast.makeText(context, "Đã post Twitter!", Toast.LENGTH_SHORT).show();
                                Log.d("myLog", "Posted Twitter: " + result.response.getUrl());
                                try {
                                    PostManager postManager = new PostManager(context);
                                    postManager.docFile();
                                    Status statusTW = postManager.getStatus(status);
                                    statusTW.setIsTW(1);
                                    postManager.setStatus(statusTW);
                                    postManager.ghiFile();
                                } catch (Exception e) {
                                    Log.d("myLog", "Error File:" + e.toString());
                                }
                            }

                            public void failure(TwitterException exception) {
                                Toast.makeText(context, "Có lỗi Post Twitter!", Toast.LENGTH_SHORT).show();
                                Log.d("myLog", "Error Twitter: " + exception.toString());
                                try {
                                    PostManager postManager = new PostManager(context);
                                    postManager.docFile();
                                    Status statusTW = postManager.getStatus(status);
                                    statusTW.setIsTW(2);
                                    statusTW.setManager(2);
                                    postManager.setStatus(statusTW);
                                    postManager.ghiFile();
                                } catch (Exception e) {
                                    Log.d("myLog", "Error File:" + e.toString());
                                }
                            }
                        });
            } else {
                new PostImage().execute(status);
            }
        } else {
            Toast.makeText(context, "Không có kết nối internet!", Toast.LENGTH_SHORT).show();
            Log.d("myLog", "Error Twitter: " + "No connection");

            try {
                PostManager postManager = new PostManager(context);
                postManager.docFile();
                Status statusTW = postManager.getStatus(status);
                statusTW.setIsTW(2);
                statusTW.setManager(2);
                postManager.setStatus(statusTW);
                postManager.ghiFile();
            } catch (Exception e) {
                Log.d("myLog", "Error File:" + e.toString());
            }
        }
    }

    private class PostImage extends AsyncTask<Status, Void, Void> {
        boolean check = true;
        vn.sunnet.hungdh.socialmediaplatform.data.Status statusx;
        @Override
        protected Void doInBackground(vn.sunnet.hungdh.socialmediaplatform.data.Status... params) {
            vn.sunnet.hungdh.socialmediaplatform.data.Status status = params[0];
            statusx = status;
            String tweet = status.getStatus();
            File photo = new File(context.getFilesDir(), "hack.jpg");
            try {
                photo.createNewFile();
                FileOutputStream fos = new FileOutputStream(photo.getPath());
                fos.write(status.getImage());
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                Log.d("myLog", "Error");
            }

            TwitterSession session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
            TwitterAuthToken twitterAuthToken = session.getAuthToken();
            String token = twitterAuthToken.token;
            String secret = twitterAuthToken.secret;

            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(context.getResources().getString(R.string.TWITTER_KEY));
            configurationBuilder.setOAuthConsumerSecret(context.getResources().getString(R.string.TWITTER_SECRET));
            configurationBuilder.setOAuthAccessToken(token);
            configurationBuilder.setOAuthAccessTokenSecret(secret);
            Configuration configuration = configurationBuilder.build();
            twitter4j.Twitter twitter = new TwitterFactory(configuration).getInstance();
            StatusUpdate statusUD = new StatusUpdate(tweet);
            statusUD.setMedia(photo); // set the image to be uploaded here.
            try {
                twitter.updateStatus(statusUD);
            } catch (Exception e) {
                Log.d("myLog", "Error");
                check = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (check) {
                Toast.makeText(context, "Đã post Twitter!", Toast.LENGTH_SHORT).show();
                Log.d("myLog", "Posted Twitter: ");
                try {
                    PostManager postManager = new PostManager(context);
                    postManager.docFile();
                    vn.sunnet.hungdh.socialmediaplatform.data.Status statusTW = postManager.getStatus(statusx);
                    statusTW.setIsTW(1);
                    postManager.setStatus(statusTW);
                    postManager.ghiFile();
                } catch (Exception e) {
                    Log.d("myLog", "Error File:" + e.toString());
                }
            } else {
                Toast.makeText(context, "Có lỗi Post Twitter!", Toast.LENGTH_SHORT).show();
                Log.d("myLog", "Error Twitter: ");
                try {
                    PostManager postManager = new PostManager(context);
                    postManager.docFile();
                    vn.sunnet.hungdh.socialmediaplatform.data.Status statusTW = postManager.getStatus(statusx);
                    statusTW.setIsTW(2);
                    statusTW.setManager(2);
                    postManager.setStatus(statusTW);
                    postManager.ghiFile();
                } catch (Exception ex) {
                    Log.d("myLog", "Error File:" + ex.toString());
                }
            }
        }
    }
}
