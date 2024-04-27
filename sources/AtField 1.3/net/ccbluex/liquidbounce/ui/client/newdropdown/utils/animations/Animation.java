/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.TimerUtil;

public abstract class Animation {
    public TimerUtil timerUtil = new TimerUtil();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public double getEndPoint() {
        return this.endPoint;
    }

    public void reset() {
        this.timerUtil.reset();
    }

    public void setDuration(int n) {
        this.duration = n;
    }

    protected boolean correctOutput() {
        return false;
    }

    public boolean finished(Direction direction) {
        return this.isDone() && this.direction.equals((Object)direction);
    }

    protected abstract double getEquation(double var1);

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTime())));
        }
    }

    public double getLinearOutput() {
        return 1.0 - (double)this.timerUtil.getTime() / (double)this.duration * this.endPoint;
    }

    public Animation(int n, double d) {
        this.duration = n;
        this.endPoint = d;
        this.direction = Direction.FORWARDS;
    }

    public Animation(int n, double d, Direction direction) {
        this.duration = n;
        this.endPoint = d;
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public double getOutput() {
        if (this.direction == Direction.FORWARDS) {
            if (this.isDone()) {
                return this.endPoint;
            }
            return this.getEquation(this.timerUtil.getTime()) * this.endPoint;
        }
        if (this.isDone()) {
            return 0.0;
        }
        if (this.correctOutput()) {
            double d = Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
            return this.getEquation(d) * this.endPoint;
        }
        return (1.0 - this.getEquation(this.timerUtil.getTime())) * this.endPoint;
    }

    public void changeDirection() {
        this.setDirection(this.direction.opposite());
    }

    public void setEndPoint(double d) {
        this.endPoint = d;
    }

    public boolean isDone() {
        return this.timerUtil.hasTimeElapsed(this.duration);
    }
}

