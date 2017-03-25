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
public class FragmentError extends Fragment {

    private ListView lvMyPostError;
    private PostManager postManagerError;
    private Vector<Status> myPostError;
    private LVPostAdapter adapterError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypost_error_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMyPostError = (ListView) view.findViewById(R.id.lv_mypost_error);
    }

    private void initError() {
        postManagerError = new PostManager(getActivity());
        postManagerError.docFile();
        myPostError = postManagerError.getErrorPost();

        if (myPostError.size() > 0) {
            adapterError = new LVPostAdapter(getActivity(), myPostError);
            lvMyPostError.setAdapter(adapterError);

            lvMyPostError.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new callDetailsPost().execute(position);
                }
            });

            //registerForContextMenu(lvMyPostError);
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
        AdapterView.AdapterContextMenuInfo infoError = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_delete:
                try {
                    Log.d("myLog", "Xóa" + infoError.position);
                    Status status = myPostError.get(infoError.position);
                    status.setManager(3);
                    postManagerError.setStatus(status);
                    postManagerError.ghiFile();
                    initError();
                } catch (Exception e) {
                    Log.d("myLog", "Error: " + e.toString());
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
*/
    class callDetailsPost extends AsyncTask<Integer, Void, Void> {
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
                intent.putExtra("status", myPostError.get(params[0]));
                startActivity(intent);
            } catch (Exception e) {
                Log.d("myLog", "Error Post: " + e.toString());
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
        initError();
    }
}
