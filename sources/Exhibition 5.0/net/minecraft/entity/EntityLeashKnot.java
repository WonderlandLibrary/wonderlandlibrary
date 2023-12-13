// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.block.BlockFence;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging
{
    private static final String __OBFID = "CL_00001548";
    
    public EntityLeashKnot(final World worldIn) {
        super(worldIn);
    }
    
    public EntityLeashKnot(final World worldIn, final BlockPos p_i45851_2_) {
        super(worldIn, p_i45851_2_);
        this.setPosition(p_i45851_2_.getX() + 0.5, p_i45851_2_.getY() + 0.5, p_i45851_2_.getZ() + 0.5);
        final float var3 = 0.125f;
        final float var4 = 0.1875f;
        final float var5 = 0.25f;
        this.func_174826_a(new AxisAlignedBB(this.posX - 0.1875, this.posY - 0.25 + 0.125, this.posZ - 0.1875, this.posX + 0.1875, this.posY + 0.25 + 0.125, this.posZ + 0.1875));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    public void func_174859_a(final EnumFacing p_174859_1_) {
    }
    
    @Override
    public int getWidthPixels() {
        return 9;
    }
    
    @Override
    public int getHeightPixels() {
        return 9;
    }
    
    @Override
    public float getEyeHeight() {
        return -0.0625f;
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 1024.0;
    }
    
    @Override
    public void onBroken(final Entity p_110128_1_) {
    }
    
    @Override
    public boolean writeToNBTOptional(final NBTTagCompound tagCompund) {
        return false;
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        final ItemStack var2 = playerIn.getHeldItem();
        boolean var3 = false;
        if (var2 != null && var2.getItem() == Items.lead && !this.worldObj.isRemote) {
            final double var4 = 7.0;
            final List var5 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
            for (final EntityLiving var7 : var5) {
                if (var7.getLeashed() && var7.getLeashedToEntity() == playerIn) {
                    var7.setLeashedToEntity(this, true);
                    var3 = true;
                }
            }
        }
        if (!this.worldObj.isRemote && !var3) {
            this.setDead();
            if (playerIn.capabilities.isCreativeMode) {
                final double var4 = 7.0;
                final List var5 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
                for (final EntityLiving var7 : var5) {
                    if (var7.getLeashed() && var7.getLeashedToEntity() == this) {
                        var7.clearLeashed(true, false);
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean onValidSurface() {
        return this.worldObj.getBlockState(this.field_174861_a).getBlock() instanceof BlockFence;
    }
    
    public static EntityLeashKnot func_174862_a(final World worldIn, final BlockPos p_174862_1_) {
        final EntityLeashKnot var2 = new EntityLeashKnot(worldIn, p_174862_1_);
        var2.forceSpawn = true;
        worldIn.spawnEntityInWorld(var2);
        return var2;
    }
    
    public static EntityLeashKnot func_174863_b(final World worldIn, final BlockPos p_174863_1_) {
        final int var2 = p_174863_1_.getX();
        final int var3 = p_174863_1_.getY();
        final int var4 = p_174863_1_.getZ();
        final List var5 = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB(var2 - 1.0, var3 - 1.0, var4 - 1.0, var2 + 1.0, var3 + 1.0, var4 + 1.0));
        for (final EntityLeashKnot var7 : var5) {
            if (var7.func_174857_n().equals(p_174863_1_)) {
                return var7;
            }
        }
        return null;
    }
}
