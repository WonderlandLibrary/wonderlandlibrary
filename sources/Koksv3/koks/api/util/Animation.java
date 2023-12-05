package koks.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author kroko
 * @created on 07.11.2020 : 18:56
 */
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
        return Math.round(getAnimationX()) == getGoalX() && Math.round(getAnimationY()) == getGoalY();
    }

    public boolean hasXReached(float reachedX) {
        return Math.round(getAnimationX()) == reachedX;
    }

    public boolean hasYReached(float reachedY) {
        return Math.round(getAnimationY()) == reachedY;
    }

    private Minecraft mc = Minecraft.getMinecraft();

    public float getAnimationX() {

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        if(x == goalX) return goalX;

        if(x < goalX) x+= ((goalX - x) / scaledResolution.getScaledWidth()) * speed;
        if(x > goalX) x-= ((x - goalX) / scaledResolution.getScaledWidth()) * speed;
        return x;
    }

    public float getAnimationY() {

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        if(y == goalY) return goalY;

        if(y < goalY) y+= speed;
        if(y > goalY) y-= speed;
        return y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getGoalX() {
        return goalX;
    }

    public void setGoalX(float goalX) {
        this.goalX = goalX;
    }

    public float getGoalY() {
        return goalY;
    }

    public void setGoalY(float goalY) {
        this.goalY = goalY;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
