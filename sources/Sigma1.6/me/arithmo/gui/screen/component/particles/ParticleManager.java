/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.screen.component.particles;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.gui.screen.component.particles.BottomLeftParticle;
import me.arithmo.gui.screen.component.particles.BottomRightParticle;
import me.arithmo.gui.screen.component.particles.GravityParticle;
import me.arithmo.gui.screen.component.particles.Particle;
import me.arithmo.gui.screen.component.particles.TopLeftParticle;
import me.arithmo.gui.screen.component.particles.TopRightParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ParticleManager {
    private List<Particle> particles = new CopyOnWriteArrayList<Particle>();
    public Minecraft mc = Minecraft.getMinecraft();
    private boolean rightClicked;

    public ParticleManager() {
        this.particles.clear();
    }

    public void render(int x, int y) {
        if (this.particles.size() <= 10000) {
            for (int i = 0; i < 5; ++i) {
                int rand = this.random(0, 6);
                if (rand == 1) {
                    this.particles.add(new TopLeftParticle(this.centerWidth() + this.random(- this.getRes().getScaledWidth(), this.getRes().getScaledWidth()), this.centerHeight() + this.random(- this.getRes().getScaledHeight(), this.getRes().getScaledHeight()), this.random(1, 2), this.random(1, 2), this.random(40, 200)));
                }
                if (rand == 2) {
                    this.particles.add(new GravityParticle(this.centerWidth() + this.random(- this.getRes().getScaledWidth(), this.getRes().getScaledWidth()), this.centerHeight() + this.random(- this.getRes().getScaledHeight(), this.getRes().getScaledHeight()), this.random(1, 3), this.random(1, 2), this.random(40, 220)));
                }
                if (rand == 3) {
                    this.particles.add(new TopRightParticle(this.centerWidth() + this.random(- this.getRes().getScaledWidth(), this.getRes().getScaledWidth()), this.centerHeight() + this.random(- this.getRes().getScaledHeight(), this.getRes().getScaledHeight()), this.random(1, 3), this.random(1, 2), this.random(40, 220)));
                }
                if (rand == 4) {
                    this.particles.add(new BottomLeftParticle(this.centerWidth() + this.random(- this.getRes().getScaledWidth(), this.getRes().getScaledWidth()), this.centerHeight() + this.random(- this.getRes().getScaledHeight(), this.getRes().getScaledHeight()), this.random(1, 3), this.random(1, 2), this.random(40, 220)));
                }
                if (rand != 5) continue;
                this.particles.add(new BottomRightParticle(this.centerWidth() + this.random(- this.getRes().getScaledWidth(), this.getRes().getScaledWidth()), this.centerHeight() + this.random(- this.getRes().getScaledHeight(), this.getRes().getScaledHeight()), this.random(1, 3), this.random(1, 2), this.random(40, 220)));
            }
        }
        for (Particle p : this.particles) {
            if (p.getAlpha() <= 0.0f) {
                this.particles.remove(p);
            }
            p.render(this);
        }
    }

    public int random(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public ScaledResolution getRes() {
        return new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    }

    public int centerWidth() {
        return this.getRes().getScaledWidth() / 2;
    }

    public int centerHeight() {
        return this.getRes().getScaledHeight() / 2;
    }

    public CopyOnWriteArrayList<Particle> getParticles() {
        return (CopyOnWriteArrayList)this.particles;
    }

    public void setParticles(CopyOnWriteArrayList<Particle> particles) {
        this.particles = particles;
    }
}

