/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL20
 */
package ui.shader.shaders;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;
import ui.shader.Shader;

public final class BackgroundShader
extends Shader {
    public static final BackgroundShader BACKGROUND_SHADER = new BackgroundShader();
    public float time;

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
        int resolutionID = this.getUniform("iResolution");
        if (resolutionID > -1) {
            GL20.glUniform2f((int)resolutionID, (float)Display.getWidth(), (float)Display.getHeight());
        }
        if ((timeID = this.getUniform("iTime")) > -1) {
            GL20.glUniform1f((int)timeID, (float)this.time);
        }
        this.time += 3.0E-4f * (float)RenderUtils.deltaTime;
    }
}

