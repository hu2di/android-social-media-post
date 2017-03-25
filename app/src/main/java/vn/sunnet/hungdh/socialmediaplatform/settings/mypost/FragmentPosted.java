package vn.sunnet.hungdh.socialmediaplatform.settings.mypost;

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
import java.util.Vector;

import vn.sunnet.hungdh.socialmediaplatform.R;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;

/**
 * Created by HUNGDH on 8/6/2015.
 */
public class FragmentPosted extends Fragment {

    private ListView lvMyPostPosted;
    private PostManager postManagerPosted;
    private Vector<Status> myPostPosted;
    private LVPostAdapter adapterPosted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypost_posted_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMyPostPosted = (ListView) view.findViewById(R.id.lv_mypost_posted);
    }

    private void initPosted() {
        postManagerPosted = new PostManager(getActivity());
        postManagerPosted.docFile();
        myPostPosted = postManagerPosted.getMyPost();

        if (myPostPosted.size() > 0) {
            adapterPosted = new LVPostAdapter(getActivity(), myPostPosted);
            lvMyPostPosted.setAdapter(adapterPosted);
            lvMyPostPosted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new callDetailsPost().execute(position);
                }
            });

            //registerForContextMenu(lvMyPostPosted);
        }
    }

  /*  @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.mypost_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo infoPosted = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_delete:
                try {
                    Log.d("myLog", "Xóa" + infoPosted.position);
                    Status status = myPostPosted.get(infoPosted.position);
                    status.setManager(3);
                    postManagerPosted.setStatus(status);
                    postManagerPosted.ghiFile();
                    initPosted();
                } catch (Exception e) {
                    Log.d("myLog", "Posted: " + e.toString());
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/

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
                intent.putExtra("status", myPostPosted.get(params[0]));
                startActivity(intent);
            } catch (Exception e) {
                Log.d("myLog", "Posted Post: " + e.toString());
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
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initPosted();
    }
}
