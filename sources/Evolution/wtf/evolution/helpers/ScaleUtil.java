package wtf.evolution.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ScaleUtil {
    public static float size = 2;

    public static void scale_pre() {
        final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        final double scale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2);
        GL11.glPushMatrix();
        GL11.glScaled(scale * size, scale * size, scale * size);
    }

    public static void scale_post() {
        GL11.glScaled(size, size, size);
        GL11.glPopMatrix();
    }

    public static int calc(int value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return (int) (value * rs.getScaleFactor() / size);
    }
    public static int calc(float value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return (int) (value * rs.getScaleFactor() / size);
    }

    public static float[] calc(float mouseX, float mouseY) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        mouseX = mouseX *  rs.getScaleFactor() / size;
        mouseY = mouseY *  rs.getScaleFactor() / size;
        return new float[]{mouseX, mouseY};
    }

}
