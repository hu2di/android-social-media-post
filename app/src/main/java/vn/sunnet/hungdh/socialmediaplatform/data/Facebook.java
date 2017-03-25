package vn.sunnet.hungdh.socialmediaplatform.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.sunnet.hungdh.socialmediaplatform.settings.mypost.PostManager;

/**
 * Created by Dinh Huy Hung on 7/8/2015.
 */
public class Facebook {

    Context context;
    ConnectionCheck connectionCheck;

    public Facebook(Context context) {
        this.context = context;
        connectionCheck = new ConnectionCheck(context);
    }

    public void postFacebook(final Status status) {

        if (connectionCheck.isConnectingToInternet()) {
            Bundle params = new Bundle();
            params.putString("privacy", getPrivacy(status.getLevel()));

            if (status.getImage() == null) {
                params.putString("message", status.getStatus());

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/feed", params, HttpMethod.POST, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        getID(response, status);
                    }
                }
                ).executeAsync();
            } else {
                byte[] data = status.getImage();
                params.putString("caption", status.getStatus());
                params.putByteArray("picture", data);

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/photos", params, HttpMethod.POST, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        getID(response, status);
                    }
                }
                ).executeAsync();
            }
        } else {
            Toast.makeText(context, "Không có kết nối internet!", Toast.LENGTH_SHORT).show();
            Log.d("myLog", "Error Post Facebook:" + "No connection");

            try {
                PostManager postManager = new PostManager(context);
                postManager.docFile();
                Status statusFB = postManager.getStatus(status);
                statusFB.setIsFB(2);
                statusFB.setManager(2);
                statusFB.setIdFacebook("");
                postManager.setStatus(statusFB);
                postManager.ghiFile();
            } catch (Exception e) {
                Log.d("myLog", "Error File:" + e.toString());
            }
        }
    }

    public void getID(GraphResponse response, Status status) {
        String postID;

        PostManager postManager = new PostManager(context);
        Status statusFB = new Status();

        try {
            postManager.docFile();
            statusFB = postManager.getStatus(status);
        } catch (Exception e) {
            Log.d("myLog", "Error File:" + e.toString());
        }

        String s = response.toString();
        Pattern p = Pattern.compile("id\":\"([^\"]+)");
        Matcher m = p.matcher(s);

        if (m.find()) {
            postID = m.group(1);
            Toast.makeText(context, "Đã post Facebook!", Toast.LENGTH_SHORT).show();
            Log.d("myLog", "Posted Facebook" + response.toString());
            Log.d("myLog", "postID: " + postID);
            statusFB.setIsFB(1);
            statusFB.setIdFacebook(postID);
        } else {
            Toast.makeText(context, "Có lỗi post Facebook!", Toast.LENGTH_SHORT).show();
            Log.d("myLog", "Error Post Facebook" + response.toString());
            statusFB.setIsFB(2);
            statusFB.setManager(2);
            statusFB.setIdFacebook("");
        }

        try {
            postManager.setStatus(statusFB);
            postManager.ghiFile();
        } catch (Exception e) {
            Log.d("myLog", "Error File:" + e.toString());
        }
    }

    public String getPrivacy(int level) {
        JSONObject privacy = new JSONObject();
        try {
            switch (level) {
                case 0:
                    privacy.put("value", "SELF");
                    break;
                case 1:
                    privacy.put("value", "ALL_FRIENDS");
                    break;
                case 2:
                    privacy.put("value", "EVERYONE");
                    break;
            }
        } catch (JSONException e1) {
        }
        return privacy.toString();
    }
}
