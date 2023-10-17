package de.tired.util.render;

import de.tired.util.render.RenderUtil;

public class AnimationUtil {

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) ((double) RenderUtil.instance.delta * speed);
        animation = animation < finalState ? (Math.min(animation + (double) add, finalState)) : (Math.max(animation - (double) add, finalState));
        return animation;
    }

}
