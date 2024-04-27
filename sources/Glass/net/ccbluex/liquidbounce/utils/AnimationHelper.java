/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.value.BoolValue;

public class AnimationHelper {
    public float animationX;
    public int alpha;
    public int getAlpha() {
        return this.alpha;
    }
    public float getAnimationX() {
        return this.animationX;
    }
    public void resetAlpha() {
        this.alpha = 0;
    }
    public AnimationHelper() {
        this.alpha = 0;
    }
    public void updateAlpha(int speed) {
        if(alpha < 255)
            this.alpha += speed;
    }
    public AnimationHelper(BoolValue value) {
        animationX = value.get() ? 5 : -5;
    }
    public AnimationHelper(Module module) {
        animationX = module.getState() ? 5 : -5;
    }
}
