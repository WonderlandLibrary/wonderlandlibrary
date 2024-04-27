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
    public static final RainbowFontShader INSTANCE;
    private static float strengthX;
    private static boolean isInUse;
    private static float strengthY;
    private static float offset;

    public final boolean isInUse() {
        return isInUse;
    }

    public final void setStrengthX(float f) {
        strengthX = f;
    }

    public final void setOffset(float f) {
        offset = f;
    }

    static {
        RainbowFontShader rainbowFontShader;
        INSTANCE = rainbowFontShader = new RainbowFontShader();
    }

    @Override
    public void startShader() {
        super.startShader();
        isInUse = true;
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f((int)this.getUniform("strength"), (float)strengthX, (float)strengthY);
        GL20.glUniform1f((int)this.getUniform("offset"), (float)offset);
    }

    public final float getOffset() {
        return offset;
    }

    public final void setStrengthY(float f) {
        strengthY = f;
    }

    public final float getStrengthX() {
        return strengthX;
    }

    public final float getStrengthY() {
        return strengthY;
    }

    @Override
    public void close() {
        if (isInUse) {
            this.stopShader();
        }
    }

    @JvmStatic
    public static final RainbowFontShader begin(boolean bl, float f, float f2, float f3) {
        boolean bl2 = false;
        if (bl) {
            INSTANCE.setStrengthX(f);
            INSTANCE.setStrengthY(f2);
            INSTANCE.setOffset(f3);
            INSTANCE.startShader();
        }
        return INSTANCE;
    }

    private RainbowFontShader() {
        super("rainbow_font_shader.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("offset");
        this.setupUniform("strength");
    }

    @Override
    public void stopShader() {
        super.stopShader();
        isInUse = false;
    }
}

