/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.shader.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.helpers.render.AnimationHelper;
import org.celestial.client.helpers.render.shader.FramebufferShader;
import org.lwjgl.opengl.GL20;

public class StarsShader
extends FramebufferShader {
    public static StarsShader STAR_SHADER = new StarsShader();
    public float time;

    public StarsShader() {
        super("star.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f(this.getUniform("resolution"), new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight());
        GL20.glUniform1f(this.getUniform("time"), this.time);
        this.time = (float)((double)this.time + 0.1 * (double)AnimationHelper.delta);
    }
}

