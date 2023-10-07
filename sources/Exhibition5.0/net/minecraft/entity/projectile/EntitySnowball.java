// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySnowball extends EntityThrowable
{
    private static final String __OBFID = "CL_00001722";
    
    public EntitySnowball(final World worldIn) {
        super(worldIn);
    }
    
    public EntitySnowball(final World worldIn, final EntityLivingBase p_i1774_2_) {
        super(worldIn, p_i1774_2_);
    }
    
    public EntitySnowball(final World worldIn, final double p_i1775_2_, final double p_i1775_4_, final double p_i1775_6_) {
        super(worldIn, p_i1775_2_, p_i1775_4_, p_i1775_6_);
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        if (p_70184_1_.entityHit != null) {
            byte var2 = 0;
            if (p_70184_1_.entityHit instanceof EntityBlaze) {
                var2 = 3;
            }
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), var2);
        }
        for (int var3 = 0; var3 < 8; ++var3) {
            this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}
