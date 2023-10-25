/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class EntityDragonPart
extends Entity {
    public final IEntityMultiPart entityDragonObj;
    public final String partName;

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
    }

    public EntityDragonPart(IEntityMultiPart iEntityMultiPart, String string, float f, float f2) {
        super(iEntityMultiPart.getWorld());
        this.setSize(f, f2);
        this.entityDragonObj = iEntityMultiPart;
        this.partName = string;
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return this == entity || this.entityDragonObj == entity;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return this.isEntityInvulnerable(damageSource) ? false : this.entityDragonObj.attackEntityFromPart(this, damageSource, f);
    }
}

