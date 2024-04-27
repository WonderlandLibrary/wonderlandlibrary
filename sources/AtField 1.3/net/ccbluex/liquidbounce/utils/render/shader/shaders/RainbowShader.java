/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import java.io.Closeable;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import org.lwjgl.opengl.GL20;

public final class RainbowShader
extends Shader
implements Closeable {
    @JvmField
    public static final RainbowShader INSTANCE;
    private float strengthY;
    public static final Companion Companion;
    private float offset;
    private float strengthX;
    private boolean isInUse;

    public RainbowShader() {
        super("rainbow_shader.frag");
    }

    @Override
    public void startShader() {
        super.startShader();
        this.isInUse = true;
    }

    public final void setOffset(float f) {
        this.offset = f;
    }

    public final boolean isInUse() {
        return this.isInUse;
    }

    @Override
    public void stopShader() {
        super.stopShader();
        this.isInUse = false;
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f((int)this.getUniform("strength"), (float)this.strengthX, (float)this.strengthY);
        GL20.glUniform1f((int)this.getUniform("offset"), (float)this.offset);
    }

    public final void setStrengthY(float f) {
        this.strengthY = f;
    }

    public final float getOffset() {
        return this.offset;
    }

    public final float getStrengthX() {
        return this.strengthX;
    }

    public final void setStrengthX(float f) {
        this.strengthX = f;
    }

    @Override
    public void close() {
        if (this.isInUse) {
            this.stopShader();
        }
    }

    static {
        Companion = new Companion(null);
        INSTANCE = new RainbowShader();
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("offset");
        this.setupUniform("strength");
    }

    public final float getStrengthY() {
        return this.strengthY;
    }

    public static final class Companion {
        private Companion() {
        }

        public final RainbowShader begin(boolean bl, float f, float f2, float f3) {
            boolean bl2 = false;
            RainbowShader rainbowShader = INSTANCE;
            if (bl) {
                rainbowShader.setStrengthX(f);
                rainbowShader.setStrengthY(f2);
                rainbowShader.setOffset(f3);
                rainbowShader.startShader();
            }
            return rainbowShader;
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

