package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;
import android.view.View;

import com.blunderer.materialdesignlibrary.listeners.OnSearchDynamicListener;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;
import com.blunderer.materialdesignlibrary.views.Toolbar;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;

import java.util.List;

public class ActionBarSearchHandler extends ActionBarHandler {

    private OnSearchListener mOnSearchListener;
    private OnSearchDynamicListener mOnSearchDynamicListener;
    private boolean mAutoCompletionEnabled;
    private boolean mAutoCompletionDynamic;
    private List<String> mAutoCompletionSuggestions;
    private int mThreshold;
    private ToolbarSearch.AutoCompletionMode mAutoCompletionMode;
    private int mCustomSearchButtonId;
    private View mCustomSearchButton;
//    private int mInputTextStyle;
//    private int mInputBackgroundResource;

    public ActionBarSearchHandler(Context context, OnSearchListener onSearchListener) {
        super(context);

        mOnSearchListener = onSearchListener;
        mAutoCompletionEnabled = false;
        mAutoCompletionDynamic = false;
        mThreshold = 1;
        mAutoCompletionMode = ToolbarSearch.AutoCompletionMode.STARTS_WITH;
        mCustomSearchButtonId = 0;
//        mInputTextStyle = 0;
//        mInputBackgroundResource = 0;
    }

    /**
     * Build the Toolbar to be the Search Toolbar.
     *
     * @return The Toolbar.
     */
    @Override
    public Toolbar build() {
        ToolbarSearch toolbar = new ToolbarSearch(mContext);
        toolbar.setData(mAutoCompletionEnabled, mAutoCompletionDynamic, mAutoCompletionSuggestions,
                mAutoCompletionMode, mThreshold, mOnSearchListener, mOnSearchDynamicListener);
//                mInputTextStyle, mInputBackgroundResource);

        return toolbar;
    }

    @Override
    public ActionBarSearchHandler setAutoCompletionItems(List<String> mItems) {
        return null;
    }

    /**
     * Enables Auto Completion.
     *
     * @return The ActionBarSearchHandler.
     */
    public ActionBarSearchHandler enableAutoCompletion() {
        mAutoCompletionEnabled = true;

        return this;
    }

    /**
     * Enables Dynamic Auto Completion.
     * e.g. can be useful to retrieve strings from a server.
     *
     * @param onSearchDynamicListener The listener to call when the treatment is over.
     * @return The ActionBarSearchHandler.
     */
    public ActionBarSearchHandler enableAutoCompletionDynamic(
            OnSearchDynamicListener onSearchDynamicListener) {
        mAutoCompletionDynamic = true;
        mOnSearchDynamicListener = onSearchDynamicListener;

        return this;
    }

    /**
     * Specifies the minimum number of characters the user has to type in the edit box before the list is shown.
     * When threshold is less than or equals 0, a threshold of 1 is applied.
     * DEFAULT = 1
     *
     * @param threshold The number of characters to type before the list is shown.
     * @return The ActionBarSearchHandler.
     */
    public ActionBarSearchHandler setAutoCompletionThreshold(int threshold) {
        mThreshold = threshold;

        return this;
    }

    /**
     * If Auto Completion is enabled but Dynamic Auto Completion is not, this method sets the suggestions to search into.
     *
     * @param suggestions The suggestions to search into.
     * @return The ActionBarSearchHandler.
     */
    public ActionBarSearchHandler setAutoCompletionSuggestions(List<String> suggestions) {
        mAutoCompletionSuggestions = suggestions;

        return this;
    }

    /**
     * If Auto Completion and Auto Completion Dynamic are enabled, this method sets the Auto Completion Mode.
     * - STARTS_WITH that searches sentences that start with the value.
     * - CONTAINS that searches sentences that contain the value.
     * The value is that the user is currently searching.
     * DEFAULT = STARTS_WITH
     *
     * @param autoCompletionMode The mode for searching into sentences.
     * @return The ActionBarSearchHandler.
     */
    public ActionBarSearchHandler setAutoCompletionMode(ToolbarSearch.AutoCompletionMode autoCompletionMode) {
        mAutoCompletionMode = autoCompletionMode;

        return this;
    }

    public ActionBarSearchHandler setCustomSearchButton(int customSearchButtonId) {
        mCustomSearchButtonId = customSearchButtonId;

        return this;
    }

    public ActionBarSearchHandler setCustomSearchButton(View customSearchButton) {
        mCustomSearchButton = customSearchButton;

        return this;
    }

//    public ActionBarSearchHandler setInputStyle(int inputTextStyle,
//                                                int inputBackgroundResource) {
//        mInputTextStyle = inputTextStyle;
//        mInputBackgroundResource = inputBackgroundResource;
//        return this;
//    }

    public int getCustomSearchButtonId() {
        return mCustomSearchButtonId;
    }

    public View getCustomSearchButton() {
        return mCustomSearchButton;
    }

}
