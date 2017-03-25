package vn.sunnet.hungdh.socialmediaplatform.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dinh Huy Hung on 7/8/2015.
 */
public class Status implements Serializable {
    private String status;
    private int level;
    private byte[] image;
    private Date date;
    private int manager;
    private int isFB;
    private int isTW;
    private String idFacebook;

    public Status() {
        status = "";
        level = 2;
        image = null;
        date = new Date();
        manager = 0;
        isFB = 0;
        isTW = 0;
        idFacebook = "";
    }

    public Status(String status, int level, Bitmap image, Date date, int manager, int isFB, int isTW) {
        this.status = status;
        this.level = level;
        if (image == null) {
            this.image = null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            this.image = baos.toByteArray();
        }
        this.date = date;
        this.manager = manager;
        this.isFB = isFB;
        this.isTW = isTW;
    }

    public String getStatus() {
        return status;
    }

    public int getLevel() {
        return level;
    }

    public byte[] getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public String getDateFormat() {
        String strDateFormat = "dd/MM/yyyy hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date);
    }

    public int getManager() {
        return manager;
    }

    public int getIsFB() {
        return isFB;
    }

    public int getIsTW() {
        return isTW;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public void setIsFB(int isFB) {
        this.isFB = isFB;
    }

    public void setIsTW(int isTW) {
        this.isTW = isTW;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public String toString() {
        if (status.length() >= 17) {
            String str = status.substring(0, 17);
            str = str + "...";
            return str;
        } else {
            return status;
        }
    }
}
