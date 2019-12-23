package com.github.webninjasi.sandboxgl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private double avarageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        int frameCount = 0;
        long totalTime = 0;
        long lastTime = System.nanoTime();

        while(running){
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update((System.nanoTime() - lastTime) / 1000.0 / 1000.0 / 1000.0);
                    this.gamePanel.draw(canvas, avarageFPS);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {e.printStackTrace();}
                }
            }

            totalTime += System.nanoTime() - lastTime;
            lastTime = System.nanoTime();

            frameCount++;
            if(totalTime >= 1000000000) {
                avarageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println(avarageFPS);
            }
        }
    }
}
