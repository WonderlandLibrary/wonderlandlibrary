package koks.api.utils;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
@Getter @Setter
public class Animation {

    float x, goalX, y, goalY;
    float speed;

    public Animation() {
    }

    public Animation(float x, float goalX, float y, float goalY, float speed) {
        this.x = x;
        this.y = y;
        this.goalX = goalX;
        this.goalY = goalY;
        this.speed = speed;
    }

    public boolean isFinished() {
        return Math.round(getX()) == getGoalX() && Math.round(getY()) == getGoalY();
    }

    public boolean hasXReached(float reachedX) {
        return Math.round(getX()) == reachedX;
    }

    public boolean hasYReached(float reachedY) {
        return Math.round(getY()) == reachedY;
    }

    private final Minecraft mc = Minecraft.getMinecraft();

    public float getAnimationX() {
        final Resolution resolution = Resolution.getResolution();

        if(x == goalX) return goalX;

        if(x < goalX) x += ((goalX - x) / resolution.getWidth()) * speed;
        if(x > goalX) x -= ((x - goalX) / resolution.getWidth()) * speed;
        return x;
    }

    public float getAnimationY() {
        final Resolution resolution = Resolution.getResolution();
        if(y == goalY) return goalY;

        if(y < goalY) y+= ((goalY - y) / resolution.getHeight())  * speed;
        if(y > goalY) y-= ((y - goalY) / resolution.getHeight()) * speed;
        return y;
    }
}
