/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import java.io.Closeable;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL20;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0015H\u0016J\b\u0010\u0017\u001a\u00020\u0015H\u0016J\b\u0010\u0018\u001a\u00020\u0015H\u0016J\b\u0010\u0019\u001a\u00020\u0015H\u0016R\u001e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\rR\u001a\u0010\u0011\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\r\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/shader/shaders/RainbowFontShader;", "Lnet/ccbluex/liquidbounce/utils/render/shader/Shader;", "Ljava/io/Closeable;", "()V", "<set-?>", "", "isInUse", "()Z", "offset", "", "getOffset", "()F", "setOffset", "(F)V", "strengthX", "getStrengthX", "setStrengthX", "strengthY", "getStrengthY", "setStrengthY", "close", "", "setupUniforms", "startShader", "stopShader", "updateUniforms", "Companion", "KyinoClient"})
public final class RainbowFontShader
extends Shader
implements Closeable {
    private boolean isInUse;
    private float strengthX;
    private float strengthY;
    private float offset;
    @JvmField
    @NotNull
    public static final RainbowFontShader INSTANCE;
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

    public RainbowFontShader() {
        super("rainbow_font_shader.frag");
    }

    static {
        Companion = new Companion(null);
        INSTANCE = new RainbowFontShader();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J)\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0086\bR\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/shader/shaders/RainbowFontShader$Companion;", "", "()V", "INSTANCE", "Lnet/ccbluex/liquidbounce/utils/render/shader/shaders/RainbowFontShader;", "begin", "enable", "", "x", "", "y", "offset", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final RainbowFontShader begin(boolean enable, float x, float y, float offset) {
            int $i$f$begin = 0;
            RainbowFontShader instance = INSTANCE;
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

