package tech.drainwalk.animation;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.utility.math.MathUtility;

public class Animation {
    @Setter @Getter
    private float value, prevValue;
    private float animationSpeed;
    private float fromValue, toValue;
    @Getter @Setter
    private float animationValue;

    public void update(boolean update) {
        prevValue = value;
        value = MathUtility.clamp(value + (update ? animationSpeed : -animationSpeed), fromValue, toValue);
    }

    public void animate(float fromValue, float toValue, float animationSpeed, EasingList.Easing easing, float partialTicks) {
        this.animationSpeed = animationSpeed;
        this.fromValue = fromValue;
        this.toValue = toValue;
        animationValue = easing.ease(MathUtility.interpolate(prevValue, value, partialTicks));
    }
}
