package vestige.util.animation;

import vestige.setting.impl.EnumModeSetting;
import vestige.setting.impl.IntegerSetting;

import java.util.function.Supplier;

public class AnimationUtil {

    public static EnumModeSetting<AnimationType> getAnimationType(AnimationType defaultAnim) {
        return new EnumModeSetting<>("Animation", defaultAnim, AnimationType.values());
    }

    public static EnumModeSetting<AnimationType> getAnimationType(Supplier<Boolean> visibility, AnimationType defaultAnim) {
        return new EnumModeSetting<>("Animation", visibility, defaultAnim, AnimationType.values());
    }

    public static IntegerSetting getAnimationDuration(int defaultDuration) {
        return new IntegerSetting("Animation duration", defaultDuration, 0, 1000, 25);
    }

    public static IntegerSetting getAnimationDuration(Supplier<Boolean> visibility, int defaultDuration) {
        return new IntegerSetting("Animation duration", visibility, defaultDuration, 0, 1000, 25);
    }

}