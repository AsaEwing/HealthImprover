package com.asaewing.healthimprover.app2.Others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.asaewing.healthimprover.app2.MainActivity2;

import java.io.InputStream;

/**
 *
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;
    Bitmap mIcon = null;
    String key = null;

    public DownloadImageTask(String mkey, ImageView bmImage) {
        this.key = mkey;
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);

            //ProgressBarQrCode.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        MainActivity2.mInfoMap.IMput(key,mIcon);
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        //bmImage.setImageBitmap(result);
    }

    public Bitmap getDownloadBitmap () {
        return mIcon;
    }
}