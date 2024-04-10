package info.sigmaclient.sigma.utils.render.blurs;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.utils.render.ShaderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

import static info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.resetColor;
import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.utils.render.ColorUtils.interpolateColorC;
import static info.sigmaclient.sigma.utils.render.RenderUtils.setAlphaLimit;
import static info.sigmaclient.sigma.utils.render.RenderUtils.startBlend;

public class Gradient {

    private static final ShaderUtil gradientMaskShader = new ShaderUtil("gradientMask");
    private static final ShaderUtil gradientShader = new ShaderUtil("gradient");



    public static void drawGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        ScaledResolution sr = new ScaledResolution(mc);

        setAlphaLimit(0);
        resetColor();
        startBlend();
        gradientShader.init();
        gradientShader.setUniformf("location", x * sr.getScaleFactor(), (Minecraft.getInstance().getMainWindow().getHeight() - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        gradientShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        // Bottom Left
        gradientShader.setUniformf("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f, alpha);
        //Top left
        gradientShader.setUniformf("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f, alpha);
        //Bottom Right
        gradientShader.setUniformf("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f, alpha);
        //Top Right
        gradientShader.setUniformf("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f, alpha);

        //Apply the gradient to whatever is put here
        ShaderUtil.drawQuads(x, y, width, height);

        gradientShader.unload();
    }

    public static void drawGradientLR(float x, float y, float width, float height, float alpha, Color left, Color right) {
        drawGradient(x, y, width, height, alpha, left, left, right, right);
    }

    public static void drawGradientTB(float x, float y, float width, float height, float alpha, Color top, Color bottom) {
        drawGradient(x, y, width, height, alpha, bottom, top, bottom, top);
    }


    public static void applyGradientHorizontal(float x, float y, float width, float height, float alpha, Color left, Color right, Runnable content) {
        applyGradient(x, y, width, height, alpha, left, left, right, right, content);
    }

    public static void applyGradientVertical(float x, float y, float width, float height, float alpha, Color top, Color bottom, Runnable content) {
        applyGradient(x, y, width, height, alpha, bottom, top, bottom, top, content);
    }


    public static void applyGradientCornerRL(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topRight, Runnable content) {
        Color mixedColor = interpolateColorC(topRight, bottomLeft, .5f);
        applyGradient(x, y, width, height, alpha, bottomLeft, mixedColor, mixedColor, topRight, content);
    }
    public static void applyGradientCornerLR(float x, float y, float width, float height, float alpha, Color bottomRight, Color topLeft, Runnable content) {
        Color mixedColor = interpolateColorC(bottomRight, topLeft, .5f);
        applyGradient(x, y, width, height, alpha, mixedColor, topLeft, bottomRight, mixedColor, content);
    }

    public static void applyGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight, Runnable content) {
        resetColor();
        startBlend();
        gradientMaskShader.init();

        ScaledResolution sr = new ScaledResolution(mc);

        gradientMaskShader.setUniformf("location", x * sr.getScaleFactor(), (Minecraft.getInstance().getMainWindow().getHeight() - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        gradientMaskShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        gradientMaskShader.setUniformf("alpha", alpha);
        gradientMaskShader.setUniformi("tex", 0);
        // Bottom Left
        gradientMaskShader.setUniformf("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f);
        //Top left
        gradientMaskShader.setUniformf("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f);
        //Bottom Right
        gradientMaskShader.setUniformf("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f);
        //Top Right
        gradientMaskShader.setUniformf("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f);

        //Apply the gradient to whatever is put here
        content.run();

        gradientMaskShader.unload();
    }


}
