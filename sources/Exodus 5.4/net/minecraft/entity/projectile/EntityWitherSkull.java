/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull
extends EntityFireball {
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public EntityWitherSkull(World world) {
        super(world);
        this.setSize(0.3125f, 0.3125f);
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (!this.worldObj.isRemote) {
            if (movingObjectPosition.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0f)) {
                        if (!movingObjectPosition.entityHit.isEntityAlive()) {
                            this.shootingEntity.heal(5.0f);
                        } else {
                            this.applyEnchantments(this.shootingEntity, movingObjectPosition.entityHit);
                        }
                    }
                } else {
                    movingObjectPosition.entityHit.attackEntityFrom(DamageSource.magic, 5.0f);
                }
                if (movingObjectPosition.entityHit instanceof EntityLivingBase) {
                    int n = 0;
                    if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                        n = 10;
                    } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        n = 40;
                    }
                    if (n > 0) {
                        ((EntityLivingBase)movingObjectPosition.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * n, 1));
                    }
                }
            }
            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0f, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
            this.setDead();
        }
    }

    public EntityWitherSkull(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, d4, d5, d6);
        this.setSize(0.3125f, 0.3125f);
    }

    public boolean isInvulnerable() {
        return this.dataWatcher.getWatchableObjectByte(10) == 1;
    }

    @Override
    protected float getMotionFactor() {
        return this.isInvulnerable() ? 0.73f : super.getMotionFactor();
    }

    @Override
    public float getExplosionResistance(Explosion explosion, World world, BlockPos blockPos, IBlockState iBlockState) {
        float f = super.getExplosionResistance(explosion, world, blockPos, iBlockState);
        Block block = iBlockState.getBlock();
        if (this.isInvulnerable() && EntityWither.func_181033_a(block)) {
            f = Math.min(0.8f, f);
        }
        return f;
    }

    public EntityWitherSkull(World world, EntityLivingBase entityLivingBase, double d, double d2, double d3) {
        super(world, entityLivingBase, d, d2, d3);
        this.setSize(0.3125f, 0.3125f);
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(10, (byte)0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return false;
    }

    public void setInvulnerable(boolean bl) {
        this.dataWatcher.updateObject(10, (byte)(bl ? 1 : 0));
    }
}

