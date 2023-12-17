// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;

public class ParticleEmitter extends Particle
{
    private final Entity attachedEntity;
    private int age;
    private final int lifetime;
    private final EnumParticleTypes particleTypes;
    
    public ParticleEmitter(final World worldIn, final Entity p_i46279_2_, final EnumParticleTypes particleTypesIn) {
        this(worldIn, p_i46279_2_, particleTypesIn, 3);
    }
    
    public ParticleEmitter(final World p_i47219_1_, final Entity p_i47219_2_, final EnumParticleTypes p_i47219_3_, final int p_i47219_4_) {
        super(p_i47219_1_, p_i47219_2_.posX, p_i47219_2_.getEntityBoundingBox().minY + p_i47219_2_.height / 2.0f, p_i47219_2_.posZ, p_i47219_2_.motionX, p_i47219_2_.motionY, p_i47219_2_.motionZ);
        this.attachedEntity = p_i47219_2_;
        this.lifetime = p_i47219_4_;
        this.particleTypes = p_i47219_3_;
        this.onUpdate();
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
    }
    
    @Override
    public void onUpdate() {
        for (int i = 0; i < 16; ++i) {
            final double d0 = this.rand.nextFloat() * 2.0f - 1.0f;
            final double d2 = this.rand.nextFloat() * 2.0f - 1.0f;
            final double d3 = this.rand.nextFloat() * 2.0f - 1.0f;
            if (d0 * d0 + d2 * d2 + d3 * d3 <= 1.0) {
                final double d4 = this.attachedEntity.posX + d0 * this.attachedEntity.width / 4.0;
                final double d5 = this.attachedEntity.getEntityBoundingBox().minY + this.attachedEntity.height / 2.0f + d2 * this.attachedEntity.height / 4.0;
                final double d6 = this.attachedEntity.posZ + d3 * this.attachedEntity.width / 4.0;
                this.world.spawnParticle(this.particleTypes, false, d4, d5, d6, d0, d2 + 0.2, d3, new int[0]);
            }
        }
        ++this.age;
        if (this.age >= this.lifetime) {
            this.setExpired();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
}
