package com.asaewing.healthimprover.Others;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ListPopupWindow;

public class MyAutoView extends AutoCompleteTextView {

    public MyAutoView(Context context) {
        super(context);
    }

    public MyAutoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAutoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyAutoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    //=======================================================
    private int mLastKeyCode = KeyEvent.KEYCODE_UNKNOWN;


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        //&& !mPopup.isDropDownAlwaysVisible()
        Log.d("**MyAutoView**","**MyAutoView**key999**");
        /*if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dismissDropDown();
                    return true;
                }
            }
        }*/
        return false;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*boolean consumed = mPopup.onKeyUp(keyCode, event);
        if (consumed) {
            switch (keyCode) {
                // if the list accepts the key events and the key event
                // was a click, the text view gets the selected item
                // from the drop down as its content
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_TAB:
                    if (event.hasNoModifiers()) {
                        performCompletion();
                    }
                    return true;
            }
        }*/
        /*if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
            performCompletion();
            return true;
        }*/
        return super.onKeyUp(keyCode, event);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (mPopup.onKeyDown(keyCode, event)) {
            return true;
        }*/

        /*if (!isPopupShowing()) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.hasNoModifiers()) {
                        performValidation();
                    }
            }
        }
        if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
            return true;
        }*/
        mLastKeyCode = keyCode;
        boolean handled = super.onKeyDown(keyCode, event);
        mLastKeyCode = KeyEvent.KEYCODE_UNKNOWN;
        if (handled && isPopupShowing()) {
            clearListSelection();
        }
        return handled;
    }
}

