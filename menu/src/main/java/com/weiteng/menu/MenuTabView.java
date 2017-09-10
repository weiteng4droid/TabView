package com.weiteng.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Arrays;


/**
 * Tab 控制控制
 *
 * Created by weiTeng on 15/12/5.
 */
public class MenuTabView extends View {

    private static final String TAG = "MenuTabView";
    private static final boolean DEBUG = true;

    private Rect[] mCacheBounds;
    private Rect[] mTextBounds;

    private String[] mTexts;

    // Gap between image and text
    private int mVerticalGap;
    private int mTextSize;
    private int mSingleWidth;

    private int mLeftLineWidth;
    private int mLeftLineColor;
    private int mTextSelectColor, mTextNormalColor;
    private int mNoticeColor = 0xffff0000;
    private int mSelectBgColor, mNormalBgColor;

    private int mPointRadius = 10;
    private Paint mLinePaint;
    private Paint mSelectBackgroundPaint;
    private Paint mPaint;
    private Paint mNoticePaint;

    private int mTouchSlop;

    private boolean mShowNotice;        // is show notice point
    private boolean mTouchClear;
    private boolean inTapRegion;
    private boolean[] mPosNotices;

    private int mStartX;
    private int mStartY;
    private int mCurrentIndex;

    private OnTabClickListener mOnTabClickListener;

    public interface OnTabClickListener{
        void onTabClick(int index);
    }

    public MenuTabView(Context context) {
        this(context, null);
    }

    public MenuTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MenuTabView);
        String texts = ta.getString(R.styleable.MenuTabView_mt_texts);
        if (texts != null) {
            mTexts = texts.split("\\|");
        }
        mTextSize = ta.getDimensionPixelSize(R.styleable.MenuTabView_mt_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
        mVerticalGap = ta.getDimensionPixelSize(R.styleable.MenuTabView_mt_verticalGap,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        mLeftLineWidth = ta.getDimensionPixelSize(R.styleable.MenuTabView_mt_leftLineWith,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()));
        mLeftLineColor = ta.getColor(R.styleable.MenuTabView_mt_leftLineColor, getResources().getColor(android.R.color.holo_red_light));

        mTextSelectColor = ta.getColor(R.styleable.MenuTabView_mt_textSelectColor, 0xff0099cc);
        mTextNormalColor = ta.getColor(R.styleable.MenuTabView_mt_textNormalColor, 0x59f0f0f0);
        mSelectBgColor = ta.getColor(R.styleable.MenuTabView_mt_bgSelectColor, getResources().getColor(android.R.color.darker_gray));
        mNormalBgColor = ta.getColor(R.styleable.MenuTabView_mt_bgNormalColor, getResources().getColor(android.R.color.white));

        mShowNotice = ta.getBoolean(R.styleable.MenuTabView_mt_showNotice, false);

        ta.recycle();

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mLeftLineColor);
        mLinePaint.setStrokeWidth(mLeftLineWidth);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mTextSelectColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mSelectBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectBackgroundPaint.setStyle(Paint.Style.FILL);

        if(mShowNotice){
            mNoticePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mNoticePaint.setColor(mNoticeColor);
            mPosNotices = new boolean[mTexts.length];
        }

        // get the real touchSlop
        int touchSlop;
        if(context == null){
            touchSlop = ViewConfiguration.getTouchSlop();
        }else{
            final ViewConfiguration config = ViewConfiguration.get(context);
            touchSlop = config.getScaledTouchSlop();
        }

        mTouchSlop = touchSlop * touchSlop;
        inTapRegion = false;
    }

    public void setCurrentIndex(int index){
        setCurrentIndex(index, false);
        invalidate();
    }

    public String getCurrentItemText(){
        final int index = mCurrentIndex;
        if (index < mTexts.length) {
            return mTexts[index];
        }
        return null;
    }

    public void setCurrentIndex(int index, boolean tiger){
        mCurrentIndex = index;
        if(mOnTabClickListener != null && tiger){
            mOnTabClickListener.onTabClick(index);
        }
    }

    public void setNoticePointRadius(int pointRadius){
        if (mPointRadius != pointRadius) {
            mPointRadius = pointRadius;
            requestLayout();
        }
    }

    public boolean isShowNotice() {
        return mShowNotice;
    }

    public void showNoticePoint(boolean showNotice) {
        if(mShowNotice != showNotice) {
            mShowNotice = showNotice;
            invalidate();
        }
    }

    public void showNoticePointAtPosition(int position, boolean toggle){
        if(!mShowNotice){
            return;
        }
        if(position >= mTexts.length){
            throw new IndexOutOfBoundsException("超出标签的长度");
        }
        mPosNotices[position] = toggle;
        invalidate();
    }

    public void clearNoticePointOnTouch(boolean tiger){
        mTouchClear = tiger;
    }

    public void setTextSize(int textSize){
        setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    private void setTextSize(int unit, int textSize){
        mPaint.setTextSize(TypedValue.applyDimension(unit, textSize, getResources().getDisplayMetrics()));

        if(mTextSize != textSize){
            mTextSize = textSize;
            requestLayout();
            invalidate();
        }
    }

    public void setLeftLineColor(int leftLineColor){
        if(mLeftLineColor != leftLineColor){
            mLeftLineColor = leftLineColor;
            invalidate();
        }
    }

    public void setTextSelectColor(int textSelectColor){
        if(mTextSelectColor != textSelectColor){
            mTextSelectColor = textSelectColor;
            invalidate();
        }
    }

    public void setTextNormalColor(int textNormalColor) {
        if(mTextNormalColor != textNormalColor){
            mTextNormalColor = textNormalColor;
            invalidate();
        }
    }

    public void setLeftLineWidth(int leftLineWidth){
        mLinePaint.setStrokeWidth(mLeftLineWidth);
        if(mLeftLineWidth != leftLineWidth){
            mLeftLineWidth = leftLineWidth;
            requestLayout();
            invalidate();
        }
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mCacheBounds == null || mCacheBounds.length != mTexts.length) {
            mCacheBounds = new Rect[mTexts.length];
        }

        if (mTextBounds == null || mTextBounds.length != mTexts.length) {
            mTextBounds = new Rect[mTexts.length];
        }

        int width;
        int height;
        int realWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int realHeight = heightSize - getPaddingTop() - getPaddingBottom();

        // 临时最小的尺寸
        final String tempContent = "测试";
        int tempSingleHeight = (int) (mVerticalGap * 2 + mPaint.measureText(tempContent, 0, tempContent.length()));
        int tempSingleWidth = (int) (mVerticalGap * 2 + mPaint.measureText(tempContent, 0, tempContent.length()));

        // 测量宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            width = realWidth;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            if (tempSingleWidth < realWidth) {
                width = realWidth;
            } else {
                width = tempSingleWidth;
            }
        } else {
            width = Math.min(tempSingleWidth, realWidth);
        }

        // 测量高度
        if (heightMode == MeasureSpec.EXACTLY) {
            height = realHeight;
            tempSingleHeight = realHeight / mTexts.length;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            if (realHeight <= tempSingleHeight * mTexts.length) {
                height = tempSingleHeight * mTexts.length;
            } else {
                height = realHeight;
                tempSingleHeight = realHeight / mTexts.length;
            }
        } else {
            height = Math.min(tempSingleHeight * mTexts.length, realHeight);
        }

        mSingleWidth = width;

        if (DEBUG) {
            Log.d(TAG, "height = " + height);
            Log.d(TAG, "width = " + width);
        }
        calculateBounds(tempSingleHeight);
        setMeasuredDimension(width + getPaddingRight() + getPaddingLeft(), height + getPaddingTop() + getPaddingBottom());
    }

    /**
     * 计算图标的位置和单个item的位置
     */
    private void calculateBounds(int tempSingleHeight) {
        for (int i = 0; i < mCacheBounds.length; i++) {
            if (mCacheBounds[i] == null) {
                mCacheBounds[i] = new Rect();
            }

            Rect rect = mCacheBounds[i];
            rect.left = getPaddingLeft();
            rect.top = i * tempSingleHeight + getPaddingTop();
            rect.right = rect.left + mSingleWidth + getPaddingRight();
            rect.bottom = rect.top + tempSingleHeight;

            if (mTextBounds[i] == null) {
                mTextBounds[i] = new Rect();
            }
            mPaint.getTextBounds(mTexts[i], 0, mTexts[i].length(), mTextBounds[i]);
        }

        Log.d(TAG, "mCacheBounds = " + Arrays.toString(mCacheBounds));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                inTapRegion = true;

                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - mStartX);
                int dy = (int) (event.getY() - mStartY);

                int distance = dx * dx + dy * dy;
                if(distance > mTouchSlop){
                    inTapRegion = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(inTapRegion){
                    int index = mCurrentIndex;
                    for (int i = 0; i < mCacheBounds.length; i++) {
                        Rect touchRect = mCacheBounds[i];

                        // If you need more accurate when you need to add the Y direction of the judgment, this View only to determine the level of direction
                        if(mStartY > touchRect.top && mStartY < touchRect.bottom){
                            index = i;
                            break;
                        }
                    }
                    if (mCurrentIndex != index) {
                        mCurrentIndex = index;
                        if (mOnTabClickListener != null) {
                            mOnTabClickListener.onTabClick(index);
                        }
                        if (mTouchClear) {
                            mPosNotices[index] = false;
                        }
                        invalidate();
                    }
                }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mTexts.length; i++) {
            if (i == mCurrentIndex) {
                mPaint.setFakeBoldText(true);
                mPaint.setColor(mTextSelectColor);
                mSelectBackgroundPaint.setColor(mSelectBgColor);
            } else {
                mPaint.setFakeBoldText(false);
                mPaint.setColor(mTextNormalColor);
                mSelectBackgroundPaint.setColor(mNormalBgColor);
            }

            // 画Item背景颜色
            canvas.drawRect(mCacheBounds[i], mSelectBackgroundPaint);

            // 画选中的左边缘线
            if (i == mCurrentIndex) {
                canvas.drawRect(mCacheBounds[i].left,
                        mCacheBounds[i].top + mLeftLineWidth / 2, mCacheBounds[i].left + mLeftLineWidth, mCacheBounds[i].bottom, mLinePaint);
            }

            // 绘制文本
            canvas.drawText(mTexts[i], mCacheBounds[i].exactCenterX(), mCacheBounds[i].exactCenterY() + mTextBounds[i].height() / 2, mPaint);
        }
    }
}