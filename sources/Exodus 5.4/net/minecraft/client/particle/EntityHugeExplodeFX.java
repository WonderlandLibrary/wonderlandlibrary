/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityHugeExplodeFX
extends EntityFX {
    private int maximumTime = 8;
    private int timeSinceStart;

    @Override
    public void onUpdate() {
        int n = 0;
        while (n < 6) {
            double d = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            double d2 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            double d3 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d, d2, d3, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0, 0.0, new int[0]);
            ++n;
        }
        ++this.timeSinceStart;
        if (this.timeSinceStart == this.maximumTime) {
            this.setDead();
        }
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
    }

    protected EntityHugeExplodeFX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityHugeExplodeFX(world, d, d2, d3, d4, d5, d6);
        }
    }
}

