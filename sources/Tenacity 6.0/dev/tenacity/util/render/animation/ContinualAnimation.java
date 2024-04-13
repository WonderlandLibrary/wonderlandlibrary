package dev.tenacity.util.render.animation;

import dev.tenacity.util.render.animation.impl.SmoothStepAnimation;

public final class ContinualAnimation {

    private float output, endpoint;

    private AbstractAnimation animation = new SmoothStepAnimation(0, 0, AnimationDirection.BACKWARDS);

    public void animate(final float destination, int duration) {
        output = endpoint - animation.getOutput().floatValue();
        endpoint = destination;
        if(output != (endpoint - destination))
            animation = new SmoothStepAnimation(duration, endpoint - output, AnimationDirection.BACKWARDS);
    }

    public float getOutput() {
        output = endpoint - animation.getOutput().floatValue();
        return output;
    }

}
