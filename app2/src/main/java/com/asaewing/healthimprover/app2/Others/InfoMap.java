package com.asaewing.healthimprover.app2.Others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 *
 */
public class InfoMap {

    HashMap<String,String> mIM = new HashMap<>();

    //TODO----put somethings----
    public void IMput (String key, Object object) {
        String ss;
        if (object instanceof Bitmap) {
            ss= BitMapToString((Bitmap) object);
        } else {
            ss = object.toString();
        }

        if (IMisHaveKey(key)) {
            mIM.remove(key);
        }
        mIM.put(key,ss);
    }

    //TODO----get somethings----
    public String IMgetString (String key) {

        String ss = null;
        if (IMisHaveKey(key)) {
            ss = mIM.get(key);
        }
        return ss;
    }

    public int IMgetInt (String key) {

        int ii = 0;
        if (IMisHaveKey(key)) {
            ii = (int)Double.parseDouble(mIM.get(key));
        }
        return ii;
    }

    public float IMgetFloat (String key) {

        float ff = 0;
        if (IMisHaveKey(key)) {
            ff = Float.parseFloat(mIM.get(key));
        }
        return ff;
    }

    public boolean IMgetBoolean (String key) {

        boolean bb = false;
        if (IMisHaveKey(key)) {
            String tmp = mIM.get(key);
            if (!tmp.equals("0") && !tmp.equals("1")) {
                bb = Boolean.parseBoolean(mIM.get(key));
            } else if (tmp.equals("0")){
                bb = false;
            } else if (tmp.equals("1")){
                bb = true;
            }

        }
        return bb;
    }

    public Bitmap IMgetBitmap (String key) {

        Bitmap bitmap = null;
        if (IMisHaveKey(key)) {
            bitmap = StringToBitMap(mIM.get(key));
        }
        return bitmap;
    }

    //TODO----have somethings----
    public boolean IMisHaveKey (String key) {

        return mIM.containsKey(key);
    }

    //TODO----remove somethings----
    public void IMremove (String key) {
        mIM.remove(key);
    }

    //TODO----Other----
    public String BitMapToString(Bitmap bitmap){
        /*
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;*/

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        /*
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }*/

        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


    class InfoMapItem {

        private String mString = null;
        //private int mInt = Integer.parseInt(null);
        //private long mLong = Long.parseLong(null);
        private int mInt;
        private long mLong;

        //TODO----put somethings----
        public void IMIputString (String ss) {
            this.mString = ss;
        }

        public void IMIputInteger (int ii) {
            this.mInt = ii;
        }

        public void IMIputLong (long ll) {
            this.mLong = ll;
        }

        //TODO----get somethings----
        public String IMIgetString () {

            return this.mString;
        }

        public int IMIgetInteger () {

            return this.mInt;
        }

        public long IMIgetLong () {

            return this.mLong;
        }
    }
}
