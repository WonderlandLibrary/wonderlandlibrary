/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.TimerUtil;
import net.ccbluex.liquidbounce.utils.misc.Direction;

public abstract class Animation2323 {
    public TimerUtil timerUtil = new TimerUtil();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public Animation2323(int ms, double endPoint) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = Direction.FORWARDS;
    }

    public Animation2323(int ms, double endPoint, Direction direction) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public boolean finished(Direction direction) {
        return this.isDone() && this.direction.equals((Object)direction);
    }

    public double getLinearOutput() {
        return 1.0 - (double)this.timerUtil.getTime() / (double)this.duration * this.endPoint;
    }

    public double getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(double endPoint) {
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

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTime())));
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected boolean correctOutput() {
        return false;
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
            double revTime = Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
            return this.getEquation(revTime) * this.endPoint;
        }
        return (1.0 - this.getEquation(this.timerUtil.getTime())) * this.endPoint;
    }

    protected abstract double getEquation(double var1);
}

