package dev.tenacity.util.render.animation;

import dev.tenacity.util.misc.TimerUtil;

public abstract class AbstractAnimation {

    private final TimerUtil timerUtil = new TimerUtil();

    protected int duration;
    protected double endPoint;
    protected AnimationDirection direction;

    public AbstractAnimation(final int duration, final double endPoint, final AnimationDirection direction) {
        this.duration = duration;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public double getLinearOutput() {
        return 1 - ((timerUtil.getTime() / (double) duration) * endPoint);
    }

    public AbstractAnimation setDirection(final AnimationDirection direction) {
        if(this.direction != direction) {
            this.direction = direction;
            timerUtil.setTime(System.currentTimeMillis() - (duration - Math.min(duration, timerUtil.getTime())));
        }
        return this;
    }

    public boolean isFinished(final AnimationDirection direction) {
        return isDone() && this.direction.equals(direction);
    }

    protected boolean correctOutput() {
        return false;
    }

    public Double getOutput() {
        if(direction.isForward()) {
            if(isDone())
                return endPoint;

            return getEquation(timerUtil.getTime() / (double) duration) * endPoint;
        } else {
            if(isDone())
                return 0d;

            if(correctOutput()) {
                final double revTime = Math.min(duration, Math.max(0, duration - timerUtil.getTime()));
                return getEquation(revTime / (double) duration) * endPoint;
            }

            return (1 - getEquation(timerUtil.getTime() / (double) duration)) * endPoint;
        }
    }

    public boolean isDone() {
        return timerUtil.hasTimeElapsed(duration, false);
    }

    public void reset() {
        timerUtil.reset();
    }

    protected abstract double getEquation(final double x);

}
