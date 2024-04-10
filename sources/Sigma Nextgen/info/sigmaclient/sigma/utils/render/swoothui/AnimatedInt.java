package info.sigmaclient.sigma.utils.render.swoothui;

public class AnimatedInt extends AnimatedValue<Integer> {
    private final int delta;

    public AnimatedInt(Integer from, Integer to, int delta) {
        super(from, to);
        this.delta = delta;
    }

    @Override
    protected Integer update(Integer current, Target target) {
        int targetValue = target == Target.FROM ? from : to;
        if (current > targetValue) {
            current -= delta;
            if (current < targetValue) current = targetValue;
        } else if (current < targetValue) {
            current += delta;
            if (current > targetValue) current = targetValue;
        }

        return current;
    }
}
