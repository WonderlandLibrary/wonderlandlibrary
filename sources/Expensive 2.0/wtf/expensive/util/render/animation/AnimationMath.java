package wtf.expensive.util.render.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.MathHelper;
import net.optifine.Config;
import org.lwjgl.opengl.GL11;
import wtf.expensive.util.IMinecraft;

public class AnimationMath implements IMinecraft {
    public static double deltaTime() {
        return mc.debugFPS > 0 ? (1.0000 / mc.debugFPS) : 1;
    }

    public static float fast(float end, float start, float multiple) {
        return (1 - MathHelper.clamp((float) (AnimationMath.deltaTime() * multiple), 0, 1)) * end + MathHelper.clamp((float) (AnimationMath.deltaTime() * multiple), 0, 1) * start;
    }

    public static float lerp(float end, float start, float multiple) {
        return (float) (end + (start - end) * MathHelper.clamp(AnimationMath.deltaTime() * multiple, 0, 1));
    }

    public static double lerp(double end, double start, double multiple) {
        return (end + (start - end) * MathHelper.clamp(AnimationMath.deltaTime() * multiple, 0, 1));
    }

    public static void sizeAnimation(double width, double height, double scale) {
        GlStateManager.translated(width, height, 0);
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-width, -height, 0);
    }
}
