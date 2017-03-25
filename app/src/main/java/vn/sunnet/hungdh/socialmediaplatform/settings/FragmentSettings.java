package vn.sunnet.hungdh.socialmediaplatform.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.settings.facebook.FacebookLogin;
import vn.sunnet.hungdh.socialmediaplatform.settings.mypost.MyPost;
import vn.sunnet.hungdh.socialmediaplatform.settings.twitter.TwitterLogin;

/**
 * Created by HUNGDH on 7/17/2015.
 */
public class FragmentSettings extends Fragment {
    ListView lv_channels;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv_channels = (ListView) view.findViewById(R.id.lv_channels);
        //initChannels();

        TableLayout myPost = (TableLayout)view.findViewById(R.id.mypost);
        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLog", "Call My Post Activity");
                new callMyPost().execute();
            }
        });
    }

    private void initChannels() {
        ChannelsAdapter channelsAdapter = new ChannelsAdapter(this.getActivity());
        lv_channels.setAdapter(channelsAdapter);
        lv_channels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log.d("myLog", "Facebook Intent Call");
                        new callFacebook().execute();
                        break;
                    case 1:
                        new callTwitter().execute();
                        Log.d("myLog", "Twitter Intent Call");
                        break;
                }
            }
        });
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
            if (dialog!=null) {
                dialog.dismiss();
            }
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
            if (dialog!=null) {
                dialog.dismiss();
            }
        }
    }

    private class callMyPost extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", "Đang tải... ");
            dialog.setCancelable(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent myPost = new Intent(getActivity(), MyPost.class);
            getActivity().startActivity(myPost);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog!=null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initChannels();
    }
}
