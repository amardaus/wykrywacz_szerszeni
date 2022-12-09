package com.example.wykrywaczszerszeni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

public class WaveFormView extends View {
    private View mView;
    Context context;
    private ArrayList<Float> amplitudes;
    private ArrayList<RectF> spikes;
    private final float radius = 6f;
    private final float d = 6f;
    private final float w = 9f;
    private float screen_w = 0f;
    private float screen_h = 550f;
    private int maxSpikes = 0;

    public WaveFormView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.context = ctx;
        screen_w = ctx.getResources().getDisplayMetrics().widthPixels;
        init();
    }

    private void init(){
        amplitudes = new ArrayList<>();
        spikes = new ArrayList<>();
        maxSpikes = (int) (screen_w/(w+d));

        //DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        //screen_h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  200, metrics);
    }

    public void addAmplitude(Float amp){
        float norm = Math.min((amp/7),screen_h);
        amplitudes.add(norm);

        spikes.clear();
        ArrayList<Float> amps = new ArrayList<>(amplitudes.subList(Math.max(amplitudes.size() - maxSpikes,0),amplitudes.size()));
        for(int i = 0; i < amps.size(); i++){
            float left = screen_w - i*(w+d);
            float top = screen_h/2 - amps.get(i)/2;
            float right = left + w;
            float bottom = top + amps.get(i);
            spikes.add(new RectF(left,top,right,bottom));
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = 100;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        paint.setColor(getResources().getColor(R.color.red));
        for (RectF spike:spikes) {
            canvas.drawRoundRect(spike,radius,radius,paint);
        }
    }


}
