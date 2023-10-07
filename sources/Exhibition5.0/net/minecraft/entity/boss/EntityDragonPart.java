// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.boss;

import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.Entity;

public class EntityDragonPart extends Entity
{
    public final IEntityMultiPart entityDragonObj;
    public final String field_146032_b;
    private static final String __OBFID = "CL_00001657";
    
    public EntityDragonPart(final IEntityMultiPart p_i1697_1_, final String p_i1697_2_, final float p_i1697_3_, final float p_i1697_4_) {
        super(p_i1697_1_.func_82194_d());
        this.setSize(p_i1697_3_, p_i1697_4_);
        this.entityDragonObj = p_i1697_1_;
        this.field_146032_b = p_i1697_2_;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return !this.func_180431_b(source) && this.entityDragonObj.attackEntityFromPart(this, source, amount);
    }
    
    @Override
    public boolean isEntityEqual(final Entity entityIn) {
        return this == entityIn || this.entityDragonObj == entityIn;
    }
}
