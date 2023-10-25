/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySmallFireball
extends EntityFireball {
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public EntitySmallFireball(World world) {
        super(world);
        this.setSize(0.3125f, 0.3125f);
    }

    public EntitySmallFireball(World world, EntityLivingBase entityLivingBase, double d, double d2, double d3) {
        super(world, entityLivingBase, d, d2, d3);
        this.setSize(0.3125f, 0.3125f);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (!this.worldObj.isRemote) {
            if (movingObjectPosition.entityHit != null) {
                boolean bl = movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0f);
                if (bl) {
                    this.applyEnchantments(this.shootingEntity, movingObjectPosition.entityHit);
                    if (!movingObjectPosition.entityHit.isImmuneToFire()) {
                        movingObjectPosition.entityHit.setFire(5);
                    }
                }
            } else {
                BlockPos blockPos;
                boolean bl = true;
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
                    bl = this.worldObj.getGameRules().getBoolean("mobGriefing");
                }
                if (bl && this.worldObj.isAirBlock(blockPos = movingObjectPosition.getBlockPos().offset(movingObjectPosition.sideHit))) {
                    this.worldObj.setBlockState(blockPos, Blocks.fire.getDefaultState());
                }
            }
            this.setDead();
        }
    }

    public EntitySmallFireball(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, d4, d5, d6);
        this.setSize(0.3125f, 0.3125f);
    }
}

