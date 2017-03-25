package vn.sunnet.hungdh.socialmediaplatform.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import vn.sunnet.hungdh.socialmediaplatform.data.Facebook;
import vn.sunnet.hungdh.socialmediaplatform.data.MyTwitter;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;
import vn.sunnet.hungdh.socialmediaplatform.settings.mypost.PostManager;

/**
 * Created by HUNGDH on 8/21/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        FacebookSdk.sdkInitialize(context.getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(" z4VmS1WPhSBIWfl3ixdoGE1ap", "TyFTnuIohwBhZRY44n8TGwwp0YnYxMkpGfelfe28aJVxuACMQy");
        Fabric.with(context, new Twitter(authConfig), new TweetComposer());

        Status status = new Status();
        try {
            Bundle bundle = intent.getExtras();
            status = (Status) bundle.get("statusSchedule");
            Log.d("myLog", "Receive: " + status.toString());
        } catch (Exception e) {
            Log.d("myLog", "Error Receiver: " + e.toString());
        }

        try {
            PostManager postManager = new PostManager(context);
            postManager.docFile();

            Status statusAR = postManager.getStatus(status);

            if (statusAR != null) {
                Toast.makeText(context, "Posting!!!", Toast.LENGTH_SHORT).show();
                Log.d("myLog", "Posting! " + statusAR.toString());

                statusAR.setManager(0);
                postManager.setStatus(statusAR);
                postManager.ghiFile();

                if (statusAR.getIsFB() == 1) {
                    try {
                        Facebook facebook = new Facebook(context);
                        facebook.postFacebook(statusAR);
                    } catch (Exception e) {
                        Log.d("myLog", "Error Post FB:" + e.toString());
                    }
                }

                if (statusAR.getIsTW() == 1) {
                    try {
                        MyTwitter twitter = new MyTwitter(context);
                        twitter.postTwitter(statusAR);
                    } catch (Exception e) {
                        Log.d("myLog", "Error Post TW:" + e.toString());
                    }
                }
            }

        } catch (Exception e) {
            Log.d("myLog", "Error File:" + e.toString());
        }
    }
}