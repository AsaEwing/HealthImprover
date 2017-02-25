package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;

import com.blunderer.materialdesignlibrary.views.Toolbar;

import java.util.List;

public abstract class ActionBarHandler {

    protected final Context mContext;

    public ActionBarHandler(Context context) {
        mContext = context;
    }

    public abstract Toolbar build();

    public abstract ActionBarSearchHandler setAutoCompletionItems(List<String> mItems);
}
