package com.github.webninjasi.sandboxgl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Pair;

import java.util.Vector;

public class SParticle  {
    private boolean obstacle;
    private float posX, posY, velX, velY;
    private Pair<Integer, Integer> acceleration;

    private int size;
    private Rect rect;
    private int maxSpeed;

    public SParticle(Pair<Integer, Integer> pos, int _size, boolean _obstacle, Pair<Integer, Integer> acc, int maxSpeed){
        posX = pos.first;
        posY = pos.second;
        velX = 0;
        velY = 0;
        acceleration = acc;
        size = _size;
        obstacle = _obstacle;
        this.maxSpeed = maxSpeed;
        rect = new Rect((int) posX * size - size/2,(int) posY * size - size/2,(int) posX * size + size/2,(int) posY * size + size/2);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    public void update(double stepTime) {
        double speed = Math.sqrt(velX * velX + velY * velY);
        if (speed < maxSpeed) {
            velX += acceleration.first * stepTime;
            velY += acceleration.second * stepTime;

            speed = Math.sqrt(velX * velX + velY * velY);
            if (speed > maxSpeed) {
                velX = (float) (velX / speed * maxSpeed);
                velY = (float) (velY / speed * maxSpeed);
            }
        }

        posX += velX * stepTime;
        posY += velY * stepTime;
        rect.offsetTo((int) posX * size - size/2, (int) posY * size - size/2);
    }

    public Pair<Integer, Integer> getPos() {
        return Pair.create((int) Math.ceil(posX), (int) Math.ceil(posY));
    }

    public void setPos(Pair<Integer, Integer> pos) {
        posX = pos.first;
        posY = pos.second;
        rect.offsetTo((int) posX * size - size/2, (int) posY * size - size/2);
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean state) {
        obstacle = state;
    }
}
