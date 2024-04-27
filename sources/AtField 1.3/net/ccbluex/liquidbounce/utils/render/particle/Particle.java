/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils.render.particle;

import java.util.Random;
import net.ccbluex.liquidbounce.utils.render.particle.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
class Particle {
    public float y;
    private final float xSpeed;
    public float x;
    private int width;
    private int height;
    private final float ySpeed = new Random().nextInt(5);
    public final float size;

    Particle(int n, int n2) {
        this.xSpeed = new Random().nextInt(5);
        this.x = n;
        this.y = n2;
        this.size = this.genRandom();
    }

    public float getY() {
        return this.y;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    private float genRandom() {
        return (float)((double)0.3f + Math.random() * (double)1.3f);
    }

    void interpolation() {
        for (int i = 0; i <= 64; ++i) {
            float f;
            float f2 = (float)i / 64.0f;
            float f3 = this.lint1(f2);
            if (f3 == (f = this.lint2(f2))) continue;
            this.y -= f2;
            this.x -= f2;
        }
    }

    public int getHeight() {
        return this.height;
    }

    private float lint1(float f) {
        return 1.02f * (1.0f - f) + 1.0f * f;
    }

    void connect(float f, float f2) {
        RenderUtils.connectPoints(this.getX(), this.getY(), f, f2);
    }

    public void setX(int n) {
        this.x = n;
    }

    void fall() {
        Minecraft minecraft = Minecraft.func_71410_x();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        this.y += this.ySpeed;
        this.x += this.xSpeed;
        if (this.y > (float)minecraft.field_71440_d) {
            this.y = 1.0f;
        }
        if (this.x > (float)minecraft.field_71443_c) {
            this.x = 1.0f;
        }
        if (this.x < 1.0f) {
            this.x = scaledResolution.func_78326_a();
        }
        if (this.y < 1.0f) {
            this.y = scaledResolution.func_78328_b();
        }
    }

    public float getX() {
        return this.x;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public void setY(int n) {
        this.y = n;
    }

    public int getWidth() {
        return this.width;
    }

    private float lint2(float f) {
        return 1.02f + f * -0.01999998f;
    }
}

