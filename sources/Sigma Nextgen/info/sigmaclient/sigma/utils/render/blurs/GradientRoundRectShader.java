package info.sigmaclient.sigma.utils.render.blurs;

import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.ShaderUtil;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.client.Minecraft;

import java.awt.*;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.utils.render.RenderUtils.startBlend;
import static info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.resetColor;

public class GradientRoundRectShader {

    private static final ShaderUtil gradientMaskShader = new ShaderUtil("roundRect2");



    public static void drawRoundRect(float x, float y, float width, float height, float radius, Color bottomLeft, Color bottomLeft2) {
        applyGradient(x,y,width,height,radius,bottomLeft,bottomLeft2);
    }

    public static void applyGradient(float x, float y, float width, float height, float alpha, Color bottomLeft,Color color2) {
        resetColor();
        startBlend();
        gradientMaskShader.init();

        ScaledResolution sr = new ScaledResolution(mc);

        gradientMaskShader.setUniformf("location", x * sr.getScaleFactor(), (Minecraft.getInstance().getMainWindow().getHeight() - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        gradientMaskShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        gradientMaskShader.setUniformf("alpha", bottomLeft.getAlpha() / 255f);
        gradientMaskShader.setUniformi("tex", 0);
        gradientMaskShader.setUniformf("radius2", alpha);
        // Bottom Left
        gradientMaskShader.setUniformf("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f);
        gradientMaskShader.setUniformf("color2", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f);

        gradientMaskShader.setUniformf("color3", color2.getRed() / 255f, color2.getGreen() / 255f, color2.getBlue() / 255f);
        gradientMaskShader.setUniformf("color4", color2.getRed() / 255f, color2.getGreen() / 255f, color2.getBlue() / 255f);

        //Apply the gradient to whatever is put here
        RenderUtils.drawRect(x-3,y-3,x+width+3,y+height+3,new Color(ColorUtils.reAlpha(bottomLeft,1).getRGB(), true).getRGB());

        gradientMaskShader.unload();
    }


}
