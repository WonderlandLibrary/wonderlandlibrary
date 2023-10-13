/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class EntityDragonPart
extends Entity {
    public final IEntityMultiPart entityDragonObj;
    public final String field_146032_b;
    private static final String __OBFID = "CL_00001657";

    public EntityDragonPart(IEntityMultiPart p_i1697_1_, String p_i1697_2_, float p_i1697_3_, float p_i1697_4_) {
        super(p_i1697_1_.func_82194_d());
        this.setSize(p_i1697_3_, p_i1697_4_);
        this.entityDragonObj = p_i1697_1_;
        this.field_146032_b = p_i1697_2_;
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
        return this.func_180431_b(source) ? false : this.entityDragonObj.attackEntityFromPart(this, source, amount);
    }

    @Override
    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn || this.entityDragonObj == entityIn;
    }
}

