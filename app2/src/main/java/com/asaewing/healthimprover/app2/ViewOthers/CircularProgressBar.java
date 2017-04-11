package com.asaewing.healthimprover.app2.ViewOthers;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.asaewing.healthimprover.app2.R;

/**
 *
 */
public class CircularProgressBar extends View {

    // Properties
    private float progress = 0;
    private float maxProgress = 300;
    private float strokeWidth = getResources().getDimension(R.dimen.default_stroke_width);
    private float backgroundStrokeWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
    private float textStrokeWidth;

    private int barColor=Color.BLUE,backgroundColor=Color.BLACK,textColor=Color.GREEN;

    String sUnit;

    int width,height,min;

    // Object used to draw
    private int startAngle = -90;
    private RectF rectF;
    private Paint backgroundPaint,foregroundPaint,textPaint;

    ObjectAnimator objectAnimator;
    //AttributeSet attributeSet;

    //region Constructor & Init Method
    public CircularProgressBar(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
        rectF = new RectF();
    }

    private void init() {
        //rectF = new RectF();

        // Init Background
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);

        // Init Foreground
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(barColor);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStyle(Paint.Style.FILL);
    }
    //endregion

    //region Draw Method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //rectF = new RectF();

        width = this.getWidth();
        height = this.getHeight();
        if(width!=height) min=Math.min(width,height);
        else min = width;

        backgroundStrokeWidth = min/15;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        strokeWidth = backgroundStrokeWidth-2;
        foregroundPaint.setStrokeWidth(strokeWidth);
        textStrokeWidth = min/2;
        textPaint.setStrokeWidth(textStrokeWidth);

        canvas.drawOval(rectF, backgroundPaint);
        float angle = (progress/maxProgress)*360;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);

        String text =
                String.valueOf((int)progress) + sUnit;
        int textHeight = (int)(height/4.2);
        textPaint.setTextSize(textHeight);
        int textWidth = (int) textPaint.measureText(text, 0, text.length());
        canvas.drawText(text, min/2 - textWidth/2, min/2 +textHeight/2, textPaint);
    }
    //endregion

    //region Mesure Method
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }
    //endregion

    //region Method Get/Set
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        //this.progress = (progress<=maxProgress) ? progress : maxProgress;
        this.progress = progress;
        invalidate();
    }

    public float getProgressBarWidth() {
        return strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public int getColor() {
        return barColor;
    }

    public void setColor(int color) {
        this.barColor = color;
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }
    //endregion

    //region Other Method
    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     * so don't call the {@link CircularProgressBar#setProgress(float)} directly within this method.
     *
     * @param progress The progress it should animate to it.
     */
    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1000);
    }

    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     * so don't call the {@link CircularProgressBar#setProgress(float)} directly within this method.
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    public void setProgressWithAnimation(float progress, int duration) {
        reset();
        objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new OvershootInterpolator());
        objectAnimator.start();
    }

    public void reset() {
        objectAnimator = null;
        progress = 0;
    }

    public void setInitProgress(int BarColor,int BackgroundColor,int TextColor,
                                float maxProgress,String msUnit) {
        this.progress = 0;
        this.barColor = BarColor;
        this.backgroundColor = BackgroundColor;
        this.textColor = TextColor;
        this.maxProgress = maxProgress;
        this.sUnit = msUnit;

        init();
    }
}