// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;

public class RenderUtils
{
    public static boolean SetCustomYaw;
    public static float CustomYaw;
    public static boolean SetCustomPitch;
    public static float CustomPitch;
    public static Minecraft mc;
    
    static {
        RenderUtils.SetCustomYaw = false;
        RenderUtils.CustomYaw = 0.0f;
        RenderUtils.SetCustomPitch = false;
        RenderUtils.CustomPitch = 0.0f;
        RenderUtils.mc = Minecraft.getMinecraft();
    }
    
    public static void setCustomYaw(final float customYaw) {
        RenderUtils.CustomYaw = customYaw;
        RenderUtils.SetCustomYaw = true;
        RenderUtils.mc.thePlayer.rotationYawHead = customYaw;
    }
    
    public static void resetPlayerYaw() {
        RenderUtils.SetCustomYaw = false;
    }
    
    public static float getCustomYaw() {
        return RenderUtils.CustomYaw;
    }
    
    public static void setCustomPitch(final float customPitch) {
        RenderUtils.CustomPitch = customPitch;
    }
    
    public static boolean isCustomPitch() {
        return RenderUtils.CustomPitch != 0.0f;
    }
    
    public static void resetPlayerPitch() {
        RenderUtils.SetCustomPitch = false;
    }
    
    public static float getCustomPitch() {
        return RenderUtils.CustomPitch;
    }
    
    public static int getColorFromPercentage(final float current, final float max) {
        final float percentage = current / max / 3.0f;
        return Color.HSBtoRGB(percentage, 1.0f, 1.0f);
    }
}
