package vn.sunnet.hungdh.socialmediaplatform.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import vn.sunnet.hungdh.socialmediaplatform.R;

/**
 * Created by HUNGDH on 7/22/2015.
 */
public class ChannelsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    private static final String[] CHANNELS = {"Facebook", "Twitter"};
    private static final int[] ICONS = {R.drawable.icon_facebook, R.drawable.icon_twitter};

    public ChannelsAdapter(Context context){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = layoutInflater.inflate(R.layout.listview_channel_layout, null);
        }
        ImageView iv_icon = (ImageView) listItem.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) listItem.findViewById(R.id.tv_name);
        TextView tv_logged = (TextView) listItem.findViewById(R.id.tv_logged);
        iv_icon.setImageResource(ICONS[position]);
        tv_name.setText(CHANNELS[position]);

        String email = "";
        if (position == 0) {
            SharedPreferences preferences = context.getSharedPreferences("my_facebook", Context.MODE_PRIVATE);
            email = preferences.getString("email", "");
            if (email.equals("")) email = "Not logged in";
            Log.d("myLog", "Email Adapter: " + email);
        }
        if (position == 1) {
            SharedPreferences preferences = context.getSharedPreferences("my_twitter", Context.MODE_PRIVATE);
            email = preferences.getString("username", "");
            if (email.equals("")) email = "Not logged in";
            Log.d("myLog", "Email Adapter: " + email);
        }
        tv_logged.setText(email);
        return listItem;
    }
}
