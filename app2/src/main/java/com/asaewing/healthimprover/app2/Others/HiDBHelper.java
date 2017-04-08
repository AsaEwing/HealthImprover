package com.asaewing.healthimprover.app2.Others;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asaewing.healthimprover.app2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HiDBHelper extends SQLiteOpenHelper{

    private final static String DATABASE_NAME = "hi_database";
    private final static int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private Context context;

    //HiApp Info
    public final static String TABLE_NAME_Hi = "Hi_table";
    public final static String KEY_Hi_Index = "Hi_index";
    public final static String KEY_Flag_UseFirst = "UseFirst_flag";
    public final static String KEY_Flag_SAID = "SAID_Data_Flag";
    public final static String KEY_Flag_FAID = "FAID_Data_Flag";
    public final static String KEY_AC_uid = "uid";
    public final static String KEY_AC_initDate = "register_time";
    public final static String KEY_AC_FCM_id = "FCM_id";
    public final static String KEY_AC_LoginType = "login_type";
    public final static String KEY_AC_Locale = "locale";
    public final static String KEY_AC_Name = "user_name";
    public final static String KEY_AC_Birthday = "birthday";
    public final static String KEY_AC_Gender = "gender";
    public final static String KEY_AC_Height = "height";
    public final static String KEY_AC_Weight = "weight";
    public final static String KEY_AC_WakeTime = "recently_wake_time";
    public final static String KEY_AC_WakeTime48 = "recently_wake_time48";
    public final static String KEY_AC_Image = "AC_Image";
    public final static String KEY_AC_Image_url = "picture_url";
    public final static String KEY_AC_Account = "account";
    public final static String KEY_AC_GoogleMail = "AC_Mail_google";
    public final static String KEY_AC_FacebookMail = "AC_Mail_facebook";

    public final static String KEY_BI_Height_before = "BI_Height_before";
    public final static String KEY_BI_Weight_before = "BI_Weight_before";
    public final static String KEY_BI_Step_before = "BI_Step_before";
    public final static String KEY_BI_CalIn_before = "BI_CalIn_before";
    public final static String KEY_BI_CalOut_before = "BI_CalOut_before";

    public final static String KEY_BI_Height_before_H = "BI_Height_before_H";
    public final static String KEY_BI_Height_before_L = "BI_Height_before_L";
    public final static String KEY_BI_Weight_before_H = "BI_Weight_before_H";
    public final static String KEY_BI_Weight_before_L = "BI_Weight_before_L";

    //Note
    private final static String TABLE_NAME_note = "note_table";
    private final static String FEILD_note_ID = "_id";
    private final static String FEILD_note_TEXT = "item_text";
    //said
    public final static String TABLE_NAME_SAID = "SAID_table";
    private final static String KEY_SAID_Index = "Store_index";
    public final static String KEY_SAID_Store_ID = "Store_ID";
    public final static String KEY_SAID_Store_Name = "Store_Name";
    private final static String KEY_SAID_Store_Code1 = "Store_Code1";
    private final static String KEY_SAID_Store_Code2 = "Store_Code2";
    //faid
    public final static String TABLE_NAME_FAID = "FAID_table";
    private final static String KEY_FAID_Index = "menu_index";
    private final static String KEY_FAID_IndexID = "menu_indexID";
    public final static String KEY_FAID_Basic_ID = "Basic_ID";
    public final static String KEY_FAID_Basic_Name = "Basic_Name";
    private final static String KEY_FAID_Basic_Weight_g = "Basic_Weight_g";
    private final static String KEY_FAID_Basic_Price_TWD = "Basic_Price_TWD";
    private final static String KEY_FAID_Label_CalNormal_kcal = "Label_Cal_kcal";
    private final static String KEY_FAID_Label_CalNoSa_kcal = "Label_CalNoSa_kcal";

    //in cal
    public final static String TABLE_NAME_CalIn = "CalIn_table";
    public final static String KEY_CalIn_Index = "CalIn_Index";
    public final static String KEY_CalIn_Date = "CalIn_Date";
    public final static String KEY_CalIn_Time = "CalIn_Time";
    public final static String KEY_CalIn_Time48 = "CalIn_Time48";
    public final static String KEY_CalIn_IndexID = "CalIn_IndexID";
    public final static String KEY_CalIn_RID = "CalIn_RID";
    public final static String KEY_CalIn_FoodID = "CalIn_FoodID";
    public final static String KEY_CalIn_Rname = "CalIn_Rname";
    public final static String KEY_CalIn_Fname = "CalIn_Fname";
    public final static String KEY_CalIn_CG = "CalIn_CG";
    public final static String KEY_CalIn_Amount = "CalIn_Amount";
    public final static String KEY_CalIn_Size = "CalIn_Size";
    public final static String KEY_CalIn_Sugar = "CalIn_Sugar";
    public final static String KEY_CalIn_HotCold = "CalIn_HotCold";
    public final static String KEY_CalIn_oneCal = "CalIn_oneCal";
    public final static String KEY_Cloud_CalIn = "KC_CalIn";

    //out cal
    public final static String TABLE_NAME_CalOut = "CalOut_table";
    public final static String KEY_CalOut_Index = "CalOut_Index";//**
    public final static String KEY_CalOut_Date = "CalOut_Date";
    public final static String KEY_CalOut_Time = "CalOut_Time";
    public final static String KEY_CalOut_Time48 = "CalOut_Time48";
    public final static String KEY_CalOut_IndexID = "CalOut_IndexID";//**
    public final static String KEY_CalOut_Sport = "CalOut_Sport";
    public final static String KEY_CalOut_Cal = "CalOut_Cal";
    public final static String KEY_CalOut_HeartRate = "CalOut_HeartRate";
    public final static String KEY_CalOut_Strength = "CalOut_Strength";
    public final static String KEY_CalOut_Continue = "CalOut_Continue";
    public final static String KEY_CalOut_Distance = "CalOut_Distance";
    public final static String KEY_Cloud_CalOut = "KC_CalOut";

    public final static String KEY_CalOut_Sport_Run = "run";
    public final static String KEY_CalOut_Sport_Basketball = "basketball";
    public final static String KEY_CalOut_Sport_Volleyball = "volleyball";
    public final static String KEY_CalOut_Sport_Tennis = "tennis";
    public final static String KEY_CalOut_Sport_Badminton = "badminton";
    public final static String KEY_CalOut_Sport_Swim = "swim";
    public final static String KEY_CalOut_Sport_Bike = "bike";
    public final static String KEY_CalOut_Sport_Pingpong = "pingpong";
    public final static String KEY_CalOut_Sport_Dance = "Dance";

    //weight
    public final static String TABLE_NAME_Weight = "Weight_table";
    public final static String KEY_Weight_Index = "Weight_Index";
    public final static String KEY_Weight_IndexID = "Weight_IndexID";
    public final static String KEY_Weight_Date = "Weight_Date";
    public final static String KEY_Weight_Time = "Weight_Time";
    public final static String KEY_Weight_Time48 = "Weight_Time48";
    public final static String KEY_Weight_Weight = "Weight_Weight";
    public final static String KEY_Cloud_Weight = "KC_Weight";

    //height
    public final static String TABLE_NAME_Height = "Height_table";
    public final static String KEY_Height_Index = "Height_Index";
    public final static String KEY_Height_IndexID = "Height_IndexID";
    public final static String KEY_Height_Date = "Height_Date";
    public final static String KEY_Height_Time = "Height_Time";
    public final static String KEY_Height_Time48 = "Height_Time48";
    public final static String KEY_Height_Height = "Height_Height";
    public final static String KEY_Cloud_Height = "KC_Height";

    //target
    public final static String TABLE_NAME_Target = "Target_table";
    public final static String KEY_Target_Index = "Target_Index";//**
    public final static String KEY_Target_initDate = "Target_initDate";
    public final static String KEY_Target_initTime = "Target_initTime";
    public final static String KEY_Target_endDate = "Target_endDate";
    public final static String KEY_Target_endTime = "Target_endTime";
    public final static String KEY_Target_conDay = "Target_conDay";
    public final static String KEY_Target_conHour = "Target_conHour";
    public final static String KEY_Target_Target = "Target_Target";
    public final static String KEY_Target_TargetValue = "Target_Value";
    public final static String KEY_Cloud_Target = "KC_Target";

    public final static String KEY_Target_Height = "Target_Height";
    public final static String KEY_Target_Weight = "Target_Weight";
    public final static String KEY_Target_BMI = "Target_BMI";

    //prediction
    public final static String TABLE_NAME_Pred = "Pred_table";
    public final static String KEY_Pred_Index = "Pred_Index";
    public final static String KEY_Pred_IndexID = "Pred_IndexID";
    public final static String KEY_Pred_Date = "Pred_Date";
    public final static String KEY_Pred_Time = "Pred_Time";
    public final static String KEY_Pred_Time48 = "Pred_Time48";
    public final static String KEY_Pred_Weight = "Pred_Weight";
    public final static String KEY_Cloud_Pred = "KC_Pred";


    //Create Table
    //0=>False , 1=>True
    private String CREATE_TABLE_Hi =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_Hi+"("+
                    KEY_Hi_Index+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_Flag_UseFirst+" BOOLEAN,"+
                    KEY_Flag_SAID+" BOOLEAN,"+
                    KEY_Flag_FAID+" BOOLEAN,"+
                    KEY_AC_uid+" TEXT,"+
                    KEY_AC_initDate+" TEXT,"+
                    KEY_AC_FCM_id+" TEXT,"+
                    KEY_AC_LoginType+" TEXT,"+
                    KEY_AC_Locale+" TEXT,"+
                    KEY_AC_Name+" TEXT,"+
                    KEY_AC_Birthday+" TEXT,"+
                    KEY_AC_Gender+" TEXT,"+
                    KEY_AC_Height+" TEXT,"+
                    KEY_AC_Weight+" TEXT,"+
                    KEY_AC_WakeTime+" TEXT,"+
                    KEY_AC_WakeTime48+" TEXT,"+
                    KEY_AC_Image+" TEXT,"+
                    KEY_AC_Image_url+" TEXT,"+
                    KEY_AC_Account+" TEXT,"+
                    KEY_AC_GoogleMail+" TEXT,"+
                    KEY_AC_FacebookMail+" TEXT,"+
                    KEY_BI_Height_before+" TEXT,"+
                    KEY_BI_Weight_before+" TEXT,"+
                    KEY_BI_Step_before+" TEXT,"+
                    KEY_BI_CalIn_before+" TEXT,"+
                    KEY_BI_CalOut_before+" TEXT"+
                    ")";
    private String CREATE_TABLE_note =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_note+"("+
                    FEILD_note_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    FEILD_note_TEXT+" TEXT"+
                    ")";
    private String CREATE_TABLE_SAID =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_SAID+"("+
                    KEY_SAID_Index+" INTEGER PRIMARY KEY,"+
                    KEY_SAID_Store_ID+" TEXT,"+
                    KEY_SAID_Store_Name+" TEXT,"+
                    KEY_SAID_Store_Code1+" TEXT,"+
                    KEY_SAID_Store_Code2+" TEXT"+
                    ")";
    private String CREATE_TABLE_FAID =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_FAID+"("+
                    KEY_FAID_Index+" INTEGER PRIMARY KEY,"+
                    KEY_FAID_IndexID+" TEXT,"+
                    KEY_FAID_Basic_ID+" TEXT,"+
                    KEY_FAID_Basic_Name+" TEXT,"+
                    KEY_FAID_Basic_Weight_g+" TEXT,"+
                    KEY_FAID_Basic_Price_TWD+" TEXT,"+
                    KEY_FAID_Label_CalNormal_kcal+" TEXT,"+
                    KEY_FAID_Label_CalNoSa_kcal+" TEXT"+
                    ")";
    private String CREATE_TABLE_CalIn =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_CalIn+"("+
                    KEY_CalIn_Index+" INTEGER PRIMARY KEY,"+
                    KEY_CalIn_Date+" TEXT,"+
                    KEY_CalIn_Time+" TEXT,"+
                    KEY_CalIn_Time48+" TEXT,"+
                    KEY_CalIn_IndexID+" INTEGER,"+
                    KEY_CalIn_RID+" TEXT,"+
                    KEY_CalIn_FoodID+" TEXT,"+
                    KEY_CalIn_Rname+" TEXT,"+
                    KEY_CalIn_Fname+" TEXT,"+
                    KEY_CalIn_CG+" TEXT,"+
                    KEY_CalIn_Amount+" TEXT,"+
                    KEY_CalIn_Size+" TEXT,"+
                    KEY_CalIn_Sugar+" TEXT,"+
                    KEY_CalIn_HotCold+" TEXT,"+
                    KEY_CalIn_oneCal+" TEXT"+
                    ")";
    private String CREATE_TABLE_CalOut =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_CalOut+"("+
                    KEY_CalOut_Index+" INTEGER PRIMARY KEY,"+
                    KEY_CalOut_Date+" TEXT,"+
                    KEY_CalOut_Time+" TEXT,"+
                    KEY_CalOut_Time48+" TEXT,"+
                    KEY_CalOut_IndexID+" INTEGER,"+
                    KEY_CalOut_Sport+" TEXT,"+
                    KEY_CalOut_Cal+" TEXT,"+
                    KEY_CalOut_HeartRate+" TEXT,"+
                    KEY_CalOut_Strength+" TEXT,"+
                    KEY_CalOut_Continue+" TEXT,"+
                    KEY_CalOut_Distance+" TEXT"+
                    ")";
    private String CREATE_TABLE_Weight =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_Weight+"("+
                    KEY_Weight_Index+" INTEGER PRIMARY KEY,"+
                    KEY_Weight_Date+" TEXT,"+
                    KEY_Weight_Time+" TEXT,"+
                    KEY_Weight_Time48+" TEXT,"+
                    KEY_Weight_IndexID+" INTEGER,"+
                    KEY_Weight_Weight+" TEXT"+
                    ")";
    private String CREATE_TABLE_Height =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_Height+"("+
                    KEY_Height_Index+" INTEGER PRIMARY KEY,"+
                    KEY_Height_Date+" TEXT,"+
                    KEY_Height_Time+" TEXT,"+
                    KEY_Height_Time48+" TEXT,"+
                    KEY_Height_IndexID+" INTEGER,"+
                    KEY_Height_Height+" TEXT"+
                    ")";
    private String CREATE_TABLE_Target =
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_Target+"("+
                    KEY_Target_Index+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_Target_initDate+" TEXT,"+
                    KEY_Target_initTime+" TEXT,"+
                    KEY_Target_endDate+" TEXT,"+
                    KEY_Target_endTime+" TEXT,"+
                    KEY_Target_conDay+" TEXT,"+
                    KEY_Target_conHour+" TEXT,"+
                    KEY_Target_Target+" TEXT,"+
                    KEY_Target_TargetValue+" TEXT"+
                    ")";

    public HiDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Hi);
        db.execSQL(CREATE_TABLE_note);
        db.execSQL(CREATE_TABLE_SAID);
        db.execSQL(CREATE_TABLE_FAID);
        db.execSQL(CREATE_TABLE_CalIn);
        db.execSQL(CREATE_TABLE_CalOut);
        db.execSQL(CREATE_TABLE_Weight);
        db.execSQL(CREATE_TABLE_Height);
        db.execSQL(CREATE_TABLE_Target);

        //SAID_jsonUpdate();
        //database = this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Hi);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_note);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SAID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CalIn);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CalOut);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Weight);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Height);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Target);
        onCreate(db);
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    public void SAID_jsonUpdate(){
        //JSONObject jsonObjAll = new JSONObject(R.raw.said);
        //JSONArray jsonArrayAll = new JSONArray(R.raw.said);
        InputStream inputStream = context.getResources().openRawResource(R.raw.said);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        try {
            JSONArray ja_All = new JSONArray(jsonString);
            int len = ja_All.length();
            Log.d("**JsonArray", String.valueOf(len));

            for (int ii=0;ii<len;ii++){
                JSONObject jo_Item = ja_All.getJSONObject(ii);
                //Log.d("**JsonArray", String.valueOf(jo_Item));
                SAIDInsert(jo_Item.getInt(KEY_SAID_Index),
                        jo_Item.getString(KEY_SAID_Store_ID),
                        jo_Item.getString(KEY_SAID_Store_Name),
                        jo_Item.getString(KEY_SAID_Store_Code1),
                        jo_Item.getString(KEY_SAID_Store_Code2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void FAID_jsonUpdate(){
        //JSONObject jsonObjAll = new JSONObject(R.raw.said);
        //JSONArray jsonArrayAll = new JSONArray(R.raw.said);
        InputStream inputStream = context.getResources().openRawResource(R.raw.faid);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        try {
            JSONArray ja_All = new JSONArray(jsonString);
            int len = ja_All.length();
            Log.d("**JsonArray", String.valueOf(len));

            for (int ii=0;ii<len;ii++){
                JSONObject jo_Item = ja_All.getJSONObject(ii);
                //Log.d("**JsonArray", String.valueOf(jo_Item));
                String tmpBasicID = jo_Item.getString(KEY_FAID_Basic_ID);
                String tmpIndexID = tmpBasicID.substring(0,4);
                FAIDInsert(jo_Item.getInt(KEY_FAID_Index),
                        tmpIndexID,
                        tmpBasicID,
                        jo_Item.getString(KEY_FAID_Basic_Name),
                        jo_Item.getString(KEY_FAID_Basic_Weight_g),
                        jo_Item.getString(KEY_FAID_Basic_Price_TWD),
                        jo_Item.getString(KEY_FAID_Label_CalNormal_kcal),
                        jo_Item.getString(KEY_FAID_Label_CalNoSa_kcal));
                Log.d("HiDB","**HiDB**"+ii);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("HiDB","**HiDB**");
        }

    }

    public Cursor HiSelect(){
        Cursor cursor = database.query(TABLE_NAME_Hi, null, null, null, null, null, null);
        return cursor;
    }

    public void HiUpdate(int id, ContentValues values){
        database.update(TABLE_NAME_Hi, values, KEY_Hi_Index + "=" + Integer.toString(id), null);
    }

    public void HiInsert(ContentValues values){

        database.insert(TABLE_NAME_Hi, null, values);
    }

    public Cursor NoteSelect(){
        Cursor cursor = database.query(TABLE_NAME_note, null, null, null, null, null, null);
        return cursor;
    }

    public void NoteInsert(String itemText){
        ContentValues values = new ContentValues();
        values.put(FEILD_note_TEXT, itemText);
        database.insert(TABLE_NAME_note, null, values);
    }

    public void NoteDelete(int id){
        database.delete(TABLE_NAME_note, FEILD_note_ID + "=" + Integer.toString(id), null);
    }

    public void NoteUpdate(int id, String itemText){
        ContentValues values = new ContentValues();
        values.put(FEILD_note_TEXT, itemText);
        database.update(TABLE_NAME_note, values, FEILD_note_ID + "=" + Integer.toString(id), null);
    }

    public Cursor WeightSelect(){
        Cursor cursor = database.query(TABLE_NAME_Weight, null, null, null, null, null, null);
        return cursor;
    }

    public void WeightUpdate(int id, ContentValues values){
        database.update(TABLE_NAME_Weight, values, KEY_Weight_Index + "=" + Integer.toString(id), null);
    }

    public void WeightInsert(ContentValues values){
        database.insert(TABLE_NAME_Weight, null, values);
    }

    public Cursor HeightSelect(){
        Cursor cursor = database.query(TABLE_NAME_Height, null, null, null, null, null, null);
        return cursor;
    }

    public void HeightUpdate(int id, ContentValues values){
        database.update(TABLE_NAME_Height, values, KEY_Height_Index + "=" + Integer.toString(id), null);
    }

    public void HeightInsert(ContentValues values){
        database.insert(TABLE_NAME_Height, null, values);
    }

    public Cursor TargetSelect(String target){
        Cursor cursor = database.query(TABLE_NAME_Target, null, KEY_Target_Target+"='"+target+"'", null, null, null, null);
        return cursor;
    }

    public void TargetUpdate(int id, ContentValues values){
        database.update(TABLE_NAME_Target, values, KEY_Target_Index + "=" + Integer.toString(id), null);
    }

    public void TargetInsert(ContentValues values){
        database.insert(TABLE_NAME_Target, null, values);
    }

    public Cursor CalInSelect(){
        Cursor cursor = database.query(TABLE_NAME_CalIn, null, null, null, null, null, null);
        return cursor;
    }

    @SuppressLint("SimpleDateFormat")
    public Cursor CalInSelect(String day){
        String day1,day2,day3,dayWhere;// select * from table where date between day1 and day2;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cc = Calendar.getInstance();
        try {
            cc.setTime(df.parse(day));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        day2 = day;
        cc.add(Calendar.DAY_OF_YEAR,-1);
        day1 = df.format(cc.getTime());
        cc.add(Calendar.DAY_OF_YEAR,2);
        day3 = df.format(cc.getTime());

        dayWhere = String.format("%s='%s' OR %s='%s' OR %s='%s'"
                ,KEY_CalIn_Date,day1,KEY_CalIn_Date,day2,KEY_CalIn_Date,day3);

        Cursor cursor = database.query(TABLE_NAME_CalIn, null, dayWhere, null, null, null, null);
        return cursor;
    }

    public void CalInUpdate(int id, ContentValues values){
        database.update(TABLE_NAME_CalIn, values, KEY_CalIn_Index + "=" + Integer.toString(id), null);
    }

    public void CalInInsert(ContentValues values){

        database.insert(TABLE_NAME_CalIn, null, values);
    }
///////////////////////////////
    public Cursor CalOutSelect(){
        Cursor cursor = database.query(TABLE_NAME_CalOut, null, null, null, null, null, null);
        return cursor;
    }

    @SuppressLint("SimpleDateFormat")
    public Cursor CalOutSelect(String day){
        String day1,day2,day3,dayWhere;// select * from table where date between day1 and day2;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cc = Calendar.getInstance();
        try {
            cc.setTime(df.parse(day));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        day2 = day;
        cc.add(Calendar.DAY_OF_YEAR,-1);
        day1 = df.format(cc.getTime());
        cc.add(Calendar.DAY_OF_YEAR,2);
        day3 = df.format(cc.getTime());

        dayWhere = String.format("%s='%s' OR %s='%s' OR %s='%s'"
                ,KEY_CalOut_Date,day1,KEY_CalOut_Date,day2,KEY_CalOut_Date,day3);

        Cursor cursor = database.query(TABLE_NAME_CalOut, null, dayWhere, null, null, null, null);
        return cursor;
    }

    public void CalOutUpdate(int id, ContentValues values){
        database.update(TABLE_NAME_CalOut, values, KEY_CalOut_Index + "=" + Integer.toString(id), null);
    }

    public void CalOutInsert(ContentValues values){

        database.insert(TABLE_NAME_CalOut, null, values);
    }

    public Cursor SAIDSelect(){
        //Cursor cursor = database.query(TABLE_NAME_SAID, null, null, null, null, null, null);
        return database.query(TABLE_NAME_SAID, null, null, null, null, null, null);
    }

    private void SAIDInsert(int Store_index,String Store_ID
            , String Store_Name,String Store_Code1,String Store_Code2){
        ContentValues values = new ContentValues();
        values.put(KEY_SAID_Index, Store_index);
        values.put(KEY_SAID_Store_ID, Store_ID);
        values.put(KEY_SAID_Store_Name, Store_Name);
        values.put(KEY_SAID_Store_Code1, Store_Code1);
        values.put(KEY_SAID_Store_Code2, Store_Code2);

        database.insert(TABLE_NAME_SAID, null, values);
    }

    private void SAIDDelete(String Store_ID){
        database.delete(TABLE_NAME_SAID
                , KEY_SAID_Store_ID + "=" + Store_ID, null);
    }

    private void SAIDUpdate(int Store_index,String Store_ID
            , String Store_Name,String Store_Code1,String Store_Code2){
        ContentValues values = new ContentValues();
        values.put(KEY_SAID_Index, Store_index);
        values.put(KEY_SAID_Store_Name, Store_Name);
        values.put(KEY_SAID_Store_Code1, Store_Code1);
        values.put(KEY_SAID_Store_Code2, Store_Code2);
        database.update(TABLE_NAME_SAID, values
                , KEY_SAID_Store_ID + "=" + Store_ID, null);
    }

    public Cursor FAIDSelect(){
        Cursor cursor = database.query(TABLE_NAME_FAID, null, null, null, null, null, null);
        return cursor;
    }

    private void FAIDInsert(int Food_index,String Food_indexID,String Food_ID
            , String Food_Name,String Food_Weight,String Food_Price,
                            String Food_CalNormal,String Food_CalNoSugar){
        ContentValues values = new ContentValues();
        values.put(KEY_FAID_Index, Food_index);
        values.put(KEY_FAID_IndexID, Food_indexID);
        values.put(KEY_FAID_Basic_ID, Food_ID);
        values.put(KEY_FAID_Basic_Name, Food_Name);
        values.put(KEY_FAID_Basic_Weight_g, Food_Weight);
        values.put(KEY_FAID_Basic_Price_TWD, Food_Price);
        values.put(KEY_FAID_Label_CalNormal_kcal, Food_CalNormal);
        values.put(KEY_FAID_Label_CalNoSa_kcal, Food_CalNoSugar);

        database.insert(TABLE_NAME_FAID, null, values);
    }

    private void FAIDDelete(String Food_ID){
        database.delete(TABLE_NAME_FAID
                , KEY_FAID_Basic_ID + "=" + Food_ID, null);
    }

    private void FAIDUpdate(int Food_index,String Food_ID
            , String Food_Name,String Food_Weight,String Food_Price,
                            String Food_CalNormal,String Food_CalNoSugar){
        ContentValues values = new ContentValues();
        values.put(KEY_FAID_Index, Food_index);
        values.put(KEY_FAID_Basic_Name, Food_Name);
        values.put(KEY_FAID_Basic_Weight_g, Food_Weight);
        values.put(KEY_FAID_Basic_Price_TWD, Food_Price);
        values.put(KEY_FAID_Label_CalNormal_kcal, Food_CalNormal);
        values.put(KEY_FAID_Label_CalNoSa_kcal, Food_CalNoSugar);
        database.update(TABLE_NAME_FAID, values
                , KEY_FAID_Basic_ID + "=" + Food_ID, null);
    }

    public String SAID_NameFindId(String StoreName){
        String tmpStr = "";
        Cursor cursorR = database.query(TABLE_NAME_SAID
                ,new String[]{KEY_SAID_Store_ID}
                ,KEY_SAID_Store_Name+"='"+StoreName+"'"
                ,null,null,null,null);
        Log.d("**SQL**","**SQL**"+cursorR.getCount());

        if (cursorR.getCount()>0){
            cursorR.moveToPosition(0);
            tmpStr = cursorR.getString(cursorR.getColumnIndex(KEY_SAID_Store_ID));
        }

        cursorR.close();

        return tmpStr;
    }

    public String FoodNameFindId(String StoreName,String FoodName){
        String tmpStr = "";
        String tmpR = SAID_NameFindId(StoreName);

        Cursor cursorF = database.query(TABLE_NAME_FAID
                ,new String[]{KEY_FAID_Basic_ID}
                ,KEY_FAID_IndexID+"='"+tmpR+"'"
                        +" AND "+KEY_FAID_Basic_Name+"='"+FoodName+"'"
                ,null,null,null,null);
        Log.d("**SQL**","**SQL22**"+cursorF.getCount());

        if (cursorF.getCount()>0){
            cursorF.moveToPosition(0);
            tmpStr = cursorF.getString(cursorF.getColumnIndex(KEY_FAID_Basic_ID));
        }

        cursorF.close();

        return tmpStr;
    }

    public String[] FoodIdFindCal(String mFoodId){
        String[] tmpStr = new String[2];

        Cursor cursorF = database.query(TABLE_NAME_FAID
                ,new String[]{KEY_FAID_Label_CalNormal_kcal,KEY_FAID_Label_CalNoSa_kcal}
                ,KEY_FAID_Basic_ID+"='"+mFoodId+"'"
                ,null,null,null,null);
        Log.d("**SQL**","**FoodIdFindCal**"+cursorF.getCount());

        if (cursorF.getCount()>0){
            cursorF.moveToPosition(0);
            tmpStr[0] = cursorF.getString(cursorF.getColumnIndex(KEY_FAID_Label_CalNormal_kcal));
            tmpStr[1] = cursorF.getString(cursorF.getColumnIndex(KEY_FAID_Label_CalNoSa_kcal));
        }

        cursorF.close();

        return tmpStr;
    }

    public void close(){
        database.close();
    }
}
