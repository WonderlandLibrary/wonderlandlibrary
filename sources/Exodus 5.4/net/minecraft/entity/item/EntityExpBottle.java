/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityExpBottle
extends EntityThrowable {
    @Override
    protected float getInaccuracy() {
        return -20.0f;
    }

    public EntityExpBottle(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (!this.worldObj.isRemote) {
            this.worldObj.playAuxSFX(2002, new BlockPos(this), 0);
            int n = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);
            while (n > 0) {
                int n2 = EntityXPOrb.getXPSplit(n);
                n -= n2;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, n2));
            }
            this.setDead();
        }
    }

    @Override
    protected float getVelocity() {
        return 0.7f;
    }

    public EntityExpBottle(World world) {
        super(world);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.07f;
    }

    public EntityExpBottle(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }
}

