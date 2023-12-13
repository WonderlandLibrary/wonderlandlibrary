// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;

public class EntityParticleEmitter extends EntityFX
{
    private Entity field_174851_a;
    private int field_174852_ax;
    private int field_174850_ay;
    private EnumParticleTypes field_174849_az;
    private static final String __OBFID = "CL_00002574";
    
    public EntityParticleEmitter(final World worldIn, final Entity p_i46279_2_, final EnumParticleTypes p_i46279_3_) {
        super(worldIn, p_i46279_2_.posX, p_i46279_2_.getEntityBoundingBox().minY + p_i46279_2_.height / 2.0f, p_i46279_2_.posZ, p_i46279_2_.motionX, p_i46279_2_.motionY, p_i46279_2_.motionZ);
        this.field_174851_a = p_i46279_2_;
        this.field_174850_ay = 3;
        this.field_174849_az = p_i46279_3_;
        this.onUpdate();
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
    }
    
    @Override
    public void onUpdate() {
        for (int var1 = 0; var1 < 16; ++var1) {
            final double var2 = this.rand.nextFloat() * 2.0f - 1.0f;
            final double var3 = this.rand.nextFloat() * 2.0f - 1.0f;
            final double var4 = this.rand.nextFloat() * 2.0f - 1.0f;
            if (var2 * var2 + var3 * var3 + var4 * var4 <= 1.0) {
                final double var5 = this.field_174851_a.posX + var2 * this.field_174851_a.width / 4.0;
                final double var6 = this.field_174851_a.getEntityBoundingBox().minY + this.field_174851_a.height / 2.0f + var3 * this.field_174851_a.height / 4.0;
                final double var7 = this.field_174851_a.posZ + var4 * this.field_174851_a.width / 4.0;
                this.worldObj.spawnParticle(this.field_174849_az, false, var5, var6, var7, var2, var3 + 0.2, var4, new int[0]);
            }
        }
        ++this.field_174852_ax;
        if (this.field_174852_ax >= this.field_174850_ay) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
}
