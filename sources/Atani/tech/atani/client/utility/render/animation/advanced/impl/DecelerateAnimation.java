package tech.atani.client.utility.render.animation.advanced.impl;

import tech.atani.client.utility.render.animation.advanced.Animation;
import tech.atani.client.utility.render.animation.advanced.Direction;

public class DecelerateAnimation extends Animation {

    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }


    protected double getEquation(double x) {
        return 1 - ((x - 1) * (x - 1));
    }
}
