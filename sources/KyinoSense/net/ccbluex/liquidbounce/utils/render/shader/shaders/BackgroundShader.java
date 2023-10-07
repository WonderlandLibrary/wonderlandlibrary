/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public final class BackgroundShader
extends Shader {
    public static final BackgroundShader BACKGROUND_SHADER = new BackgroundShader();
    private float time;

    public BackgroundShader() {
        super("background.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("iResolution");
        this.setupUniform("iTime");
    }

    @Override
    public void updateUniforms() {
        int timeID;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int resolutionID = this.getUniform("iResolution");
        if (resolutionID > -1) {
            GL20.glUniform2f((int)resolutionID, (float)((float)scaledResolution.func_78326_a() * 2.0f), (float)((float)scaledResolution.func_78328_b() * 2.0f));
        }
        if ((timeID = this.getUniform("iTime")) > -1) {
            GL20.glUniform1f((int)timeID, (float)this.time);
        }
        this.time += 0.005f * (float)RenderUtils.deltaTime;
    }
}

