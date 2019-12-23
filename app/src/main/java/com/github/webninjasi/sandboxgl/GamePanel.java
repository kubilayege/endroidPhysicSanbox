package com.github.webninjasi.sandboxgl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Hashtable<Pair<Integer, Integer>, SParticle> particleMap;
    private List<SParticle> particles = new ArrayList<SParticle>();
    private Paint mPaint[] = new Paint[7];
    private Paint textPaint;

    private int width;
    private int height;

    private int particleSize = 6;
    private int maxParticleCount;
    private int maxSpeed = 1000;
    private int obstacleCount = 0;

    private Pair<Integer, Integer> gravity = Pair.create(0,10);

    private int brushRadius = 9;
    private int brushColor = 0;

    private boolean touchDown;
    private Pair<Integer, Integer> touchPos;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);
        setFocusable(true);

        mPaint[0] = new Paint();
        mPaint[0].setColor(Color.rgb(255,0,0));
        mPaint[1] = new Paint();
        mPaint[1].setColor(Color.rgb(0,255,0));
        mPaint[2] = new Paint();
        mPaint[2].setColor(Color.rgb(0,0,255));
        mPaint[3] = new Paint();
        mPaint[3].setColor(Color.rgb(0,255,255));
        mPaint[4] = new Paint();
        mPaint[4].setColor(Color.rgb(255,0,255));
        mPaint[5] = new Paint();
        mPaint[5].setColor(Color.rgb(255,255,0));
        mPaint[6] = new Paint();
        mPaint[6].setColor(Color.rgb(255,255,255));

        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.rgb(0,255,0));

        touchDown = false;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int m_ScreenHeight = displayMetrics.heightPixels - 300;
        int m_ScreenWidth = displayMetrics.widthPixels;

        width = m_ScreenWidth / particleSize;
        height = m_ScreenHeight / particleSize;

        maxParticleCount = width * height;

        particleMap = new Hashtable<Pair<Integer,Integer>, SParticle>(maxParticleCount);
        particles = new ArrayList<SParticle>(maxParticleCount);

        // Create obstacles
        for (int i = 0; i<width; i++){
            addParticle(Pair.create(i, 0), true);
            addParticle(Pair.create(i, height-1), true);
        }
        for (int i = 1; i<height-1; i++){
            addParticle(Pair.create(0, i), true);
            addParticle(Pair.create(width-1, i), true);
        }

        obstacleCount = width * 2 + (height-2) * 2;
    }

    public void brush(Pair<Integer, Integer> p){
        Pair<Integer, Integer> bp;
        for (int i = -brushRadius; i<=brushRadius; i++){
            for (int j = -brushRadius; j<=brushRadius; j++){
                bp = Pair.create(p.first + i, p.second + j);
                addParticle(bp, false);
            }
        }
    }

    public void addParticle(Pair<Integer, Integer> p, boolean obstacle){
        Pair<Integer, Integer> bp = Pair.create(p.first, p.second);
        if(!particleMap.containsKey(bp)) {
            if (p.first < 0 || p.first >= width || p.second < 0 || p.second >= height)
                return;
            if (!obstacle && (p.first == 0 || p.first == width-1 || p.second == 0 || p.second == height-1))
                return;

            SParticle ptcl = new SParticle(bp, particleSize, obstacle, gravity, maxSpeed, mPaint[brushColor]);
            particles.add(ptcl);
            particleMap.put(bp, ptcl);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(),this);

        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false );
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchPos = Pair.create((int)(event.getX()/particleSize),(int)event.getY()/particleSize);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDown = true;
                break;
            case MotionEvent.ACTION_UP:
                touchDown = false;
                break;
        }

        return true;
    }

    public void update(double deltaTime){
        if (touchDown) {
            brush(touchPos);
        }

        int particleCount = particles.size();
        Object list[] = particles.toArray();
        SParticle ptcl;
        double stepTime = 1.0 / maxSpeed;
        Pair<Integer, Integer> pos;
        Pair<Integer, Integer> oldpos;

        for (int i=0; i<particleCount; i++){
            ptcl = ((SParticle) list[i]);

            if (ptcl.isObstacle())
                continue;

            for (double t=0; t<deltaTime; t+=stepTime) {
                oldpos = ptcl.getPos();
                ptcl.update(stepTime);

                pos = ptcl.getPos();

                if (particleMap.containsKey(pos)) {
                    ptcl.setPos(oldpos);
                    break;
                }

                particleMap.remove(oldpos);
                particleMap.put(pos, ptcl);
            }
        }
    }

    public void draw(Canvas canvas, double avarageFPS) {
        super.draw(canvas);

        canvas.drawColor(Color.BLACK);

        int particleCount = particles.size();
        Object list[] = particles.toArray();

        for (int i=0; i<particleCount; i++){
            ((SParticle) list[i]).draw(canvas);
        }

        canvas.drawText(((int)avarageFPS) + " FPS/" + (particleCount-obstacleCount) + " Objs", 50, 50, textPaint);
    }

    public void setBrushRadius(int val){
        brushRadius = val;
    }

    public void setColor(int val){
        brushColor = val;
    }
}
