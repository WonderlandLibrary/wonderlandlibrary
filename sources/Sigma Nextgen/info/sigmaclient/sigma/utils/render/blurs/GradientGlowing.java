package info.sigmaclient.sigma.utils.render.blurs;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.ShaderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

import static info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.resetColor;
import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.utils.render.ColorUtils.interpolateColorC;
import static info.sigmaclient.sigma.utils.render.RenderUtils.startBlend;

public class GradientGlowing {

    private static final ShaderUtil gradientMaskShader = new ShaderUtil("gradientGlow");



    public static void drawGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        applyGradient(x,y,width,height,alpha,bottomLeft,topLeft,bottomRight,topRight,()->{
            RenderUtils.drawRect(x,y,x+width,y+height,-1);
        },5, 10);
    }

    public static void applyGradientCornerRL(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topRight, Runnable content) {
        Color mixedColor = interpolateColorC(topRight, bottomLeft, .5f);
        applyGradient(x, y, width, height, alpha, bottomLeft, mixedColor, mixedColor, topRight, content,5,10);
    }
    public static void applyGradientCornerRL(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topRight, float radius, float soft) {
        Color mixedColor = interpolateColorC(topRight, bottomLeft, .5f);
        applyGradient(x, y, width, height, alpha, bottomLeft, mixedColor, mixedColor, topRight, null,radius,soft);
    }
    public static void applyGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight, Runnable content, float radius, float soft) {
        resetColor();
        startBlend();
        gradientMaskShader.init();

        ScaledResolution sr = new ScaledResolution(mc);

        gradientMaskShader.setUniformf("location", x * sr.getScaleFactor(), (Minecraft.getInstance().getMainWindow().getHeight() - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        gradientMaskShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        gradientMaskShader.setUniformf("alpha", alpha);
        gradientMaskShader.setUniformf("radius", radius);
        gradientMaskShader.setUniformf("soft", soft);
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
//        content.run();
//        ShaderUtil.drawQuads();

        RenderUtils.drawRect(x-soft,y-soft,x+width+soft,y+height+soft,new Color(ColorUtils.reAlpha(bottomLeft,1).getRGB(), true).getRGB());

        gradientMaskShader.unload();
    }


}
