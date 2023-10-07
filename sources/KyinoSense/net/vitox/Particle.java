/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.vitox;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vitox.particle.util.RenderUtils;

@SideOnly(value=Side.CLIENT)
class Particle {
    public float x;
    public float y;
    public final float size;
    private final float ySpeed = new Random().nextInt(5);
    private final float xSpeed = new Random().nextInt(5);
    private int height;
    private int width;

    Particle(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = this.genRandom();
    }

    private float lint1(float f) {
        return 1.02f * (1.0f - f) + 1.0f * f;
    }

    private float lint2(float f) {
        return 1.02f + f * -0.01999998f;
    }

    void connect(float x, float y) {
        RenderUtils.connectPoints(this.getX(), this.getY(), x, y);
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    void interpolation() {
        for (int n = 0; n <= 64; ++n) {
            float p2;
            float f = (float)n / 64.0f;
            float p1 = this.lint1(f);
            if (p1 == (p2 = this.lint2(f))) continue;
            this.y -= f;
            this.x -= f;
        }
    }

    void fall() {
        Minecraft mc = Minecraft.func_71410_x();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        this.y += this.ySpeed;
        this.x += this.xSpeed;
        if (this.y > (float)mc.field_71440_d) {
            this.y = 1.0f;
        }
        if (this.x > (float)mc.field_71443_c) {
            this.x = 1.0f;
        }
        if (this.x < 1.0f) {
            this.x = scaledResolution.func_78326_a();
        }
        if (this.y < 1.0f) {
            this.y = scaledResolution.func_78328_b();
        }
    }

    private float genRandom() {
        return (float)((double)0.3f + Math.random() * (double)1.3f);
    }
}

