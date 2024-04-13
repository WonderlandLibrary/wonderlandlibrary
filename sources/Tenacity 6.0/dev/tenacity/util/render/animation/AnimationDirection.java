package dev.tenacity.util.render.animation;

public enum AnimationDirection {

    FORWARDS,
    BACKWARDS;

    public AnimationDirection getOpposite() {
        return this == AnimationDirection.FORWARDS ? AnimationDirection.BACKWARDS : AnimationDirection.FORWARDS;
    }

    public boolean isForward() {
        return this == AnimationDirection.FORWARDS;
    }

    public boolean isBackward() {
        return this == AnimationDirection.BACKWARDS;
    }

}
