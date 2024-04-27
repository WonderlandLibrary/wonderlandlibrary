/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IGlStateManager;
import net.minecraft.client.renderer.GlStateManager;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\b\u0010\t\u001a\u00020\u0004H\u0016J\b\u0010\n\u001a\u00020\u0004H\u0016J\b\u0010\u000b\u001a\u00020\u0004H\u0016J\b\u0010\f\u001a\u00020\u0004H\u0016J\b\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u0004H\u0016J\b\u0010\u000f\u001a\u00020\u0004H\u0016J\b\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0011\u001a\u00020\u0004H\u0016J\b\u0010\u0012\u001a\u00020\u0004H\u0016J\b\u0010\u0013\u001a\u00020\u0004H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J(\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u0006H\u0016\u00a8\u0006\u001a"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/GlStateManagerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IGlStateManager;", "()V", "bindTexture", "", "textureID", "", "disableBlend", "disableCull", "disableLighting", "disableRescaleNormal", "disableTexture2D", "enableAlpha", "enableBlend", "enableColorMaterial", "enableTexture2D", "popAttrib", "popMatrix", "pushAttrib", "pushMatrix", "resetColor", "tryBlendFuncSeparate", "glSrcAlpha", "glOneMinusSrcAlpha", "glOne", "glZero", "LiKingSense"})
public final class GlStateManagerImpl
implements IGlStateManager {
    public static final GlStateManagerImpl INSTANCE;

    @Override
    public void bindTexture(int textureID) {
        GlStateManager.func_179144_i((int)textureID);
    }

    @Override
    public void resetColor() {
        GlStateManager.func_179117_G();
    }

    @Override
    public void enableTexture2D() {
        GlStateManager.func_179098_w();
    }

    @Override
    public void enableBlend() {
        GlStateManager.func_179147_l();
    }

    @Override
    public void tryBlendFuncSeparate(int glSrcAlpha, int glOneMinusSrcAlpha, int glOne, int glZero) {
        GlStateManager.func_179120_a((int)glSrcAlpha, (int)glOneMinusSrcAlpha, (int)glOne, (int)glZero);
    }

    @Override
    public void disableTexture2D() {
        GlStateManager.func_179090_x();
    }

    @Override
    public void disableBlend() {
        GlStateManager.func_179084_k();
    }

    @Override
    public void enableAlpha() {
        GlStateManager.func_179141_d();
    }

    @Override
    public void disableLighting() {
        GlStateManager.func_179140_f();
    }

    @Override
    public void disableCull() {
        GlStateManager.func_179129_p();
    }

    @Override
    public void enableColorMaterial() {
        GlStateManager.func_179142_g();
    }

    @Override
    public void disableRescaleNormal() {
        GlStateManager.func_179101_C();
    }

    @Override
    public void pushMatrix() {
        GlStateManager.func_179094_E();
    }

    @Override
    public void pushAttrib() {
        GlStateManager.func_179123_a();
    }

    @Override
    public void popMatrix() {
        GlStateManager.func_179121_F();
    }

    @Override
    public void popAttrib() {
        GlStateManager.func_179099_b();
    }

    private GlStateManagerImpl() {
    }

    static {
        GlStateManagerImpl glStateManagerImpl;
        INSTANCE = glStateManagerImpl = new GlStateManagerImpl();
    }
}

