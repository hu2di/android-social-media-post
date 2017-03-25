package vn.sunnet.hungdh.socialmediaplatform.status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;

/**
 * Created by HUNGDH on 9/21/2015.
 */
public class MyFollowersAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private Vector<Status> myPost;

    public MyFollowersAdapter(Context context, Vector<Status> myPost){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myPost = myPost;
    }
    @Override
    public int getCount() {
        return myPost.size();
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
            listItem = layoutInflater.inflate(R.layout.listview_mypost_manager, null);
        }

        ImageView iv_image = (ImageView) listItem.findViewById(R.id.iv_image);
        TextView tv_status = (TextView) listItem.findViewById(R.id.tv_status);
        TextView tv_time = (TextView) listItem.findViewById(R.id.tv_time);
        ImageView iv_state = (ImageView) listItem.findViewById(R.id.iv_state);

        if (myPost.get(position).getImage() == null) {
            iv_image.setImageResource(R.drawable.everypost_icon);
        } else {
            iv_image.setImageResource(R.drawable.image_attached);
        }
        tv_status.setText(myPost.get(position).toString());
        tv_time.setText(myPost.get(position).getDateFormat());
        switch (myPost.get(position).getManager()) {
            case 0:
                iv_state.setImageResource(R.drawable.check_icon);
                break;
            case 1:
                iv_state.setImageResource(R.drawable.scheduled_icon);
                break;
            case 2:
                iv_state.setImageResource(R.drawable.canceled_icon);
                break;
            case 3:
                iv_state.setImageResource(R.drawable.trashed_filter_icon);
                break;
        }

        return listItem;
    }
}
