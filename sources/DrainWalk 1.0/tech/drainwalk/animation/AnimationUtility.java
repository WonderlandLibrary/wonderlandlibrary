package tech.drainwalk.animation;

import net.minecraft.client.renderer.GlStateManager;

public class AnimationUtility {
    public static void scaleAnimation(float x, float y, float width, float height, float scaleValue) {
        GlStateManager.translate((x + width / 2), (y + height / 2), 0);
        GlStateManager.scale(scaleValue, scaleValue, scaleValue);
        GlStateManager.translate(-(x + width / 2), -(y + height / 2), 0);
    }

}
