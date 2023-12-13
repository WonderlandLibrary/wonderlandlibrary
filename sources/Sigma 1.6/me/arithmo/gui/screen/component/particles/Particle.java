/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.screen.component.particles;

import java.util.Random;
import me.arithmo.gui.screen.component.particles.ParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Particle {
    private float posX;
    private float posY;
    private float alpha;
    private float size;
    private float speed;
    public Minecraft mc = Minecraft.getMinecraft();

    public Particle(float posX, float posY, float size, float speed, float alpha) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setSize(size);
        this.setSpeed(speed);
        this.setAlpha(alpha);
    }

    public int random(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public ScaledResolution getRes() {
        return new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    }

    public void render(ParticleManager p) {
        if (this.getAlpha() - 1.0f >= 0.0f) {
            this.setAlpha(this.getAlpha() - 1.0f);
        }
    }

    public float getPosX() {
        return this.posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

