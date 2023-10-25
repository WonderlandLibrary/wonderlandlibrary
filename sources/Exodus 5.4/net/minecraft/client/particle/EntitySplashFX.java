/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public class EntitySplashFX
extends EntityRainFX {
    protected EntitySplashFX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3);
        this.particleGravity = 0.04f;
        this.nextTextureIndexX();
        if (d5 == 0.0 && (d4 != 0.0 || d6 != 0.0)) {
            this.motionX = d4;
            this.motionY = d5 + 0.1;
            this.motionZ = d6;
        }
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntitySplashFX(world, d, d2, d3, d4, d5, d6);
        }
    }
}

