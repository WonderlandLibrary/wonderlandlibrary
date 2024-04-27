/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import java.io.Closeable;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL20;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J)\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\tH\u0087\bJ\b\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u0019H\u0016J\b\u0010\u001b\u001a\u00020\u0019H\u0016J\b\u0010\u001c\u001a\u00020\u0019H\u0016J\b\u0010\u001d\u001a\u00020\u0019H\u0016R\u001e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\rR\u001a\u0010\u0011\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\r\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/shader/shaders/RainbowFontShader;", "Lnet/ccbluex/liquidbounce/utils/render/shader/Shader;", "Ljava/io/Closeable;", "()V", "<set-?>", "", "isInUse", "()Z", "offset", "", "getOffset", "()F", "setOffset", "(F)V", "strengthX", "getStrengthX", "setStrengthX", "strengthY", "getStrengthY", "setStrengthY", "begin", "enable", "x", "y", "close", "", "setupUniforms", "startShader", "stopShader", "updateUniforms", "LiKingSense"})
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
    @NotNull
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

