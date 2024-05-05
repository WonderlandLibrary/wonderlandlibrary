/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.partcles;

import java.util.ArrayList;
import java.util.Random;
import me.AveReborn.ui.partcles.Particle;
import me.AveReborn.ui.partcles.ParticleSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.util.vector.Vector2f;

public class ParticleManager {
    private Particle particle;
    private int amount;
    public ArrayList<Particle> particles = new ArrayList();
    private Random random = new Random();

    public ParticleManager(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
        this.init();
    }

    private void init() {
        this.particles.clear();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int i2 = 0;
        while (i2 < this.amount) {
            ParticleSnow particle = new ParticleSnow();
            if (particle instanceof ParticleSnow) {
                particle = new ParticleSnow();
            }
            particle.vector.x = this.random.nextInt(res.getScaledWidth() + 1);
            particle.vector.y = this.random.nextInt(res.getScaledHeight() + 1);
            this.particles.add(particle);
            ++i2;
        }
    }

    public void draw(int xAdd) {
        for (Particle particle : this.particles) {
            particle.draw(xAdd);
        }
    }
}

