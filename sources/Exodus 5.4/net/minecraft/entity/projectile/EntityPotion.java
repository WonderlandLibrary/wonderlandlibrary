/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPotion
extends EntityThrowable {
    private ItemStack potionDamage;

    public EntityPotion(World world, double d, double d2, double d3, ItemStack itemStack) {
        super(world, d, d2, d3);
        this.potionDamage = itemStack;
    }

    public void setPotionDamage(int n) {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        this.potionDamage.setItemDamage(n);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        if (this.potionDamage != null) {
            nBTTagCompound.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
        }
    }

    public int getPotionDamage() {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        return this.potionDamage.getMetadata();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05f;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("Potion", 10)) {
            this.potionDamage = ItemStack.loadItemStackFromNBT(nBTTagCompound.getCompoundTag("Potion"));
        } else {
            this.setPotionDamage(nBTTagCompound.getInteger("potionValue"));
        }
        if (this.potionDamage == null) {
            this.setDead();
        }
    }

    @Override
    protected float getVelocity() {
        return 0.5f;
    }

    public EntityPotion(World world) {
        super(world);
    }

    @Override
    protected float getInaccuracy() {
        return -20.0f;
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (!this.worldObj.isRemote) {
            AxisAlignedBB axisAlignedBB;
            List<EntityLivingBase> list;
            List<PotionEffect> list2 = Items.potionitem.getEffects(this.potionDamage);
            if (list2 != null && !list2.isEmpty() && !(list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB = this.getEntityBoundingBox().expand(4.0, 2.0, 4.0))).isEmpty()) {
                for (EntityLivingBase entityLivingBase : list) {
                    double d = this.getDistanceSqToEntity(entityLivingBase);
                    if (!(d < 16.0)) continue;
                    double d2 = 1.0 - Math.sqrt(d) / 4.0;
                    if (entityLivingBase == movingObjectPosition.entityHit) {
                        d2 = 1.0;
                    }
                    for (PotionEffect potionEffect : list2) {
                        int n = potionEffect.getPotionID();
                        if (Potion.potionTypes[n].isInstant()) {
                            Potion.potionTypes[n].affectEntity(this, this.getThrower(), entityLivingBase, potionEffect.getAmplifier(), d2);
                            continue;
                        }
                        int n2 = (int)(d2 * (double)potionEffect.getDuration() + 0.5);
                        if (n2 <= 20) continue;
                        entityLivingBase.addPotionEffect(new PotionEffect(n, n2, potionEffect.getAmplifier()));
                    }
                }
            }
            this.worldObj.playAuxSFX(2002, new BlockPos(this), this.getPotionDamage());
            this.setDead();
        }
    }

    public EntityPotion(World world, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super(world, entityLivingBase);
        this.potionDamage = itemStack;
    }

    public EntityPotion(World world, EntityLivingBase entityLivingBase, int n) {
        this(world, entityLivingBase, new ItemStack(Items.potionitem, 1, n));
    }

    public EntityPotion(World world, double d, double d2, double d3, int n) {
        this(world, d, d2, d3, new ItemStack(Items.potionitem, 1, n));
    }
}

