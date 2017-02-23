package com.cjyun.tb.patient.custom;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.cjyun.tb.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/5/25 0025</br>
 * description:</br>
 */
public class CalendarView extends View implements View.OnTouchListener {
    private final String[] weekText;//[一，二...]
    /**
     * 日期
     */
    private Calendar calendar;
    private int selectedYear;
    private int selectedMonth;
    private int curStartIndex;//the index in date[] of the first day of current month
    private int curEndIndex;//the index in date[] of the last day of current month
    private float itemWidth;//单元格 高宽
    private float itemHeight;//单元格 高宽
    /**
     * 所有的日期数据 7排6行
     */
    private int[] date = new int[7 * 6];
    /**
     * 笔
     */
    private Paint textPaint;
    private Paint bgPaint;//画背景颜色的笔
    private Paint circlePaint;//当前选择位置圆环
    private Paint roundPaint;//当前天的圆
    private Paint pointPaint;//小红点
    /**
     * 位置信息
     */
    private int actionDownIndex = -1;
    /**
     * 单元点击事件
     */
    private OnItemByClick mItemByClick;
    private ArrayMap<Integer, Boolean> mErrorMap;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        itemWidth = screenWidth / 7.0f;
        itemHeight = screenWidth * 0.8f;

        weekText = getResources().getStringArray(R.array.cv_week_head);

        float fontScale = getResources().getDisplayMetrics().scaledDensity;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(14 * fontScale + 0.5f);
        textPaint.setColor(getResources().getColor(R.color.activity_textColor));

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4f);
        circlePaint.setColor(getResources().getColor(R.color.activity_controls_normal_green));

        roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        roundPaint.setStrokeWidth(4f);
        roundPaint.setColor(getResources().getColor(R.color.activity_controls_normal_green));

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setColor(getResources().getColor(R.color.viewfinder_laser));

        calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        initial(calendar);

        setOnTouchListener(this);
    }

    /**
     * calculate the values of date[] and the legal range of index of date[]
     */
    private void initial(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int monthStart = -1;
        if (dayOfWeek >= 2 && dayOfWeek <= 7) {
            monthStart = dayOfWeek - 2;//0位置周一
        } else if (dayOfWeek == 1) {
            monthStart = 6;//空6格 日最后
        }
        curStartIndex = monthStart;//0-6
        date[monthStart] = 1;//日期1开始的地方
        int daysOfMonth = daysOfCurrentMonth();//每月的天数
        for (int i = 1; i < daysOfMonth; i++) {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + daysOfMonth;
    }

    public static final int DAYS_OF_MONTH[][] = new int[][]{
            {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
            {-1, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
    };

    private int daysOfCurrentMonth() {
        return DAYS_OF_MONTH[leap(selectedYear)][selectedMonth];
    }

    /**
     * 闰年 为 1
     *
     * @param year
     * @return
     */
    private static int leap(int year) {
        return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? 1 : 0;
    }

    /**
     * width is MeasureSpec.AT_MOST,set w is *0.5f
     * height is widthMeasureSpec.size/7f*n
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.i("TAG-1", widthSpecMode + "/" + widthSpecSize + "/" + getMeasuredWidth());
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i("TAG-2", heightSpecMode + "/" + heightSpecSize);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() / 2, MeasureSpec.EXACTLY);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measureHeight(widthMeasureSpec), View.MeasureSpec.EXACTLY);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() / 2, MeasureSpec.EXACTLY);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measureHeight(widthMeasureSpec), View.MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight(int widthMeasureSpec) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysOfMonth = daysOfCurrentMonth();//天数
        int numberOfDaysExceptFirstLine = -1;
        if (dayOfWeek >= 2 && dayOfWeek <= 7) {
            numberOfDaysExceptFirstLine = daysOfMonth - (8 - dayOfWeek + 1);
        } else if (dayOfWeek == 1) {
            numberOfDaysExceptFirstLine = daysOfMonth - 1;
        }
//        int lines = 2 + numberOfDaysExceptFirstLine / 7 + (numberOfDaysExceptFirstLine % 7 == 0 ? 0 : 1);
        int lines = 7;
        return (int) (MeasureSpec.getSize(widthMeasureSpec) / 7.0f * 0.8f * lines);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemWidth = w / 7.0f;
        itemHeight = itemWidth * 0.8f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制头标
        drawWeekTitle(canvas);
        //日期数字绘制 1-31
        drawDateItem(canvas);
    }

    private void drawWeekTitle(Canvas canvas) {
        RectF rectF = new RectF(0f, 0f, getWidth(), itemHeight);
        bgPaint.setColor(getResources().getColor(R.color.activity_background_gray));
        canvas.drawRect(rectF, bgPaint);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float baseline = (0f + itemHeight - fontMetrics.bottom - fontMetrics.top) / 2;
        for (int i = 0; i < 7; i++) {
            float weekTextX = itemWidth * i + itemWidth * 0.5f - textPaint.measureText(weekText[i]) * 0.5f;
            if (i == 5 || i == 6) {
                textPaint.setColor(getResources().getColor(R.color.activity_hint_gray));
            } else {
                textPaint.setColor(getResources().getColor(R.color.activity_textColor));
            }
            canvas.drawText(weekText[i], weekTextX, baseline, textPaint);
        }
    }

    private void drawDateItem(Canvas canvas) {
        RectF rectF = new RectF(0f, itemHeight, getWidth(), itemHeight * 7);
        bgPaint.setColor(getResources().getColor(R.color.activity_background_white));
        canvas.drawRect(rectF, bgPaint);

        for (int i = curStartIndex; i < curEndIndex; i++) {
            int x = getXByIndex(i);
            int y = getYByIndex(i);
            float top = itemHeight + (y - 1) * itemHeight;//第一个的高
            float bottom = top + itemHeight;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float baseline = (top + bottom - fontMetrics.bottom - fontMetrics.top) / 2;//把基准线定于垂直中心位置
            float startX = itemWidth * (x - 1) + itemWidth * 0.5f - textPaint.measureText(String.valueOf(date[i])) * 0.5f;
            if (x == 6 || x == 7) {//数字字体颜色
                textPaint.setColor(getResources().getColor(R.color.activity_hint_gray));
            } else {
                textPaint.setColor(getResources().getColor(R.color.activity_textColor));
            }

            if (date[i] == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                if (actionDownIndex == -1) {
                    canvas.drawCircle(
                            itemWidth * (x - 1) + itemWidth * 0.5f, top + itemHeight* 0.5f, itemHeight / 3, roundPaint);
                } else {
                    canvas.drawCircle(
                            itemWidth * (x - 1) + itemWidth * 0.5f, top + itemHeight * 0.5f, itemHeight / 3, circlePaint);
                }
            }

            if (actionDownIndex == i) {
                canvas.drawCircle(
                        itemWidth * (x - 1) + itemWidth * 0.5f, top +itemHeight * 0.5f, itemHeight / 3, roundPaint);
            }
            canvas.drawText(String.valueOf(date[i]), startX, baseline, textPaint);

            if (mErrorMap != null && !mErrorMap.isEmpty()) {
                if (mErrorMap.containsKey(date[i])) {

                    canvas.drawCircle(itemWidth * (x - 1) + itemWidth * 0.5f, baseline + 8f, 5f, pointPaint);
                }
            }
        }
    }

    private int getYByIndex(int i) {
        return i / 7 + 1;//第一行，...
    }

    private int getXByIndex(int i) {
        return i % 7 + 1;//1-7循环
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //在本空间中的位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (coordIsCalendar(y)) {
                    int index = getIndexByCoordinate(x, y);
                    if (isLegalIndex(index)) actionDownIndex = index;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (coordIsCalendar(y)) {
                    int index = getIndexByCoordinate(x, y);
                    if (isLegalIndex(index)) {
                        actionDownIndex = -1;
                        int day = date[index];
                        if (mItemByClick != null) mItemByClick.onItemClick(day);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 检测位置是否为有效的
     *
     * @param index 0-42date[]中
     * @return
     */
    private boolean isLegalIndex(int index) {
        return !isIllegalIndex(index);
    }

    private boolean isIllegalIndex(int i) {
        return i < curStartIndex || i >= curEndIndex;
    }

    private boolean coordIsCalendar(float y) {
        return y > itemHeight;
    }

    private int getIndexByCoordinate(float x, float y) {
        int m = (int) (Math.floor(x / itemWidth) + 1);
        int n = (int) (Math.floor((y - itemHeight) / itemHeight) + 1);
        return (n - 1) * 7 + m - 1;
    }

    public void setOnItemListener(OnItemByClick onItemByClick) {
        this.mItemByClick = onItemByClick;
    }

    public interface OnItemByClick {

        void onItemClick(int day);
    }

    public int getYear() {
        return selectedYear;
    }

    public int getMonth() {
        return selectedMonth;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void refreshPrevMonth() {
        Calendar c = getCalendar();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        selectedYear = c.get(Calendar.YEAR);
        selectedMonth = c.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        initial(calendar);
        invalidate();
    }


    public void refreshNextMonth() {
        Calendar c = getCalendar();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        selectedYear = c.get(Calendar.YEAR);
        selectedMonth = c.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        initial(calendar);
        invalidate();
    }

    public void setErrorData(ArrayMap<Integer, Boolean> hashMap) {
        mErrorMap = hashMap;
        invalidate();
    }

}
