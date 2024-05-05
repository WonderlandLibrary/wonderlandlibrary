/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import java.io.Closeable;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import org.lwjgl.opengl.GL20;

public final class RainbowFontShader
extends Shader
implements Closeable {
    private static boolean isInUse;
    private static float strengthX;
    private static float strengthY;
    private static float offset;
    public static final RainbowFontShader INSTANCE;

    public final boolean isInUse() {
        return isInUse;
    }

    public final float getStrengthX() {
        return strengthX;
    }

    public final void setStrengthX(float f) {
        strengthX = f;
    }

    public final float getStrengthY() {
        return strengthY;
    }

    public final void setStrengthY(float f) {
        strengthY = f;
    }

    public final float getOffset() {
        return offset;
    }

    public final void setOffset(float f) {
        offset = f;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("offset");
        this.setupUniform("strength");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f((int)this.getUniform("strength"), (float)strengthX, (float)strengthY);
        GL20.glUniform1f((int)this.getUniform("offset"), (float)offset);
    }

    @Override
    public void startShader() {
        super.startShader();
        isInUse = true;
    }

    @Override
    public void stopShader() {
        super.stopShader();
        isInUse = false;
    }

    @Override
    public void close() {
        if (isInUse) {
            this.stopShader();
        }
    }

    @JvmStatic
    public static final RainbowFontShader begin(boolean enable, float x, float y, float offset) {
        int $i$f$begin = 0;
        if (enable) {
            INSTANCE.setStrengthX(x);
            INSTANCE.setStrengthY(y);
            INSTANCE.setOffset(offset);
            INSTANCE.startShader();
        }
        return INSTANCE;
    }

    private RainbowFontShader() {
        super("rainbow_font_shader.frag");
    }

    static {
        RainbowFontShader rainbowFontShader;
        INSTANCE = rainbowFontShader = new RainbowFontShader();
    }
}

