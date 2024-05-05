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
    private boolean isInUse;
    private float strengthX;
    private float strengthY;
    private float offset;
    @JvmField
    public static final RainbowShader INSTANCE;
    public static final Companion Companion;

    public final boolean isInUse() {
        return this.isInUse;
    }

    public final float getStrengthX() {
        return this.strengthX;
    }

    public final void setStrengthX(float f) {
        this.strengthX = f;
    }

    public final float getStrengthY() {
        return this.strengthY;
    }

    public final void setStrengthY(float f) {
        this.strengthY = f;
    }

    public final float getOffset() {
        return this.offset;
    }

    public final void setOffset(float f) {
        this.offset = f;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("offset");
        this.setupUniform("strength");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f((int)this.getUniform("strength"), (float)this.strengthX, (float)this.strengthY);
        GL20.glUniform1f((int)this.getUniform("offset"), (float)this.offset);
    }

    @Override
    public void startShader() {
        super.startShader();
        this.isInUse = true;
    }

    @Override
    public void stopShader() {
        super.stopShader();
        this.isInUse = false;
    }

    @Override
    public void close() {
        if (this.isInUse) {
            this.stopShader();
        }
    }

    public RainbowShader() {
        super("rainbow_shader.frag");
    }

    static {
        Companion = new Companion(null);
        INSTANCE = new RainbowShader();
    }

    public static final class Companion {
        public final RainbowShader begin(boolean enable, float x, float y, float offset) {
            int $i$f$begin = 0;
            RainbowShader instance = INSTANCE;
            if (enable) {
                instance.setStrengthX(x);
                instance.setStrengthY(y);
                instance.setOffset(offset);
                instance.startShader();
            }
            return instance;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

