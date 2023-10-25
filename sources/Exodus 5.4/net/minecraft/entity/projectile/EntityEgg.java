/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEgg
extends EntityThrowable {
    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (movingObjectPosition.entityHit != null) {
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0f);
        }
        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
            int n = 1;
            if (this.rand.nextInt(32) == 0) {
                n = 4;
            }
            int n2 = 0;
            while (n2 < n) {
                EntityChicken entityChicken = new EntityChicken(this.worldObj);
                entityChicken.setGrowingAge(-24000);
                entityChicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                this.worldObj.spawnEntityInWorld(entityChicken);
                ++n2;
            }
        }
        double d = 0.08;
        int n = 0;
        while (n < 8) {
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5) * 0.08, ((double)this.rand.nextFloat() - 0.5) * 0.08, ((double)this.rand.nextFloat() - 0.5) * 0.08, Item.getIdFromItem(Items.egg));
            ++n;
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }

    public EntityEgg(World world) {
        super(world);
    }

    public EntityEgg(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
    }

    public EntityEgg(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }
}

