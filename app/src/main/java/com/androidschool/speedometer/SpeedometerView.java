package com.androidschool.speedometer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SpeedometerView extends View {

    private final int BODY_CIRCLE_STROKE_WIDTH = getResources().getDimensionPixelSize(R.dimen.body_circle_stroke_width);
    private final int SCALE_ARC_STROKE_WIDTH = getResources().getDimensionPixelSize(R.dimen.scale_arc_stroke_width);
    private static final float START_ANGLE = -90f;
    private static final int MAX_ANGLE = 360;
    private static final float START_SCALE_ARC_ANGLE = -65f;
    private static final int MAX_SCALE_ARC_ANGLE = 330;
    private static final String TAG = "SpeedometerView";

    private int mSpeed;
    private int mMaxSpeed;
    private int mLowSpeedColor;
    private int mNormalSpeedColor;
    private int mHighSpeedColor;
    private int mArrowColor;

    private RectF mSpeedometerBodyRect = new RectF();
    private Paint mSpeedometerBodyCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mScaleRect = new RectF();
    private Paint mScaleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SpeedometerView(Context context) {
        super(context);
        init(context, null);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int mSpeed) {
        this.mSpeed = mSpeed;
    }

    public int getMaxSpeed() {
        return mMaxSpeed;
    }

    public void setMaxSpeed(int mMaxSpeed) {
        this.mMaxSpeed = mMaxSpeed;
    }

    public int getLowSpeedColor() {
        return mLowSpeedColor;
    }

    public void setLowSpeedColor(int mLowSpeedColor) {
        this.mLowSpeedColor = mLowSpeedColor;
    }

    public int getNormalSpeedColor() {
        return mNormalSpeedColor;
    }

    public void setNormalSpeedColor(int mNormalSpeedColor) {
        this.mNormalSpeedColor = mNormalSpeedColor;
    }

    public int getHighSpeedColor() {
        return mHighSpeedColor;
    }

    public void setHighSpeedColor(int mHighSpeedColor) {
        this.mHighSpeedColor = mHighSpeedColor;
    }

    public int getArrowColor() {
        return mArrowColor;
    }

    public void setArrowColor(int mArrowColor) {
        this.mArrowColor = mArrowColor;
    }



    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        Log.d(TAG, "init() called with: context = [" + context.toString() + "], attrs = [" + attrs + "]");
        extractAttributes(context, attrs);
        configurePaint(mSpeedometerBodyCirclePaint, BODY_CIRCLE_STROKE_WIDTH, R.color.colorSpeedometerBody);
        configurePaint(mScaleArcPaint, SCALE_ARC_STROKE_WIDTH, mNormalSpeedColor); //todo: add gradient with 3 colors from attrs

    }

    private void extractAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        final Resources.Theme theme = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.SpeedometerView,
                R.attr.speedometerStyle, R.style.SpeedometerStyle);
        try {
            mSpeed = typedArray.getInteger(R.styleable.SpeedometerView_speed, 0);
            mMaxSpeed = typedArray.getColor(R.styleable.SpeedometerView_maxSpeed, 0);
            mLowSpeedColor = typedArray.getColor(R.styleable.SpeedometerView_lowSpeedColor, 0);
            mNormalSpeedColor = typedArray.getColor(R.styleable.SpeedometerView_normalSpeedColor, 0);
            mHighSpeedColor = typedArray.getColor(R.styleable.SpeedometerView_highSpeedColor, 0);
            mArrowColor = typedArray.getColor(R.styleable.SpeedometerView_arrowColor, 0);
            Log.d(TAG, "Speed = " + mSpeed + ", " + "maxSpeed = " + mMaxSpeed + ", mLowSpeedColor = " + mLowSpeedColor + ", mNormalSpeedColor = " + mNormalSpeedColor + ", " + "arrowColor = " + mArrowColor);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: widthMeasureSpec = [" + MeasureSpec.toString(widthMeasureSpec) + "]," +
                " heightMeasureSpec = [" + MeasureSpec.toString(heightMeasureSpec) + "]");
        final int suggestedMinimumSize = Math.max(getSuggestedMinimumHeight(), getSuggestedMinimumWidth());
        final int resolvedWidth = resolveSize(suggestedMinimumSize + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
        final int resolvedHeight = resolveSize(suggestedMinimumSize + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
        final int resolvedSize = Math.min(resolvedHeight, resolvedWidth);
        setMeasuredDimension(resolvedSize, resolvedSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(BODY_CIRCLE_STROKE_WIDTH / 2, BODY_CIRCLE_STROKE_WIDTH / 2);
        // рисуем круг корпуса спидометра
        configureBodyRect();
        canvas.drawArc(mSpeedometerBodyRect, START_ANGLE, MAX_ANGLE, false, mSpeedometerBodyCirclePaint);
        canvas.drawRect(mSpeedometerBodyRect,mSpeedometerBodyCirclePaint);
        invalidate();
        configureScaleRect();
        canvas.drawArc(mScaleRect, START_SCALE_ARC_ANGLE, MAX_SCALE_ARC_ANGLE, false, mScaleArcPaint);
        canvas.drawRect(mScaleRect, mScaleArcPaint);
        invalidate();

    }

    private void configureBodyRect() {
        mSpeedometerBodyRect.left = getPaddingLeft();
        mSpeedometerBodyRect.top = getPaddingTop();
        mSpeedometerBodyRect.right = getWidth() - BODY_CIRCLE_STROKE_WIDTH - getPaddingRight();
        mSpeedometerBodyRect.bottom = getHeight() - BODY_CIRCLE_STROKE_WIDTH - getPaddingBottom();
    }

    private void configureScaleRect() {
        mScaleRect.left = getPaddingLeft() + BODY_CIRCLE_STROKE_WIDTH + SCALE_ARC_STROKE_WIDTH;
        mScaleRect.top = getPaddingTop() + BODY_CIRCLE_STROKE_WIDTH + SCALE_ARC_STROKE_WIDTH;
        mScaleRect.right = getWidth() - BODY_CIRCLE_STROKE_WIDTH - SCALE_ARC_STROKE_WIDTH - getPaddingRight();
        mScaleRect.bottom = getHeight() - BODY_CIRCLE_STROKE_WIDTH - SCALE_ARC_STROKE_WIDTH - getPaddingBottom();
    }



    private void configurePaint(Paint paint, int strokeWidth, int color) {
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
    }


}
