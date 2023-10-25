package org.dreamcore.client.ui.shader.shaders;

import org.lwjgl.opengl.GL20;
import org.dreamcore.client.ui.shader.FramebufferShader;

public class GlowShader extends FramebufferShader {

    public static final GlowShader GLOW_SHADER = new GlowShader();

    public GlowShader() {
        super("glow.frag");
    }

    @Override
    public void setupUniforms() {
        setupUniform("texture");
        setupUniform("texelSize");
        setupUniform("color");
        setupUniform("radius");
        setupUniform("direction");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1i(getUniform("texture"), 0);
        GL20.glUniform2f(getUniform("texelSize"), 1F / mc.displayWidth * (radius * quality), 1F / mc.displayHeight * (radius * quality));
        GL20.glUniform3f(getUniform("color"), red, green, blue);
        GL20.glUniform1f(getUniform("radius"), radius);
        GL20.glUniform2f(getUniform("direction"), 1, 1);
    }
}
