/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLargeFireball
extends EntityFireball {
    public int explosionPower = 1;

    public EntityLargeFireball(World world) {
        super(world);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("ExplosionPower", this.explosionPower);
    }

    public EntityLargeFireball(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        super(world, d, d2, d3, d4, d5, d6);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("ExplosionPower", 99)) {
            this.explosionPower = nBTTagCompound.getInteger("ExplosionPower");
        }
    }

    public EntityLargeFireball(World world, EntityLivingBase entityLivingBase, double d, double d2, double d3) {
        super(world, entityLivingBase, d, d2, d3);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (!this.worldObj.isRemote) {
            if (movingObjectPosition.entityHit != null) {
                movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0f);
                this.applyEnchantments(this.shootingEntity, movingObjectPosition.entityHit);
            }
            boolean bl = this.worldObj.getGameRules().getBoolean("mobGriefing");
            this.worldObj.newExplosion(null, this.posX, this.posY, this.posZ, this.explosionPower, bl, bl);
            this.setDead();
        }
    }
}

