/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob
extends EntityLiving
implements IAnimals {
    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    @Override
    protected boolean canDespawn() {
        return true;
    }

    public EntityWaterMob(World world) {
        super(world);
    }

    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    @Override
    public void onEntityUpdate() {
        int n = this.getAir();
        super.onEntityUpdate();
        if (this.isEntityAlive() && !this.isInWater()) {
            this.setAir(--n);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.drown, 2.0f);
            }
        } else {
            this.setAir(300);
        }
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}

