package vn.sunnet.hungdh.socialmediaplatform.settings.mypost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.Vector;

import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;

/**
 * Created by HUNGDH on 8/6/2015.
 */
public class FragmentSchedule extends Fragment {

    private ListView lvMyPostSchedule;
    private PostManager postManagerSchedule;
    private Vector<Status> myPostSchedule;
    private LVPostAdapter adapterSchedule;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypost_schedule_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMyPostSchedule  = (ListView)view.findViewById(R.id.lv_mypost_schedule);
    }

    private void initSchedule() {
        postManagerSchedule = new PostManager(getActivity());
        postManagerSchedule.docFile();
        myPostSchedule = postManagerSchedule.getSchedulePost();

        if (myPostSchedule.size()>0) {
            adapterSchedule = new LVPostAdapter(getActivity(), myPostSchedule);
            lvMyPostSchedule.setAdapter(adapterSchedule);
            lvMyPostSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new callDetailsPost().execute(position);
                }
            });

            registerForContextMenu(lvMyPostSchedule);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.mypost_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_delete:
                try {
                    Log.d("myLog", "Xóa" + info.position);
                    Status status = myPostSchedule.get(info.position);
                    status.setManager(3);

                    postManagerSchedule.setStatus(status);
                    postManagerSchedule.ghiFile();

                    lvMyPostSchedule.setAdapter(null);
                    initSchedule();
                } catch (Exception e) {
                    Log.d("myLog", "Schedule: " + e.toString());
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private class callDetailsPost extends AsyncTask<Integer, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", "Đang tải... ");
            dialog.setCancelable(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailsPost.class);
                intent.putExtra("status", myPostSchedule.get(params[0]));
                startActivity(intent);
            } catch (Exception e) {
                Log.d("myLog", "Schedule Post: " + e.toString());
                getActivity().finish();
            }
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
        initSchedule();
    }
}
