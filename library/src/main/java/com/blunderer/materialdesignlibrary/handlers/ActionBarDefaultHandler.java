package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;

import com.blunderer.materialdesignlibrary.views.Toolbar;
import com.blunderer.materialdesignlibrary.views.ToolbarDefault;

import java.util.List;

public class ActionBarDefaultHandler extends ActionBarHandler {

    public ActionBarDefaultHandler(Context context) {
        super(context);
    }

    /**
     * Build the Toolbar to be the Default Toolbar.
     *
     * @return The Toolbar.
     */
    @Override
    public Toolbar build() {
        return new ToolbarDefault(mContext);
    }

    @Override
    public ActionBarSearchHandler setAutoCompletionItems(List<String> mItems) {
        return null;
    }

}
