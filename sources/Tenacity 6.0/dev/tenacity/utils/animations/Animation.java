// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations;

import dev.tenacity.utils.time.TimerUtil;

public abstract class Animation
{
    public TimerUtil timerUtil;
    protected int duration;
    protected double endPoint;
    protected Direction direction;
    
    public Animation(final int ms, final double endPoint) {
        this(ms, endPoint, Direction.FORWARDS);
    }
    
    public Animation(final int ms, final double endPoint, final Direction direction) {
        this.timerUtil = new TimerUtil();
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }
    
    public boolean finished(final Direction direction) {
        return this.isDone() && this.direction.equals(direction);
    }
    
    public double getLinearOutput() {
        return 1.0 - this.timerUtil.getTime() / (double)this.duration * this.endPoint;
    }
    
    public double getEndPoint() {
        return this.endPoint;
    }
    
    public void setEndPoint(final double endPoint) {
        this.endPoint = endPoint;
    }
    
    public void reset() {
        this.timerUtil.reset();
    }
    
    public boolean isDone() {
        return this.timerUtil.hasTimeElapsed(this.duration);
    }
    
    public void changeDirection() {
        this.setDirection(this.direction.opposite());
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public Animation setDirection(final Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - (this.duration - Math.min(this.duration, this.timerUtil.getTime())));
        }
        return this;
    }
    
    public void setDuration(final int duration) {
        this.duration = duration;
    }
    
    protected boolean correctOutput() {
        return false;
    }
    
    public Double getOutput() {
        if (this.direction.forwards()) {
            if (this.isDone()) {
                return this.endPoint;
            }
            return this.getEquation(this.timerUtil.getTime() / (double)this.duration) * this.endPoint;
        }
        else {
            if (this.isDone()) {
                return 0.0;
            }
            if (this.correctOutput()) {
                final double revTime = (double)Math.min(this.duration, Math.max(0L, this.duration - this.timerUtil.getTime()));
                return this.getEquation(revTime / this.duration) * this.endPoint;
            }
            return (1.0 - this.getEquation(this.timerUtil.getTime() / (double)this.duration)) * this.endPoint;
        }
    }
    
    protected abstract double getEquation(final double p0);
}
