package vn.sunnet.hungdh.socialmediaplatform.settings.mypost;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.data.Facebook;
import vn.sunnet.hungdh.socialmediaplatform.data.MyTwitter;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;
import vn.sunnet.hungdh.socialmediaplatform.status.AlarmReceiver;

/**
 * Created by HUNGDH on 8/7/2015.
 */
public class DetailsPost extends Activity {

    private Status status;

    private TextView tv_likes;
    private TextView tv_comments;

    private ImageView iv_details_twitter, iv_details_check_twitter, iv_details_facebook, iv_details_check_facebook;

    private TextView tv_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_post_layout);

        Bundle data = getIntent().getExtras();
        status = (Status) data.getSerializable("status");
        Log.d("myLog", "Details: " + status.getStatus());

        TextView tv_details_date = (TextView) findViewById(R.id.tv_details_date);
        TextView tv_details_status = (TextView) findViewById(R.id.tv_details_status);
        ImageView iv_details_status = (ImageView) findViewById(R.id.iv_details_image);
        tv_details_date.setText(status.getDateFormat());
        tv_details_status.setText(status.getStatus());
        if (status.getImage() != null) {
            iv_details_status.setImageBitmap(status.getBitmap());
        }

        iv_details_twitter = (ImageView) findViewById(R.id.iv_details_twitter);
        iv_details_check_twitter = (ImageView) findViewById(R.id.iv_details_check_twitter);
        iv_details_facebook = (ImageView) findViewById(R.id.iv_details_facebook);
        iv_details_check_facebook = (ImageView) findViewById(R.id.iv_details_check_facebook);

        tv_likes = (TextView) findViewById(R.id.tv_likes);
        tv_comments = (TextView) findViewById(R.id.tv_comments);

        tv_retry = (TextView) findViewById(R.id.tv_retry);

        initUI();

        manager0();

        manager1();

        manager2();

        manager3();
    }

    private void manager0() {
        if ((status.getIsFB() == 1) && (status.getManager() == 0)) {
            getLike();
            getComments();
        }
    }

    private void manager1() {
        if (status.getManager() == 1) {
            iv_details_check_facebook.setVisibility(View.INVISIBLE);
            iv_details_check_twitter.setVisibility(View.INVISIBLE);
        }
    }

    private void manager2() {
        if (status.getManager() == 2) {
            tv_retry.setText("Retry");
            tv_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PostManager postManager = new PostManager(getApplicationContext());
                        postManager.docFile();
                        Status statusER = postManager.getStatus(status);
                        statusER.setManager(0);
                        postManager.setStatus(statusER);
                        postManager.ghiFile();
                    } catch (Exception e) {
                        Log.d("myLog", "Error File:" + e.toString());
                    }

                    if (status.getIsFB() == 2) {
                        try {
                            Facebook facebook = new Facebook(getApplicationContext());
                            facebook.postFacebook(status);
                        } catch (Exception e) {
                            Log.d("myLog", "Error Post FB:" + e.toString());
                        }
                    }

                    if (status.getIsTW() == 2) {
                        try {
                            MyTwitter twitter = new MyTwitter(getApplicationContext());
                            twitter.postTwitter(status);
                        } catch (Exception e) {
                            Log.d("myLog", "Error Post TW:" + e.toString());
                        }
                    }

                    finish();
                }
            });
        }
    }

    private void manager3() {
        if (status.getManager() == 3) {
            iv_details_check_facebook.setVisibility(View.INVISIBLE);
            iv_details_check_twitter.setVisibility(View.INVISIBLE);

            tv_retry.setText("Schedule");
            tv_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DetailsPost.this);
                    final LayoutInflater inflater = DetailsPost.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_datetimepicker_layout, null);
                    final DatePicker dp = (DatePicker) dialogView.findViewById(R.id.date_picker);
                    final TimePicker tp = (TimePicker) dialogView.findViewById(R.id.time_picker);
                    dialog.setView(dialogView);
                    dialog.setTitle("Schedule");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(DetailsPost.this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();

                            PostManager postManager = new PostManager(DetailsPost.this);
                            postManager.docFile();
                            postManager.deleteStatus(status);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, dp.getYear());
                            calendar.set(Calendar.MONTH, dp.getMonth());
                            calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                            calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                            calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
                            Date date = calendar.getTime();

                            Status statusRS = status;
                            Log.d("myLog", "Log1: " + status.toString() + status.getDate() + status.getManager() + statusRS.toString() + statusRS.getDate());
                            statusRS.setManager(1);
                            statusRS.setDate(date);
                            Log.d("myLog", "Log2: " + status.toString() + status.getDate() + status.getManager() + statusRS.toString() + statusRS.getDate());

                            postManager.addStatus(statusRS);
                            postManager.ghiFile();

                            AlarmManager alarmManager = (AlarmManager) DetailsPost.this.getSystemService(Context.ALARM_SERVICE);

                            Intent myIntent = new Intent(DetailsPost.this, AlarmReceiver.class);
                            myIntent.putExtra("statusSchedule", statusRS);

                            final int _id = (int) System.currentTimeMillis();
                            PendingIntent appIntent = PendingIntent.getBroadcast(DetailsPost.this, _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), appIntent);

                            finish();
                        }
                    });

                    dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dialog.create();
                    dialog.show();
                }
            });
        }
    }

    private void initUI() {
        switch (status.getIsTW()) {
            case 0:
                break;
            case 1:
                iv_details_twitter.setImageResource(R.drawable.icon_twitter);
                iv_details_check_twitter.setImageResource(R.drawable.check_icon);
                break;
            case 2:
                iv_details_twitter.setImageResource(R.drawable.icon_twitter);
                break;
        }

        switch (status.getIsFB()) {
            case 0:
                break;
            case 1:
                iv_details_facebook.setImageResource(R.drawable.icon_facebook);
                iv_details_check_facebook.setImageResource(R.drawable.check_icon);
                break;
            case 2:
                iv_details_facebook.setImageResource(R.drawable.icon_facebook);
                break;
        }
    }

    private void getLike() {
        String postID = status.getIdFacebook();
        Bundle params = new Bundle();
        params.putString("fields", "likes");
        new GraphRequest(AccessToken.getCurrentAccessToken(), postID, params, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                Log.d("myLog", "Likes: " + response.toString());

                String s = response.toString();
                Pattern p = Pattern.compile("name\":\"([^\"]+)");
                Matcher m = p.matcher(s);
                int i = 0;
                while (m.find()) {
                    i++;
                }
                tv_likes.setText("Likes: " + i);
            }
        }
        ).executeAsync();
    }

    private void getComments() {
        String postID = status.getIdFacebook();
        Bundle params = new Bundle();
        params.putString("fields", "comments");
        new GraphRequest(AccessToken.getCurrentAccessToken(), postID, params, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                Log.d("myLog", "Comments: " + response.toString());

                String s = response.toString();
                Pattern p = Pattern.compile("name\":\"([^\"]+)");
                Matcher m = p.matcher(s);
                int i = 0;
                while (m.find()) {
                    i++;
                }
                tv_comments.setText("Comments: " + i);
            }
        }
        ).executeAsync();
    }
}
