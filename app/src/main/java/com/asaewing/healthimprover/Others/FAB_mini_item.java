package com.asaewing.healthimprover.Others;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asa on 2016/7/29.
 */
public class FAB_mini_item extends View {

    private TextView itemText;
    private FloatingActionButton itemFAB;
    private View view;

    public FAB_mini_item(Context context) {
        super(context);
    }

    public FAB_mini_item(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setInit(int BackgroundColorText, int ColorText,
                        int BackgroundColorFAB, int ImageColorFAB,
                        String textItem, int imageFAB) {

        itemFAB.setImageResource(imageFAB);
        itemFAB.setBackgroundTintList(ColorStateList.valueOf(BackgroundColorFAB));
        itemFAB.setImageTintList(ColorStateList.valueOf(ImageColorFAB));

        itemText.setBackgroundColor(BackgroundColorText);
        itemText.setTextColor(ColorText);
        itemText.setText(textItem);
    }
}
