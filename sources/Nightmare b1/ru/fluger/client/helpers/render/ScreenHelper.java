// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render;

public class ScreenHelper
{
    private float x;
    private float y;
    
    public ScreenHelper(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public void calculateCompensation(final float targetX, final float targetY, final double xSpeed, final double ySpeed) {
        final int deltaX = (int)(Math.abs(targetX - this.x) * xSpeed);
        final int deltaY = (int)(Math.abs(targetY - this.y) * ySpeed);
        this.x = AnimationHelper.calculateCompensation(targetX, this.x, (float)(bib.frameTime * 0.5), deltaX);
        this.y = AnimationHelper.calculateCompensation(targetY, this.y, (float)(bib.frameTime * 0.5), deltaY);
    }
    
    public void calculateCompensation(final float targetX, final float targetY, final float xSpeed, final float ySpeed) {
        final int deltaX = (int)(Math.abs(targetX - this.x) * xSpeed);
        final int deltaY = (int)(Math.abs(targetY - this.y) * ySpeed);
        this.x = AnimationHelper.calculateCompensation(targetX, this.x, (float)(bib.frameTime * 0.5), deltaX);
        this.y = AnimationHelper.calculateCompensation(targetY, this.y, (float)(bib.frameTime * 0.5), deltaY);
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
}
