package com.example.mateialexandru.modernartui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class MyCustomView extends View {

    Paint paint = new Paint();
    final static private int STROKE = 20;

    private int mSeekProgess = 0;

    public MyCustomView(Context context,int progress) {
        super(context);
    }


    public MyCustomView(Context context) {
        super(context);
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setmSeekProgess(int mSeekProgess) {
        this.mSeekProgess = mSeekProgess;
    }

    @Override
    public void onDraw(Canvas canvas) {

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(STROKE);
        paint.setStyle(Paint.Style.STROKE);

        /*
        Draw borders for every rectangle
         */
        /*canvas.drawRect(0, 0, 2*getWidth()/5, getHeight()/2, paint);
        canvas.drawRect(0, getHeight()/2 , 2*getWidth()/5, getHeight(), paint);

        canvas.drawRect(2*(getWidth()/5) , 0 , getWidth(), getHeight()/3, paint);
        canvas.drawRect(2*(getWidth()/5) , getHeight()/3 , getWidth(), 2*getHeight()/3, paint);
        canvas.drawRect(2*(getWidth()/5) , 2*getHeight()/3 , getWidth(), getHeight(), paint);*/


        /*
        Fill rectangles with specific color
         */
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        //paint.setColor(Color.rgb(106,119,183));
        paint.setColor(0xFF6A77B7 + mSeekProgess);
        canvas.drawRect(STROKE, STROKE, 2*getWidth()/5- STROKE, getHeight()/2 - STROKE, paint );

        // paint.setColor(Color.rgb(214,79,151));
        paint.setColor(0xFFD64F97 + mSeekProgess);
        canvas.drawRect(STROKE, getHeight()/2  + STROKE, 2*getWidth()/5- STROKE , getHeight() - STROKE, paint );

        //Color.parseColor("0xA31D21");
        paint.setColor(0xFFA31D21 + mSeekProgess); //"));  //Color.rgb(163,29,33));
        canvas.drawRect(2*( getWidth()/5) + STROKE , STROKE, getWidth()- STROKE, getHeight()/3 - STROKE, paint );

        //paint.setColor(Color.rgb(255,255,255));
        paint.setColor(0xFFE6E6E6 + mSeekProgess);
        canvas.drawRect(2*( getWidth()/5) + STROKE , getHeight()/3 + STROKE, getWidth()- STROKE, 2*getHeight()/3 - STROKE, paint );

        //paint.setColor(Color.rgb(39,58,151));
        paint.setColor(0xFF273A97 + mSeekProgess);
        canvas.drawRect(2*( getWidth()/5) + STROKE , 2*getHeight()/3 + STROKE, getWidth()- STROKE, getHeight() - STROKE, paint );

    }

}


