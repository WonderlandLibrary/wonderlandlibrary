package info.sigmaclient.sigma.utils.render.swoothui;

public class AnimatedFloat extends AnimatedValue<Float> {
    private final float delta;
    private final boolean isDeltaGMul;

    public AnimatedFloat(Float from, Float to, float delta, boolean isDeltaGMul) {
        super(from, to);
        this.delta = delta;
        this.isDeltaGMul = isDeltaGMul;
    }

    public AnimatedFloat(Float from, Float to, float delta) {
        this(from, to, delta, false);
    }

    @Override
    protected Float update(Float current, Target target) {
        float targetValue = target == Target.FROM ? from : to;
        if (current < targetValue) {
            if (isDeltaGMul) {
                current += (to * delta);
            } else {
                current += delta;
            }
            if (current > targetValue) {
                current = targetValue;
            }
        } else if (current > targetValue) {
            if (isDeltaGMul) {
                current -= (to * delta);
            } else {
                current -= delta;
            }
            if (current < targetValue) {
                current = targetValue;
            }
        }

        return current;
    }
}
