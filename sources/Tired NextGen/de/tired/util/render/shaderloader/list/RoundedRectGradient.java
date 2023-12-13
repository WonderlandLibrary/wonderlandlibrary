package de.tired.util.render.shaderloader.list;

import de.tired.util.render.shaderloader.ShaderAnnoation;
import de.tired.util.render.shaderloader.ShaderRenderType;
import de.tired.util.render.shaderloader.ShaderExtension;
import de.tired.util.render.shaderloader.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ShaderAnnoation(fragName = "RoundedGradient.glsl", renderType = ShaderRenderType.RENDER2D)
public class RoundedRectGradient extends ShaderProgram {

    public void drawRound(float x, float y, float width, float height, float radius, Color top, Color bottom) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ShaderExtension.useShader(getShaderProgramID());
        setupRoundedRectUniforms(x, y, width, height, radius);
        // Bottom Left
        setShaderUniform("color1", bottom.getRed() / 255f, bottom.getGreen() / 255f, bottom.getBlue() / 255f, bottom.getAlpha() / 255f);
        //Top left
        setShaderUniform("color2", top.getRed() / 255f, top.getGreen() / 255f, top.getBlue() / 255f, top.getAlpha() / 255f);
        //Bottom Right
        setShaderUniform("color3", bottom.getRed() / 255f, bottom.getGreen() / 255f, bottom.getBlue() / 255f, bottom.getAlpha() / 255f);
        //Top Right
        setShaderUniform("color4", top.getRed() / 255f, top.getGreen() / 255f, top.getBlue() / 255f, top.getAlpha() / 255f);
        drawTexture(x - 1, y - 1, width + 2, height + 2);
        ShaderExtension.deleteProgram();
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

    public void drawRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {

        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ShaderExtension.useShader(getShaderProgramID());
        setupRoundedRectUniforms(x, y, width, height, radius);
        // Bottom Left
        setShaderUniform("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f, bottomLeft.getAlpha() / 255f);
        //Top left
        setShaderUniform("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f, topLeft.getAlpha() / 255f);
        //Bottom Right
        setShaderUniform("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f, bottomRight.getAlpha() / 255f);
        //Top Right
        setShaderUniform("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f, topRight.getAlpha() / 255f);
        drawTexture(x - 1, y - 1, width + 2, height + 2);
        ShaderExtension.deleteProgram();
        GlStateManager.disableBlend();

    }


    private void setupRoundedRectUniforms(float x, float y, float width, float height, float radius) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        setUniformf("location", x * sr.getScaleFactor(), (MC.displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        setUniformf("radius", radius * sr.getScaleFactor());
    }
}