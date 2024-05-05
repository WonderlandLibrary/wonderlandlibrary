/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 */
package jx.utils;

import jx.utils.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ScaleUtils {
    private int scale;

    public ScaleUtils(int scale) {
        this.scale = scale;
    }

    public void pushScale() {
        ScaledResolution rs = new ScaledResolution(Minecraft.func_71410_x());
        double scale = (double)rs.func_78325_e() / Math.pow(rs.func_78325_e(), 2.0);
        GlStateManager.func_179094_E();
        GlStateManager.func_179139_a((double)(scale * (double)this.scale), (double)(scale * (double)this.scale), (double)(scale * (double)this.scale));
    }

    public int calc(int value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.func_71410_x());
        return value * rs.func_78325_e() / this.scale;
    }

    public void popScale() {
        GlStateManager.func_179152_a((float)this.scale, (float)this.scale, (float)this.scale);
        GlStateManager.func_179121_F();
    }

    public Vec2i getMouse(int mouseX, int mouseY, ScaledResolution rs) {
        return new Vec2i(mouseX * rs.func_78325_e() / this.scale, mouseY * rs.func_78325_e() / this.scale);
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}

