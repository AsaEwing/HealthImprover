package com.asaewing.healthimprover.Others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.asaewing.healthimprover.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asa on 2016/11/11.
 */

public class FoodMap {

    public String index_StoreName = "StoreName";
    public String index_FoodName = "FoodName";
    public String index_FoodAmount = "FoodAmount";
    public String index_FoodSize = "FoodSize";
    public String index_FoodSugar = "FoodSugar";
    public String index_FoodHotCold = "FoodHotCold";
    public String index_StoreId = "StoreId";
    public String index_FoodId = "FoodId";

    private List<FoodMapItem> mMap = new ArrayList<>();

    //TODO----put somethings----
    public void FMnewFoodPut (String StoreName,String FoodName
            ,String FoodAmount,String FoodSize
            ,String FoodSugar,String FoodHotCold
            ,String FoodRemarks) {

        mMap.add(new FoodMapItem(StoreName,FoodName
                ,FoodAmount,FoodSize,FoodSugar,FoodHotCold,FoodRemarks));

    }

    //TODO----get somethings----
    public List FMgetMap () {
        return mMap;
    }

    //TODO----have somethings----
    /*public int FMisHaveFood (String StoreName,String FoodName) {
        mMap.size()

        return mIM.containsKey(key);
    }

    //TODO----remove somethings----
    public void FMremoveFood (int index) {
        mMap.remove();
    }*/


    private class FoodMapItem {

        public String index_StoreName = "StoreName";
        public String index_FoodName = "FoodName";
        public String index_FoodAmount = "FoodAmount";
        public String index_FoodSize = "FoodSize";
        public String index_FoodSugar = "FoodSugar";
        public String index_FoodHotCold = "FoodHotCold";
        public String index_StoreId = "StoreId";
        public String index_FoodId = "FoodId";
        public String index_FoodRemarks = "FoodRemarks";

        String StoreName = "";
        String FoodName = "";
        String FoodAmount = "";
        String FoodSize = "";
        String FoodSugar = "";
        String FoodHotCold = "";
        String StoreId = "";
        String FoodId = "";
        String FoodRemarks = "";

        public FoodMapItem (String StoreName,String FoodName
                ,String FoodAmount,String FoodSize
                ,String FoodSugar,String FoodHotCold
                ,String FoodRemarks){
            if (StoreName==null) StoreName="";
            if (FoodName==null) FoodName="";
            if (FoodAmount==null) FoodAmount="";
            if (FoodSize==null) FoodSize="";
            if (FoodSugar==null) FoodSugar="";
            if (FoodHotCold==null) FoodHotCold="";
            if (FoodRemarks==null) FoodRemarks="";

            this.StoreName = StoreName;
            this.FoodName = FoodName;
            this.FoodAmount = FoodAmount;
            this.FoodSize = FoodSize;
            this.FoodSugar = FoodSugar;
            this.FoodHotCold = FoodHotCold;

            this.StoreId = MainActivity.helper.SAID_NameFindId(StoreName);
            this.FoodId = MainActivity.helper.FoodNameFindId(StoreName,FoodName);

            this.FoodRemarks = FoodRemarks;
        }

        public String getItem(String indexString){
            if (indexString.equals(index_StoreName)) {
                return this.StoreName;
            } else if (indexString.equals(index_FoodName)) {
                return this.FoodName;
            } else if (indexString.equals(index_FoodAmount)) {
                return this.FoodAmount;
            } else if (indexString.equals(index_FoodSize)) {
                return this.FoodSize;
            } else if (indexString.equals(index_FoodSugar)) {
                return this.FoodSugar;
            } else if (indexString.equals(index_FoodHotCold)) {
                return this.FoodHotCold;
            } else if (indexString.equals(index_StoreId)) {
                return this.StoreId;
            } else if (indexString.equals(index_FoodId)) {
                return this.FoodId;
            } else if (indexString.equals(index_FoodRemarks)) {
                return this.FoodRemarks;
            } else {
                return null;
            }
        }

        public void setItemOne(String indexString,String newString){
            if (indexString.equals(index_StoreName)) {
                if (!this.StoreName.equals(newString)){
                    this.StoreName = newString;

                    this.StoreId = MainActivity
                            .helper.SAID_NameFindId(newString);
                    this.FoodId = MainActivity
                            .helper.FoodNameFindId(this.StoreName,this.FoodName);
                }
                
            } else if (indexString.equals(index_FoodName)) {
                if (!this.FoodName.equals(newString)){
                    this.FoodName = newString;

                    this.FoodId = MainActivity
                            .helper.FoodNameFindId(this.StoreName,this.FoodName);
                }

            } else if (indexString.equals(index_FoodAmount)) {
                this.FoodAmount = newString;

            } else if (indexString.equals(index_FoodSize)) {
                this.FoodSize = newString;

            } else if (indexString.equals(index_FoodSugar)) {
                this.FoodSugar = newString;

            } else if (indexString.equals(index_FoodHotCold)) {
                this.FoodHotCold = newString;

            } else if (indexString.equals(index_FoodRemarks)) {
                this.FoodRemarks = newString;

            }
        }
    }

}
