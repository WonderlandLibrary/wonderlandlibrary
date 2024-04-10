package info.sigmaclient.sigma.utils.render.blurs;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.utils.render.ShaderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

import static info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.resetColor;
import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.utils.render.RenderUtils.startBlend;

public class RoundRectShader {

    private static final ShaderUtil gradientMaskShader = new ShaderUtil("roundRect");



    public static void drawRoundRect(float x, float y, float width, float height, float radius, Color bottomLeft) {
        applyGradient(x,y,width,height,radius,bottomLeft);
    }

    public static void applyGradient(float x, float y, float width, float height, float alpha, Color bottomLeft) {
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

        //Apply the gradient to whatever is put here
        RenderUtils.drawRect(x-2,y-2,x+width+2,y+height+2,new Color(ColorUtils.reAlpha(bottomLeft,bottomLeft.getAlpha()).getRGB(), true).getRGB());

        gradientMaskShader.unload();
    }


}
