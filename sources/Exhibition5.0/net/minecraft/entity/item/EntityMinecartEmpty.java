// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart
{
    private static final String __OBFID = "CL_00001677";
    
    public EntityMinecartEmpty(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartEmpty(final World worldIn, final double p_i1723_2_, final double p_i1723_4_, final double p_i1723_6_) {
        super(worldIn, p_i1723_2_, p_i1723_4_, p_i1723_6_);
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
            return true;
        }
        if (this.riddenByEntity != null && this.riddenByEntity != playerIn) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            playerIn.mountEntity(this);
        }
        return true;
    }
    
    @Override
    public void onActivatorRailPass(final int p_96095_1_, final int p_96095_2_, final int p_96095_3_, final boolean p_96095_4_) {
        if (p_96095_4_) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.mountEntity(null);
            }
            if (this.getRollingAmplitude() == 0) {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0f);
                this.setBeenAttacked();
            }
        }
    }
    
    @Override
    public EnumMinecartType func_180456_s() {
        return EnumMinecartType.RIDEABLE;
    }
}
