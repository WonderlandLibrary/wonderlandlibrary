/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySnowball
extends EntityThrowable {
    public EntitySnowball(World worldIn) {
        super(worldIn);
    }

    public EntitySnowball(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntitySnowball(World worldIn, double x2, double y2, double z2) {
        super(worldIn, x2, y2, z2);
    }

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (p_70184_1_.entityHit != null) {
            int i2 = 0;
            if (p_70184_1_.entityHit instanceof EntityBlaze) {
                i2 = 3;
            }
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), i2);
        }
        for (int j2 = 0; j2 < 8; ++j2) {
            this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}

