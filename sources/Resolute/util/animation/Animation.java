// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.animation;

import vip.Resolute.util.misc.TimerUtils;

public abstract class Animation
{
    public TimerUtils timer;
    protected int duration;
    protected double endPoint;
    protected Enum<Direction> direction;
    
    public Animation(final int ms, final double endPoint) {
        this.timer = new TimerUtils();
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = Direction.FORWARDS;
    }
    
    public Animation(final int ms, final double endPoint, final Enum<Direction> direction) {
        this.timer = new TimerUtils();
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }
    
    public double getTimerOutput() {
        return this.timer.getTime() / (double)this.duration;
    }
    
    public double getEndPoint() {
        return this.endPoint;
    }
    
    public void reset() {
        this.timer.reset();
    }
    
    public boolean isDone() {
        return this.timer.hasTimeElapsed(this.duration, false);
    }
    
    public void changeDirection() {
        if (this.direction == Direction.FORWARDS) {
            this.direction = Direction.BACKWARDS;
        }
        else {
            this.direction = Direction.FORWARDS;
        }
        this.timer.setTime(System.currentTimeMillis() - (this.duration - Math.min(this.duration, this.timer.getTime())));
    }
    
    public Enum<Direction> getDirection() {
        return this.direction;
    }
    
    public void setDirection(final Enum<Direction> direction) {
        if (this.direction != direction) {
            this.timer.setTime(System.currentTimeMillis() - (this.duration - Math.min(this.duration, this.timer.getTime())));
            this.direction = direction;
        }
    }
    
    public void setDuration(final int duration) {
        this.duration = duration;
    }
    
    public double getOutput() {
        if (this.direction == Direction.FORWARDS) {
            if (this.isDone()) {
                return this.endPoint;
            }
            return this.getEquation((double)this.timer.getTime()) * this.endPoint;
        }
        else {
            if (this.isDone()) {
                return 0.0;
            }
            return (1.0 - this.getEquation((double)this.timer.getTime())) * this.endPoint;
        }
    }
    
    protected abstract double getEquation(final double p0);
}
