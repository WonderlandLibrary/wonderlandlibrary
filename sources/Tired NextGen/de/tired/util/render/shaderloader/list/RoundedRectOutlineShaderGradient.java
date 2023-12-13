package de.tired.util.render.shaderloader.list;

import de.tired.base.interfaces.IHook;
import de.tired.util.render.shaderloader.ShaderAnnoation;
import de.tired.util.render.shaderloader.ShaderProgram;
import de.tired.util.render.shaderloader.ShaderRenderType;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

@ShaderAnnoation(fragName = "RoundedOutlineGradient.glsl", renderType = ShaderRenderType.RENDER2D)
public class RoundedRectOutlineShaderGradient extends ShaderProgram implements IHook {

    public void drawRound(float x, float y, float width, float height, float radius, float outlineThickness,  Color top, Color bottom) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glUseProgram(this.getShaderProgramID());
        setupRoundedRectUniforms(x, y, width, height, radius, outlineThickness);
        setShaderUniform("color1", bottom.getRed() / 255f, bottom.getGreen() / 255f, bottom.getBlue() / 255f, bottom.getAlpha() / 255f);
        //Top left
        setShaderUniform("color2", top.getRed() / 255f, top.getGreen() / 255f, top.getBlue() / 255f, top.getAlpha() / 255f);
        //Bottom Right
        setShaderUniform("color3", bottom.getRed() / 255f, bottom.getGreen() / 255f, bottom.getBlue() / 255f, bottom.getAlpha() / 255f);
        //Top Right
        setShaderUniform("color4", top.getRed() / 255f, top.getGreen() / 255f, top.getBlue() / 255f, top.getAlpha() / 255f);
        drawTexture(x - 1, y - 1, width + 2, height + 2);
        glUseProgram(0);
        GlStateManager.disableBlend();
    }

    public static void drawTexture(float x, float y, float width, float height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x, y);
        glTexCoord2f(0, 1);
        glVertex2f(x, y + height);
        glTexCoord2f(1, 1);
        glVertex2f(x + width, y + height);
        glTexCoord2f(1, 0);
        glVertex2f(x + width, y);
        glEnd();

    }


    private void setupRoundedRectUniforms(float x, float y, float width, float height, float radius,  float outlineThickness) {
        final ScaledResolution sr = new ScaledResolution(MC);
        setUniformf("location", x * sr.getScaleFactor(), (MC.displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        setUniformf("radius", radius * sr.getScaleFactor());
        setUniformf("outlineThickness", outlineThickness);
    }


}
