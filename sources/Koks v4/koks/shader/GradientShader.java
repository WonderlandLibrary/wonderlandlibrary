package koks.shader;

import koks.api.shader.Shader;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;

import java.awt.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Shader.Info(vertex = "vertex", fragment = "gradient")
public class GradientShader extends Shader {

    public void setAttributes(Color startColor, Color endColor, float width, float height) {
        GL20.glUniform2f(getUniform("resolution"), width, height);
        GL20.glUniform3f(getUniform("startColor"), startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F);
        GL20.glUniform3f(getUniform("endColor"), endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F);
    }

}
