package vn.sunnet.hungdh.socialmediaplatform.settings.mypost;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import vn.sunnet.hungdh.socialmediaplatform.data.Status;

/**
 * Created by HUNGDH on 8/3/2015.
 */
public class PostManager {

    private Vector<Status> myPost;
    private String filename = "post_file.dat";
    private Context context;

    public PostManager(Context context) {
        this.context = context;
        myPost = new Vector<Status>();
    }

    public void docFile() {
        try {
            File f = new File(filename);
            FileInputStream fIn = context.openFileInput(f.getPath());
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            myPost = (Vector<Status>) oIn.readObject();
            oIn.close();
            fIn.close();
            Log.d("myLog", "Readed");
        } catch (FileNotFoundException e) {
            ghiFile();
        } catch (Exception e) {
            Log.d("myLog", "Error Read File: " + e.toString());
        }
    }

    public void ghiFile() {
        try {
            File f = new File(context.getFilesDir(), filename);
            f.createNewFile();
            FileOutputStream fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(myPost);
            oOut.close();
            fOut.close();
            Log.d("myLog", "Writed");
        } catch (Exception e) {
            Log.d("myLog", "Error Write File" + e.toString());
        }
    }

    public void addStatus(Status status) {
        try {
            myPost.add(status);
        } catch (Exception e) {
            Log.d("myLog", "Error add Status" + e.toString());
        }
    }

    public void deleteStatus(Status status) {
        for (int i = 0; i < myPost.size(); i++) {
            if (myPost.get(i).getDate().equals(status.getDate())) {
                myPost.remove(i);
                return;
            }
        }
    }

    public Status getStatus(Status status) {
        for (int i = 0; i < myPost.size(); i++) {
            if (myPost.get(i).getDate().equals(status.getDate())) {
                return  myPost.get(i);
            }
        }
        return null;
    }

    public void setStatus(Status status) {
        for (int i = 0; i < myPost.size(); i++) {
            if (myPost.get(i).getDate().equals(status.getDate())) {
                myPost.set(i, status);
            }
        }
    }

    public Vector<Status> getMyPost() {
        Vector<Status> Post = new Vector<Status>();
        for (int i = myPost.size() - 1; i >= 0; i--) {
            if (myPost.get(i).getManager() == 0) {
                Post.add(myPost.get(i));
            }
        }
        return Post;
    }

    public Vector<Status> getSchedulePost() {
        Vector<Status> Post = new Vector<Status>();
        for (int i = myPost.size() - 1; i >= 0; i--) {
            if (myPost.get(i).getManager() == 1) {
                Post.add(myPost.get(i));
            }
        }
        return Post;
    }

    public Vector<Status> getErrorPost() {
        Vector<Status> Post = new Vector<Status>();
        for (int i = myPost.size() - 1; i >= 0; i--) {
            if (myPost.get(i).getManager() == 2) {
                Post.add(myPost.get(i));
            }
        }
        return Post;
    }

    public Vector<Status> getDeletePost() {
        Vector<Status> Post = new Vector<Status>();
        for (int i = myPost.size() - 1; i >= 0; i--) {
            if (myPost.get(i).getManager() == 3) {
                Post.add(myPost.get(i));
            }
        }
        return Post;
    }
}
