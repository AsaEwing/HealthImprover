package com.asaewing.healthimprover.app2.fl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 2016/9/27.
 */
public class fl_Note extends RootFragment {
    private EditText inputText;
    private ListView listInput;
    private HiDBHelper helper;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;
    private List<String> option;

    public fl_Note() {
        // Required empty public constructor
    }

    public static fl_Note newInstance() {

        return new fl_Note();
    }

    //TODO----Data----
    @Override
    public void mSaveState() {

    }

    //TODO----生命週期----
    @Override
    public void onAttach(Context context) {
        TAG = getClass().getSimpleName();

        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.note_activity_main, container, false);
        initDB();
        initView();

        String tmpHi = "有什麼要紀錄的事情呢？";
        assert MainActivity2.HiCard_Text != null;
        MainActivity2.HiCard_Text.start(tmpHi);

        return rootView;
    }

    private void initDB() {
        helper = MainActivity2.helper;
        cursor = helper.NoteSelect();
        listInput = (ListView)rootView.findViewById(R.id.listInputText);
        cursorAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.note_adapter, cursor,
                new String[]{"item_text"},
                new int[]{R.id.text},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
    }

    private void initView(){
        option = new ArrayList<>();
        option.add(getActivity().getApplicationContext().getString(R.string.modify));
        option.add(getString(R.string.delete));
        inputText = (EditText)rootView.findViewById(R.id.inputText);
        listInput = (ListView)rootView.findViewById(R.id.listInputText);
        listInput.setAdapter(cursorAdapter);
        listInput.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                final int pos = position;
                cursor.moveToPosition(1);
                new AlertDialog.Builder(getActivity())
                        .setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0://modify
                                        final View item = LayoutInflater.from(getActivity()).inflate(R.layout.note_item_layout, null);
                                        final EditText editText = (EditText) item.findViewById(R.id.edittext);
                                        editText.setText(cursor.getString(1));
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("修改數值")
                                                .setView(item)
                                                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        helper.NoteUpdate(cursor.getInt(0), editText.getText().toString());
                                                        cursor.requery();
                                                        cursorAdapter.notifyDataSetChanged();
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 1://delete
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("刪除列")
                                                .setMessage("你確定要刪除？")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        helper.NoteDelete(cursor.getInt(0));
                                                        cursor.requery();
                                                        cursorAdapter.notifyDataSetChanged();
                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                        break;
                                }

                            }
                        }).show();
                return false;
            }

        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "新增");
        menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "離開程式");
        super.onCreateOptionsMenu(menu, inflater);
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "新增");
        menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "離開程式");
        return super.onCreateOptionsMenu(menu);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST://add new item
                if(!inputText.getText().toString().equals("")){
                    helper.NoteInsert(inputText.getText().toString());
                    cursor.requery();
                    cursorAdapter.notifyDataSetChanged();
                    inputText.setText("");
                }
                break;
            case Menu.FIRST + 1://exit app
                new AlertDialog.Builder(getActivity())
                        .setTitle("離開此程式")
                        .setMessage("你確定要離開？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
