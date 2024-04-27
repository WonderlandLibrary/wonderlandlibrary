/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IGlStateManager;
import net.minecraft.client.renderer.GlStateManager;

public final class GlStateManagerImpl
implements IGlStateManager {
    public static final GlStateManagerImpl INSTANCE;

    @Override
    public void bindTexture(int n) {
        GlStateManager.func_179144_i((int)n);
    }

    @Override
    public void pushMatrix() {
        GlStateManager.func_179094_E();
    }

    @Override
    public void enableColorMaterial() {
        GlStateManager.func_179142_g();
    }

    static {
        GlStateManagerImpl glStateManagerImpl;
        INSTANCE = glStateManagerImpl = new GlStateManagerImpl();
    }

    private GlStateManagerImpl() {
    }

    @Override
    public void disableRescaleNormal() {
        GlStateManager.func_179101_C();
    }

    @Override
    public void disableBlend() {
        GlStateManager.func_179084_k();
    }

    @Override
    public void popAttrib() {
        GlStateManager.func_179099_b();
    }

    @Override
    public void enableBlend() {
        GlStateManager.func_179147_l();
    }

    @Override
    public void pushAttrib() {
        GlStateManager.func_179123_a();
    }

    @Override
    public void enableTexture2D() {
        GlStateManager.func_179098_w();
    }

    @Override
    public void disableCull() {
        GlStateManager.func_179129_p();
    }

    @Override
    public void disableLighting() {
        GlStateManager.func_179140_f();
    }

    @Override
    public void popMatrix() {
        GlStateManager.func_179121_F();
    }

    @Override
    public void disableTexture2D() {
        GlStateManager.func_179090_x();
    }

    @Override
    public void enableAlpha() {
        GlStateManager.func_179141_d();
    }

    @Override
    public void resetColor() {
        GlStateManager.func_179117_G();
    }

    @Override
    public void tryBlendFuncSeparate(int n, int n2, int n3, int n4) {
        GlStateManager.func_179120_a((int)n, (int)n2, (int)n3, (int)n4);
    }
}

