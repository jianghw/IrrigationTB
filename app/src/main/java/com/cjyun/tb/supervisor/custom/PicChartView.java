package com.cjyun.tb.supervisor.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cjyun.tb.R;
import com.cjyun.tb.supervisor.bean.PieItemBean;
import com.cjyun.tb.supervisor.util.UIUtils;

/**
 * Created by Administrator on 2016/5/7 0007.
 * <p/>
 * 画圆
 */
public class PicChartView extends View {

    private static final String TAG = "PieChartView";
    private final Context context;
    private int screenW, screenH;
    /**
     * 文本的画笔，圆的画笔，和线
     */
    private Paint textPaint, piePaint, linePaint;

    /**
     * 饼的圆心和半径
     */
    private int pieCenterX, pieCenterY, pieRadius;
    /**
     * The oval to draw the oval in.
     */
    private RectF pieOval;

    private float smallMargin;

    private int[] mPieColors = new int[]{getResources().getColor(R.color.colorPicYet), getResources().getColor(R.color.colorPicNot)};

    private PieItemBean[] mPieItems;  // 多少个饼
    private float totalValue;

    public PicChartView(Context context) {
        this(context, null);
    }

    public PicChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        //init screen
        /*screenW = ScreenUtils.getScreenW(context);
        screenH = ScreenUtils.getScreenH(context);*/
        screenW = getContext().getResources().getDisplayMetrics().widthPixels;
        screenH = getContext().getResources().getDisplayMetrics().heightPixels;

        Log.d(TAG, "this device screen is " + screenW + "---" + screenH);//0,0
        // 圆心偏向Y方向偏400px值
    /*    pieCenterX = screenW / 2;//360
//        pieCenterY = screenH / 3 - 400;//426.7~426
        pieCenterY = (int)(screenH / 5.0f);//426.7~426
//        pieRadius = screenW / 4;//180
        pieRadius = screenW / 4;//180
        smallMargin = UIUtils.dip2px(context, 5);

        pieOval = new RectF();
        pieOval.left = pieCenterX - pieRadius;//180
        pieOval.top = pieCenterY - pieRadius;//246
        pieOval.right = pieCenterX + pieRadius;//540
        pieOval.bottom = pieCenterY + pieRadius;//

        Log.d(TAG, "pieOval.left=" + pieOval.left);//180.0
        Log.d(TAG, "pieOval.top=" + pieOval.top);//246.0
        Log.d(TAG, "pieOval.right=" + pieOval.right);//540.0
        Log.d(TAG, "pieOval.bottom=" + pieOval.bottom);//606.0*/
        //The paint to draw text.
        textPaint = new Paint();
        textPaint.setAntiAlias(true); // 抗锯齿
        textPaint.setTextSize(UIUtils.dip2px(context, 10));

        //The paint to draw circle.
        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);

        //The paint to draw line to show the concrete text
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(UIUtils.dip2px(context, 1));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), View.MeasureSpec.EXACTLY);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), View.MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        pieCenterX = w / 2;//360
        pieCenterY = h / 2;//426.7~426
        pieRadius = w / 4;//180
        smallMargin = UIUtils.dip2px(context, 5);

        pieOval = new RectF();
        pieOval.left = pieCenterX - pieRadius;//180
        pieOval.top = pieCenterY - pieRadius;//246
        pieOval.right = pieCenterX + pieRadius;//540
        pieOval.bottom = pieCenterY + pieRadius;//

        Log.d(TAG, "pieOval.left=" + pieOval.left);//180.0
        Log.d(TAG, "pieOval.top=" + pieOval.top);//246.0
        Log.d(TAG, "pieOval.right=" + pieOval.right);//540.0
        Log.d(TAG, "pieOval.bottom=" + pieOval.bottom);//606.0

    }

    //The degree position of the last item arc's center.
    private float lastDegree = 0;
    //The count of the continues 'small' item.
    private int addTimes = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPieItems != null && mPieItems.length > 0) {
            float start = -90.0f;// 起始角度
            for (int i = 0; i < mPieItems.length; i++) {
                if (i == mPieItems.length - 1) {
                    pieOval.left = pieCenterX - pieRadius - 3;  // 圆的差距 10
                    pieOval.top = pieCenterY - pieRadius;
                    pieOval.right = pieCenterX + pieRadius;
                    pieOval.bottom = pieCenterY + pieRadius - 3;
                }
                //画圆
                piePaint.setColor(mPieColors[i % mPieColors.length]);
                float sweep = mPieItems[i].getItemValue() / totalValue * 360; // 扫过的角度
                canvas.drawArc(pieOval, start, sweep, true, piePaint);

                //画圆对应的线
                float radians = (float) ((start + sweep / 2) / 180 * Math.PI);  // 线处在中间的位置
                float lineStartX = pieCenterX + pieRadius * 0.99f * (float) (Math.cos(radians));
                float lineStartY = pieCenterY + pieRadius * 0.99f * (float) (Math.sin(radians));

                float lineStopX, lineStopY;
                float rate;
                if (getOffset(start + sweep / 2) > 60) {
                    //    rate = 1.3f;
                    rate = 1.2f;
                } else if (getOffset(start + sweep / 2) > 30) {
                    //      rate = 1.2f;
                    rate = 1.2f;
                } else {
                    rate = 1.2f;
                    // rate = 1.1f;
                }
                //如果项目非常小,使文本远离圆,以避免被其他隐藏的文本
               /* if (start + sweep / 2 - lastDegree < 30) {
                    addTimes++;
                    rate += 0.2f * addTimes;
                } else {
                    addTimes = 0;
                }*/
                // 画横线
                lineStopX = pieCenterX + pieRadius * rate * (float) (Math.cos(radians));
                lineStopY = pieCenterY + pieRadius * rate * (float) (Math.sin(radians));

               /* if (i == mPieItems.length - 1) {

                    canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, linePaint);
                }*/
                canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, linePaint);

                //write text
                String itemTypeText = mPieItems[i].getItemType();

                //String itemPercentText = Utility.formatFloat(mPieItems[i].getItemValue() / totalValue * 100) + "%";
                //设置百分比
                // String itemPercentText = (int) (mPieItems[i].getItemValue() / totalValue * 100) + "%";
                // 设置天数
                String itemPercentText = (int) (mPieItems[i].getItemValue()) + "天";

                float itemTypeTextLen = textPaint.measureText(itemTypeText);
                float itemPercentTextLen = textPaint.measureText(itemPercentText);
                float lineTextWidth = Math.max(itemTypeTextLen, itemPercentTextLen);

                float textStartX = lineStopX;
                float textStartY = lineStopY - smallMargin;
                float percentStartX = lineStopX;
                float percentStartY = lineStopY + textPaint.getTextSize();
                if (lineStartX > pieCenterX) {
                    textStartX += (smallMargin + Math.abs(itemTypeTextLen - lineTextWidth) / 2);
                    percentStartX += (smallMargin + Math.abs(itemPercentTextLen - lineTextWidth) / 2);
                } else {
                    textStartX -= (smallMargin + lineTextWidth - Math.abs(itemTypeTextLen - lineTextWidth) / 2);
                    percentStartX -= (smallMargin + lineTextWidth - Math.abs(itemPercentTextLen - lineTextWidth) / 2);
                }
                canvas.drawText(itemTypeText, textStartX, textStartY, textPaint);
                //draw percent text
                canvas.drawText(itemPercentText, percentStartX, percentStartY, textPaint);

                //draw text underline
                float textLineStopX = lineStopX;
                if (lineStartX > pieCenterX) {
                    textLineStopX += (lineTextWidth + smallMargin * 2);
                } else {
                    textLineStopX -= (lineTextWidth + smallMargin * 2);
                }
                //  canvas.drawLine(lineStopX, lineStopY, textLineStopX, lineStopY, linePaint);

                lastDegree = start + sweep / 2;
                start += sweep;


            }
        }
    }

    public PieItemBean[] getPieItems() {
        return mPieItems;
    }

    public void setPieItems(PieItemBean[] pieItems) {
        this.mPieItems = pieItems;

        totalValue = 0;
        for (PieItemBean item : mPieItems) {
            totalValue += item.getItemValue();
        }
        invalidate();
    }

    //get the pianyi
    private float getOffset(float radius) {
        int a = (int) (radius % 360 / 90);
        switch (a) {
            case 0:
                return radius;
            case 1:
                return 180 - radius;
            case 2:
                return radius - 180;
            case 3:
                return 360 - radius;
        }
        return radius;
    }


}