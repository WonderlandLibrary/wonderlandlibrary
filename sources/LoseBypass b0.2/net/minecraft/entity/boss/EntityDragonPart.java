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

    public EntityDragonPart(IEntityMultiPart parent, String partName, float base, float sizeHeight) {
        super(parent.getWorld());
        this.setSize(base, sizeHeight);
        this.entityDragonObj = parent;
        this.partName = partName;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        boolean bl = this.entityDragonObj.attackEntityFromPart(this, source, amount);
        return bl;
    }

    @Override
    public boolean isEntityEqual(Entity entityIn) {
        if (this == entityIn) return true;
        if (this.entityDragonObj == entityIn) return true;
        return false;
    }
}

