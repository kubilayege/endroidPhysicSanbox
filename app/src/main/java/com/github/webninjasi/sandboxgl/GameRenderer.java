package com.github.webninjasi.sandboxgl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.widget.SeekBar;
import android.widget.TextView;

public class GameRenderer implements GLSurfaceView.Renderer {
    private GameWorld gWorld;
    private GameSurfaceView surfaceView;

    private long lastRenderTime;
    private int particleScale = 6;
    private int lastType;

    private int brushRadius = 4;
    private int brushType = 0;

    private int frameCount = 0;
    private long totalTime = 0;
    private long lastTime;
    private double avarageFPS;

    private TextView myInfoText;
    private Activity ctx;

    public GameRenderer(GameSurfaceView sv, Context context, TextView infoText){
        super();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int m_ScreenHeight = displayMetrics.heightPixels - 300;
        int m_ScreenWidth = displayMetrics.widthPixels;

        gWorld = new GameWorld(m_ScreenWidth/particleScale, m_ScreenHeight/particleScale, 1000, this, particleScale);

        surfaceView = sv;
        lastRenderTime = 0;
        lastType = 1;

        frameCount = 0;
        totalTime = 0;
        lastTime = System.nanoTime();

        ctx = (Activity) context;
        myInfoText = infoText;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES31.glClearColor(0f, 0f, 0f, 1.0f);

        gWorld.initialize();
    }

    public void onDrawFrame(GL10 unused) {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastRenderTime) / 1000 / 1000;
        deltaTime = deltaTime / 1000.0;
        if (lastRenderTime == 0) {
            lastRenderTime = currentTime;
            return;
        }

        float width = surfaceView.getWidth();
        float height = surfaceView.getHeight();
        float[] uScreen =
        {
            2f/width,   0f,         0f, 0f,
            0f,        -2f/height,  0f, 0f,
            0f,         0f,         0f, 0f,
            -1f,        1f,         0f, 1f
        };

        // Create new particle(s)
        if (surfaceView.isMouseDown()) {
            int x = (int) surfaceView.getMouseX();
            int y = (int) surfaceView.getMouseY();

            brush(Pair.create(x / particleScale, y / particleScale));
        }

        gWorld.onUpdate(uScreen, deltaTime);

        lastRenderTime = currentTime;

        totalTime += System.nanoTime() - lastTime;
        lastTime = System.nanoTime();

        frameCount++;
        if(totalTime >= 1000000000) {
            avarageFPS = 1000/((totalTime/frameCount)/1000000);
            frameCount = 0;
            totalTime = 0;
        }

        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myInfoText.setText(((int)avarageFPS) + " FPS/" + gWorld.getParticleCount() + " Objs");
            }
        });
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
    }

    public Resources getResources() {
        return surfaceView.getResources();
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES31.glCreateShader(type);
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);
        return shader;
    }


    public void brush(Pair<Integer, Integer> p){
        Pair<Integer, Integer> bp;
        for (int i = -brushRadius; i<=brushRadius; i++){
            for (int j = -brushRadius; j<=brushRadius; j++){
                bp = Pair.create(p.first + i, p.second + j);

                gWorld.createParticle(bp,brushType);
            }
        }
    }

    public void setBrushRadius(int brushRadius) {
        this.brushRadius = brushRadius;
    }

    public void setBrushType(int val) {
        this.brushType = val;
    }
}
