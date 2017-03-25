package vn.sunnet.hungdh.socialmediaplatform.status;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;
import twitter4j.PagableResponseList;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import vn.sunnet.hungdh.socialmediaplatform.MainActivity;
import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.data.ConnectionCheck;
import vn.sunnet.hungdh.socialmediaplatform.data.Facebook;
import vn.sunnet.hungdh.socialmediaplatform.data.MyTwitter;
import vn.sunnet.hungdh.socialmediaplatform.settings.facebook.FacebookLogin;
import vn.sunnet.hungdh.socialmediaplatform.settings.mypost.PostManager;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;
import vn.sunnet.hungdh.socialmediaplatform.settings.twitter.TwitterLogin;

/**
 * Created by HUNGDH on 7/17/2015.
 */
public class FragmentStatus extends Fragment {

    private ImageButton btn_facebook, btn_twitter;
    private boolean isFacebook = false, isTwitter = false;
    private int stateFacebook = 0, stateTwitter = 0, stateImage = 0;

    private EditText et_status;
    private TextView tv_count_text;
    private ImageButton btn_delete;
    private Bitmap mBitmap = null;

    //private ImageView iv_post_switch;
    private TextView tv_keotha;
    private int countSocial = 0;
    private ImageButton btn_post, btn_schedule;

    private ImageButton btn_capture, btn_choose_image;

    public ImageButton btn_tag, btn_trend;
    public int stateTagTrend = 0;
    public RelativeLayout view_tag, view_trend;
    private ListView lv_tag, lv_trend;
    private ProgressBar pb_tag, pb_trend;
    private ArrayList<String> dataTag = new ArrayList<String>(), dataTrend = new ArrayList<String>();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PHOTO = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.TWITTER_KEY), getResources().getString(R.string.TWITTER_SECRET));
        Fabric.with(getActivity(), new Twitter(authConfig), new TweetComposer());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        tv_keotha = (TextView) view.findViewById(R.id.tv_keotha);
        tv_keotha.setVisibility(View.INVISIBLE);
        /*tv_keotha.setOnTouchListener(new View.OnTouchListener() {
            int initialX = 0;
            int initialY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int) event.getX();
                        //initialY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int currentX = (int) event.getX();
                        //int currentY = (int) event.getY();
                        v.setX(currentX);
                        v.setY(initialY);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setX(initialX);
                        v.setY(initialY);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/
        tv_keotha.setOnTouchListener(new DragExperimentTouchListener(tv_keotha.getX(), tv_keotha.getY()));
    }

    private class DragExperimentTouchListener implements View.OnTouchListener {
        boolean isDragging = false;
        float lastX;
        float lastY;
        float deltaX;
        final float X, Y;

        public DragExperimentTouchListener(float initalX, float initialY) {
            lastX = initalX;
            lastY = initialY;
            X = initalX;
            Y = initialY;
        }

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            int action = arg1.getActionMasked();

            if (action == MotionEvent.ACTION_DOWN && !isDragging) {
                isDragging = true;
                deltaX = arg1.getX();
                return true;
            } else if (isDragging) {
                if (action == MotionEvent.ACTION_MOVE) {
                    arg0.setX(arg0.getX() + (arg1.getX() - deltaX));
                    arg0.setY(arg0.getY());
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    isDragging = false;
                    //lastX = arg1.getX();
                    //lastY = arg1.getY();
                    arg0.setX(X + 220);
                    arg0.setY(arg0.getY());
                    //arg0.setY(Y);
                    return true;
                } else if (action == MotionEvent.ACTION_CANCEL) {
                    //arg0.setX(lastX);
                    //arg0.setY(lastY);
                    arg0.setX(X + 100);
                    arg0.setY(arg0.getY());
                    isDragging = false;
                    return true;
                }
            }
            return false;
        }
    }

    private void initUI(View view) {
        ImageButton btn_settings = (ImageButton) view.findViewById(R.id.btn_status_settings);
        btn_settings.setOnClickListener(buttonClick);

        btn_facebook = (ImageButton) view.findViewById(R.id.btn_status_facebook);
        btn_twitter = (ImageButton) view.findViewById(R.id.btn_status_twitter);
        btn_facebook.setOnClickListener(buttonClick);
        btn_twitter.setOnClickListener(buttonClick);

        btn_post = (ImageButton) view.findViewById(R.id.btn_post);
        btn_schedule = (ImageButton) view.findViewById(R.id.btn_schedule);
        btn_post.setOnClickListener(buttonClick);
        btn_schedule.setOnClickListener(buttonClick);

        et_status = (EditText) view.findViewById(R.id.et_status);
        tv_count_text = (TextView) view.findViewById(R.id.tv_count_text);
        btn_delete = (ImageButton) view.findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.INVISIBLE);
        btn_delete.setOnClickListener(buttonClick);
        et_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int lenght = 140 - s.length();
                tv_count_text.setText(String.valueOf(lenght));
                if (s.length() == 0) {
                    if (mBitmap == null) {
                        btn_delete.setVisibility(View.INVISIBLE);
                        btn_delete.setActivated(false);
                    } else {
                        btn_delete.setVisibility(View.VISIBLE);
                        btn_delete.setActivated(true);
                    }
                } else {
                    btn_delete.setVisibility(View.VISIBLE);
                    btn_delete.setActivated(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button_hide);
        loginButton.setActivated(false);
        loginButton.setVisibility(View.INVISIBLE);

        btn_capture = (ImageButton) view.findViewById(R.id.btn_capture);
        btn_choose_image = (ImageButton) view.findViewById(R.id.btn_choose_image);
        btn_capture.setOnClickListener(buttonClick);
        btn_choose_image.setOnClickListener(buttonClick);

        btn_tag = (ImageButton) view.findViewById(R.id.btn_tag);
        btn_trend = (ImageButton) view.findViewById(R.id.btn_trend);
        btn_tag.setOnClickListener(buttonClick);
        btn_trend.setOnClickListener(buttonClick);

        view_tag = (RelativeLayout) view.findViewById(R.id.view_tag);
        pb_tag = (ProgressBar) view.findViewById(R.id.pb_tag);
        lv_tag = (ListView) view.findViewById(R.id.lv_tag);
        lv_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String followers = "@";
                followers = followers + dataTag.get(position);
                et_status.append(followers);
                view_tag.setVisibility(View.INVISIBLE);
                btn_tag.setImageResource(R.drawable.arroba_icon);
                stateTagTrend = 0;
            }
        });

        view_trend = (RelativeLayout) view.findViewById(R.id.view_trend);
        pb_trend = (ProgressBar) view.findViewById(R.id.pb_trend);
        lv_trend = (ListView) view.findViewById(R.id.lv_trend);
        lv_trend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String trend;
                if (dataTrend.get(position).contains("#")) {
                    trend = dataTrend.get(position);
                } else {
                    trend = "#" + dataTrend.get(position);
                }
                et_status.append(trend);
                view_trend.setVisibility(View.INVISIBLE);
                btn_trend.setImageResource(R.drawable.hashtag_icon);
                stateTagTrend = 0;
            }
        });
    }

    View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_delete:
                    btnDeleteProcess();
                    break;

                case R.id.btn_post:
                    postToAll();
                    break;
                case R.id.btn_schedule:
                    schedule();
                    break;

                case R.id.btn_capture:
                    captureProcess();
                    break;
                case R.id.btn_choose_image:
                    chooseImageProcess();
                    break;

                case R.id.btn_status_settings:
                    ((MainActivity) getActivity()).viewPager.setCurrentItem(0, true);
                    break;

                case R.id.btn_status_facebook:
                    btnStateFacebook();
                    break;
                case R.id.btn_status_twitter:
                    btnStateTwitter();
                    break;

                case R.id.btn_tag:
                    animateTagTrend(1);
                    break;
                case R.id.btn_trend:
                    animateTagTrend(2);
                    break;
            }
        }
    };

    private void animateTagTrend(int i) {
        if (stateTagTrend == i) {
            view_tag.setVisibility(View.INVISIBLE);
            view_trend.setVisibility(View.INVISIBLE);
            btn_tag.setImageResource(R.drawable.arroba_icon);
            btn_trend.setImageResource(R.drawable.hashtag_icon);
            stateTagTrend = 0;
        } else {
            if (i == 1) {
                btn_tag.setImageResource(R.drawable.arroba_icon_sel);
                btn_trend.setImageResource(R.drawable.hashtag_icon);

                view_tag.setVisibility(View.VISIBLE);
                view_trend.setVisibility(View.INVISIBLE);

                if (dataTag.size() <= 0) {
                    new Tag().execute();
                }
            } else {
                btn_tag.setImageResource(R.drawable.arroba_icon);
                btn_trend.setImageResource(R.drawable.hashtag_icon_sel);

                view_tag.setVisibility(View.INVISIBLE);
                view_trend.setVisibility(View.VISIBLE);

                if (dataTrend.size() <= 0) {
                    new Trend().execute();
                }
            }
            stateTagTrend = i;
        }
    }

    private class Tag extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_tag.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            TwitterSession session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
            TwitterAuthToken twitterAuthToken = session.getAuthToken();
            String token = twitterAuthToken.token;
            String secret = twitterAuthToken.secret;

            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(getActivity().getResources().getString(R.string.TWITTER_KEY));
            configurationBuilder.setOAuthConsumerSecret(getActivity().getResources().getString(R.string.TWITTER_SECRET));
            configurationBuilder.setOAuthAccessToken(token);
            configurationBuilder.setOAuthAccessTokenSecret(secret);
            Configuration configuration = configurationBuilder.build();
            twitter4j.Twitter twitter = new TwitterFactory(configuration).getInstance();

            PagableResponseList<User> followersList;

            try
            {
                followersList = twitter.getFollowersList(session.getUserName(), -1);
                for (int i = 0; i < followersList.size(); i++)
                {
                    User user = followersList.get(i);
                    String name = user.getName();
                    String screen = user.getScreenName();
                    String url = user.getProfileImageURL();
                    dataTag.add(name);
                    Log.d("myLog", " Name " + i + " : " + name);
                }
            } catch (Exception e) {
                Log.d("myLog", "Error: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pb_tag != null) {
                pb_tag.setVisibility(View.INVISIBLE);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataTag);
            lv_tag.setAdapter(adapter);
        }
    }

    private class Trend extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_trend.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            TwitterSession session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
            MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
            CustomService customService = myTwitterApiClient.getCustomService();
            customService.trends(23424984, true, new Callback<List>() {
                @Override
                public void success(Result<List> result) {
                    String trends = result.data.toString();
                    Log.d("myLog", "Trends: " + trends);
                    Log.d("myLog", "Url Trends: " + result.response.getUrl());
                    Pattern p = Pattern.compile("name=([^,]+)");
                    Matcher m = p.matcher(trends);
                    while (m.find()) {
                        Log.d("myLog", "Trends: " + m.group(1));
                        dataTrend.add(m.group(1));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataTrend);
                    lv_trend.setAdapter(adapter);
                }

                @Override
                public void failure(TwitterException e) {
                    Log.d("myLog", "Trends Error: " + e.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pb_trend != null) {
                pb_trend.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void btnDeleteProcess() {
        AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
        delete.setTitle("Xóa");
        delete.setMessage("Bạn muốn xóa?");
        delete.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAll();
            }
        });
        delete.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        delete.show();
    }

    private void checkFacebookLogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if (email.equals("")) {
            isFacebook = false;
        } else {
            isFacebook = true;
        }
    }

    private void btnStateFacebook() {
        if (isFacebook) {
            if (stateFacebook == 0) {
                btn_facebook.setImageResource(R.drawable.icon_facebook);
                stateFacebook = 1;
                countSocial++;
                tv_keotha.setText("" + countSocial);
            } else {
                btn_facebook.setImageResource(R.drawable.icon_facebook_des);
                stateFacebook = 0;
                countSocial--;
                tv_keotha.setText("" + countSocial);
            }
        } else {
            new callFacebook().execute();
        }
    }

    private class callFacebook extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", "Đang tải... ");
            dialog.setCancelable(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent intent = new Intent(getActivity(), FacebookLogin.class);
            getActivity().startActivity(intent);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    private void btnStateTwitter() {
        if (isTwitter) {
            if (stateTwitter == 0) {
                btn_twitter.setImageResource(R.drawable.icon_twitter);
                stateTwitter = 1;
                countSocial++;
                tv_keotha.setText("" + countSocial);
            } else {
                btn_twitter.setImageResource(R.drawable.icon_twitter_des);
                stateTwitter = 0;
                countSocial--;
                tv_keotha.setText("" + countSocial);
            }
        } else {
            new callTwitter().execute();
        }
    }

    private class callTwitter extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", "Đang tải... ");
            dialog.setCancelable(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent intent = new Intent(getActivity(), TwitterLogin.class);
            getActivity().startActivity(intent);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    private void checkTwitterLogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("my_twitter", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        if (username.equals("")) {
            isTwitter = false;
        } else {
            isTwitter = true;
        }
    }

    private void savingPrivacy() {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences("my_state_button", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putInt("stateFacebook", stateFacebook);
        editor.putInt("stateTwitter", stateTwitter);
        editor.commit();
    }

    private void restoringPrivacy() {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences("my_state_button", Context.MODE_PRIVATE);

        countSocial = 0;
        stateFacebook = preferences.getInt("stateFacebook", 0);
        if (isFacebook) {
            if (stateFacebook == 1) {
                btn_facebook.setImageResource(R.drawable.icon_facebook);
                countSocial++;
            } else {
                btn_facebook.setImageResource(R.drawable.icon_facebook_des);
            }
        } else {
            stateFacebook = 0;
            btn_facebook.setImageResource(R.drawable.icon_facebook_des);
        }


        stateTwitter = preferences.getInt("stateTwitter", 0);
        if (isTwitter) {
            if (stateTwitter == 1) {
                btn_twitter.setImageResource(R.drawable.icon_twitter);
                countSocial++;
            } else {
                btn_twitter.setImageResource(R.drawable.icon_twitter_des);
            }
        } else {
            stateTwitter = 0;
            btn_twitter.setImageResource(R.drawable.icon_twitter_des);
        }

        tv_keotha.setText("" + countSocial);
    }

    private void schedule() {
        if (stateFacebook == 0 && stateTwitter == 0) {
            Toast.makeText(getActivity(), "Bạn chưa chọn mạng xã hội nào!", Toast.LENGTH_SHORT).show();
        } else {
            if ((mBitmap == null) && (et_status.getText().toString().trim().length() <= 0)) {
                Toast.makeText(getActivity(), "Bạn chưa viết status hoặc chọn ảnh!", Toast.LENGTH_SHORT).show();
            } else {
                scheduleDialog();
            }
        }
    }

    private void scheduleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_datetimepicker_layout, null);
        final DatePicker dp = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker tp = (TimePicker) dialogView.findViewById(R.id.time_picker);
        dialog.setView(dialogView);
        dialog.setTitle("Schedule");
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                String strDateTime = dp.getYear() + "-" + (dp.getMonth() + 1) + "-" + dp.getDayOfMonth() + " " + tp.getCurrentHour() + ":" + tp.getCurrentMinute();
                //Toast.makeText(getActivity(), strDateTime, Toast.LENGTH_LONG).show();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, dp.getYear());
                calendar.set(Calendar.MONTH, dp.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                calendar.set(Calendar.MINUTE, tp.getCurrentMinute());

                String stt = et_status.getText().toString();
                SharedPreferences preferences = getActivity().getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
                int level = preferences.getInt("level", 1);
                Date date = calendar.getTime();
                Status status = new Status(stt, level, mBitmap, date, 1, stateFacebook, stateTwitter);

                try {
                    PostManager postManager = new PostManager(getActivity());
                    postManager.docFile();
                    postManager.addStatus(status);
                    postManager.ghiFile();
                } catch (Exception e) {
                    Log.d("myLog", "Error File:" + e.toString());
                }

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
                myIntent.putExtra("statusSchedule", status);

                final int _id = (int) System.currentTimeMillis();
                PendingIntent appIntent = PendingIntent.getBroadcast(getActivity(), _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), appIntent);

                clearAll();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
                delete.setTitle("Schedule");
                delete.setMessage("Bạn muốn hủy?");
                delete.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                delete.setNegativeButton("Post now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Post !!!", Toast.LENGTH_SHORT).show();
                    }
                });
                delete.show();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void postFB(Status status) {
        try {
            Facebook facebook = new Facebook(getActivity());
            facebook.postFacebook(status);
        } catch (Exception e) {
            Log.d("myLog", "Error Post FB:" + e.toString());
        }
    }

    private void postTW(Status status) {
        try {
            MyTwitter twitter = new MyTwitter(getActivity());
            twitter.postTwitter(status);
        } catch (Exception e) {
            Log.d("myLog", "Error Post TW:" + e.toString());
        }
    }

    private void postToAll() {
        if (stateFacebook == 0 && stateTwitter == 0) {
            Toast.makeText(getActivity(), "Bạn chưa chọn mạng xã hội nào!", Toast.LENGTH_SHORT).show();
        } else {
            if ((mBitmap == null) && (et_status.getText().toString().trim().length() <= 0)) {
                Toast.makeText(getActivity(), "Bạn chưa viết status hoặc chọn ảnh!", Toast.LENGTH_SHORT).show();
            } else {
                ConnectionCheck connectionCheck = new ConnectionCheck(getActivity());
                if (!connectionCheck.isConnectingToInternet()) {
                    AlertDialog.Builder post = new AlertDialog.Builder(getActivity());
                    post.setTitle("Post");
                    post.setMessage("Không có kết nối internet. Bạn muốn đặt lịch?");
                    post.setPositiveButton("Đặt lịch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scheduleDialog();
                        }
                    });
                    post.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    post.show();
                } else {
                    String stt = et_status.getText().toString();
                    SharedPreferences preferences = getActivity().getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
                    int level = preferences.getInt("level", 1);
                    Status status = new Status(stt, level, mBitmap, new Date(), 0, stateFacebook, stateTwitter);

                    try {
                        PostManager postManager = new PostManager(getActivity());
                        postManager.docFile();
                        postManager.addStatus(status);
                        postManager.ghiFile();
                    } catch (Exception e) {
                        Log.d("myLog", "Error File:" + e.toString());
                    }

                    if (stateFacebook == 1) {
                        postFB(status);
                    }
                    if (stateTwitter == 1) {
                        postTW(status);
                    }

                    clearAll();
                }
            }
        }
    }

    private void clearAll() {
        et_status.setText("");
        mBitmap = null;
        stateImage = 0;
        btn_capture.setImageResource(R.drawable.images_icon);
        btn_choose_image.setImageResource(R.drawable.album_icon);
        btn_delete.setVisibility(View.INVISIBLE);
        btn_delete.setActivated(false);
    }

    private void captureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void captureProcess() {
        if (stateImage == 0) {
            captureIntent();
        } else {
            AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
            delete.setTitle("Images");
            delete.setMessage("Bạn muốn xóa ảnh?");
            delete.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stateImage = 0;
                    mBitmap = null;
                    btn_capture.setImageResource(R.drawable.images_icon);
                    btn_choose_image.setImageResource(R.drawable.album_icon);
                    captureIntent();
                }
            });
            delete.setNegativeButton("Hủy xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            delete.show();
        }
    }

    private void chooseImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    private void chooseImageProcess() {
        if (stateImage == 0) {
            chooseImage();
        } else {
            AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
            delete.setTitle("Images");
            delete.setMessage("Bạn muốn xóa ảnh?");
            delete.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stateImage = 0;
                    mBitmap = null;
                    btn_capture.setImageResource(R.drawable.images_icon);
                    btn_choose_image.setImageResource(R.drawable.album_icon);
                    chooseImage();
                }
            });
            delete.setNegativeButton("Hủy xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            delete.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkFacebookLogin();
        checkTwitterLogin();
        restoringPrivacy();
    }

    @Override
    public void onPause() {
        super.onPause();
        savingPrivacy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            if (mBitmap != null) {
                stateImage = 1;
                btn_capture.setImageResource(R.drawable.images_icon_sel);
                btn_delete.setVisibility(View.VISIBLE);
                btn_delete.setActivated(true);
            }
        }

        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            Log.d("myLog", "FilePath: " + filePath);
            cursor.close();
            mBitmap = BitmapFactory.decodeFile(filePath);
            if (mBitmap != null) {
                stateImage = 1;
                btn_choose_image.setImageResource(R.drawable.album_icon_sel);
                btn_delete.setVisibility(View.VISIBLE);
                btn_delete.setActivated(true);
            }
        }
    }
}
