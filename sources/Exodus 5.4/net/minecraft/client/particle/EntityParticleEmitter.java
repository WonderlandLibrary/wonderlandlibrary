/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityParticleEmitter
extends EntityFX {
    private EnumParticleTypes particleTypes;
    private int lifetime;
    private Entity attachedEntity;
    private int age;

    @Override
    public void onUpdate() {
        int n = 0;
        while (n < 16) {
            double d;
            double d2;
            double d3 = this.rand.nextFloat() * 2.0f - 1.0f;
            if (d3 * d3 + (d2 = (double)(this.rand.nextFloat() * 2.0f - 1.0f)) * d2 + (d = (double)(this.rand.nextFloat() * 2.0f - 1.0f)) * d <= 1.0) {
                double d4 = this.attachedEntity.posX + d3 * (double)this.attachedEntity.width / 4.0;
                double d5 = this.attachedEntity.getEntityBoundingBox().minY + (double)(this.attachedEntity.height / 2.0f) + d2 * (double)this.attachedEntity.height / 4.0;
                double d6 = this.attachedEntity.posZ + d * (double)this.attachedEntity.width / 4.0;
                this.worldObj.spawnParticle(this.particleTypes, false, d4, d5, d6, d3, d2 + 0.2, d, new int[0]);
            }
            ++n;
        }
        ++this.age;
        if (this.age >= this.lifetime) {
            this.setDead();
        }
    }

    public EntityParticleEmitter(World world, Entity entity, EnumParticleTypes enumParticleTypes) {
        super(world, entity.posX, entity.getEntityBoundingBox().minY + (double)(entity.height / 2.0f), entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
        this.attachedEntity = entity;
        this.lifetime = 3;
        this.particleTypes = enumParticleTypes;
        this.onUpdate();
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
    }

    @Override
    public int getFXLayer() {
        return 3;
    }
}

