package vn.sunnet.hungdh.socialmediaplatform.settings.facebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONObject;

import java.util.Arrays;

import vn.sunnet.hungdh.socialmediaplatform.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class FacebookLoginFragment extends Fragment {

    private TextView tv_username;
    private TextView tv_email;
    private int level = 1;
    RadioButton _public;
    RadioButton _friends;
    RadioButton _only_me;

    private LoginButton loginButton;
    private boolean LOGIN = false;

    private CallbackManager mCallbackManager;

    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            LOGIN = true;
            setPermissions();
            displayProfileMessage(com.facebook.Profile.getCurrentProfile());
        }

        @Override
        public void onCancel() {
            Log.d("myLog", "LoginActivity Facebook Cancel");
            getActivity().finish();
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("myLog", "Error Login FB" + e.getCause().toString());
            getActivity().finish();
        }
    };

    public FacebookLoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayProfileMessage(newProfile);
            }
        };

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebook_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("email");

        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
        loginButton.setVisibility(View.INVISIBLE);

        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_email = (TextView) view.findViewById(R.id.tv_email);

        RadioGroup rg_privacy = (RadioGroup) view.findViewById(R.id.rg_privacy);
        rg_privacy.setOnCheckedChangeListener(radioClick);

        _public = (RadioButton) view.findViewById(R.id.rb_public);
        _friends = (RadioButton) view.findViewById(R.id.rb_friends);
        _only_me = (RadioButton) view.findViewById(R.id.rb_only_me);

        TextView tv_facebook_remove = (TextView)view.findViewById(R.id.tv_facebook_remove);
        tv_facebook_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_username.setText("");
                tv_email.setText("");
                LoginManager.getInstance().logOut();
                getActivity().finish();
            }
        });
    }

    private void setCheck() {
        restoringPrivacy();
        switch (level) {
            case 0:
                _only_me.setChecked(true);
                break;
            case 1:
                _friends.setChecked(true);
                break;
            case 2:
                _public.setChecked(true);
                break;
        }
    }

    RadioGroup.OnCheckedChangeListener radioClick = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.rb_public:
                    level = 2;
                    break;
                case R.id.rb_friends:
                    level = 1;
                    break;
                case R.id.rb_only_me:
                    level = 0;
                    break;
            }
        }
    };

    private void displayProfileMessage(Profile profile) {
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
            tv_username.setText(profile.getName());
            SharedPreferences preferences = getActivity().getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
            String email = preferences.getString("email", "");
            tv_email.setText(email);
            getEmail();
        } else {
            if (!LOGIN) loginButton.callOnClick();
            tv_username.setText("");
            tv_email.setText("");
        }
    }

    private void getEmail() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        String email;
                        try {
                            JSONObject jsonObject = new JSONObject(object.toString());
                            email = jsonObject.getString("email");
                            Log.d("myLog", email);
                        } catch (Exception e) {
                            Log.d("myLog", "Error JSON " + e.toString());
                            email = "";
                        }
                        if (!email.equals("")) {
                            tv_email.setText(email);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void savingPrivacy() {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putInt("level", level);
        String email = tv_email.getText().toString();
        editor.putString("email", email);
        editor.commit();
    }

    private void restoringPrivacy() {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
        level = preferences.getInt("level", 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayProfileMessage(com.facebook.Profile.getCurrentProfile());
        setCheck();
    }

    @Override
    public void onPause() {
        super.onPause();
        savingPrivacy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    public void setPermissions() {
        String permissions = AccessToken.getCurrentAccessToken().getPermissions().toString();
        Log.d("myLog", "set Permissions 1");
        final String PUBLISH_PERMISSIONS = "publish_actions";
        if (permissions.lastIndexOf(PUBLISH_PERMISSIONS) < 0) {
            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            Log.d("myLog", "set Permissions 2");
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            Log.d("myLog", " request: " + requestCode + " result: " + requestCode + " data: " + data);
        } catch (Exception e) {
            Log.d("myLog", "Error Login FB" + e.toString());
            return;
        }
    }
}
