/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public interface IParticleFactory {
    @Nullable
    public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int ... var15);
}

