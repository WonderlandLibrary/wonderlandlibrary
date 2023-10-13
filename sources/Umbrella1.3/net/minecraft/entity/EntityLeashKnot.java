/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityLeashKnot
extends EntityHanging {
    private static final String __OBFID = "CL_00001548";

    public EntityLeashKnot(World worldIn) {
        super(worldIn);
    }

    public EntityLeashKnot(World worldIn, BlockPos p_i45851_2_) {
        super(worldIn, p_i45851_2_);
        this.setPosition((double)p_i45851_2_.getX() + 0.5, (double)p_i45851_2_.getY() + 0.5, (double)p_i45851_2_.getZ() + 0.5);
        float var3 = 0.125f;
        float var4 = 0.1875f;
        float var5 = 0.25f;
        this.func_174826_a(new AxisAlignedBB(this.posX - 0.1875, this.posY - 0.25 + 0.125, this.posZ - 0.1875, this.posX + 0.1875, this.posY + 0.25 + 0.125, this.posZ + 0.1875));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void func_174859_a(EnumFacing p_174859_1_) {
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
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 1024.0;
    }

    @Override
    public void onBroken(Entity p_110128_1_) {
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    @Override
    public boolean interactFirst(EntityPlayer playerIn) {
        List var6;
        double var4;
        ItemStack var2 = playerIn.getHeldItem();
        boolean var3 = false;
        if (var2 != null && var2.getItem() == Items.lead && !this.worldObj.isRemote) {
            var4 = 7.0;
            var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
            for (EntityLiving var8 : var6) {
                if (!var8.getLeashed() || var8.getLeashedToEntity() != playerIn) continue;
                var8.setLeashedToEntity(this, true);
                var3 = true;
            }
        }
        if (!this.worldObj.isRemote && !var3) {
            this.setDead();
            if (playerIn.capabilities.isCreativeMode) {
                var4 = 7.0;
                var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
                for (EntityLiving var8 : var6) {
                    if (!var8.getLeashed() || var8.getLeashedToEntity() != this) continue;
                    var8.clearLeashed(true, false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onValidSurface() {
        return this.worldObj.getBlockState(this.field_174861_a).getBlock() instanceof BlockFence;
    }

    public static EntityLeashKnot func_174862_a(World worldIn, BlockPos p_174862_1_) {
        EntityLeashKnot var2 = new EntityLeashKnot(worldIn, p_174862_1_);
        var2.forceSpawn = true;
        worldIn.spawnEntityInWorld(var2);
        return var2;
    }

    public static EntityLeashKnot func_174863_b(World worldIn, BlockPos p_174863_1_) {
        EntityLeashKnot var7;
        int var2 = p_174863_1_.getX();
        int var3 = p_174863_1_.getY();
        int var4 = p_174863_1_.getZ();
        List var5 = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB((double)var2 - 1.0, (double)var3 - 1.0, (double)var4 - 1.0, (double)var2 + 1.0, (double)var3 + 1.0, (double)var4 + 1.0));
        Iterator var6 = var5.iterator();
        do {
            if (var6.hasNext()) continue;
            return null;
        } while (!(var7 = (EntityLeashKnot)var6.next()).func_174857_n().equals(p_174863_1_));
        return var7;
    }
}

