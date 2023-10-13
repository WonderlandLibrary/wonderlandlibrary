/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand
extends Item {
    private static final String __OBFID = "CL_00002182";

    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        double var17;
        double var15;
        BlockPos var10;
        if (side == EnumFacing.DOWN) {
            return false;
        }
        boolean var9 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        BlockPos blockPos = var10 = var9 ? pos : pos.offset(side);
        if (!playerIn.func_175151_a(var10, side, stack)) {
            return false;
        }
        BlockPos var11 = var10.offsetUp();
        boolean var12 = !worldIn.isAirBlock(var10) && !worldIn.getBlockState(var10).getBlock().isReplaceable(worldIn, var10);
        if (var12 |= !worldIn.isAirBlock(var11) && !worldIn.getBlockState(var11).getBlock().isReplaceable(worldIn, var11)) {
            return false;
        }
        double var13 = var10.getX();
        List var19 = worldIn.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.fromBounds(var13, var15 = (double)var10.getY(), var17 = (double)var10.getZ(), var13 + 1.0, var15 + 2.0, var17 + 1.0));
        if (var19.size() > 0) {
            return false;
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockToAir(var10);
            worldIn.setBlockToAir(var11);
            EntityArmorStand var20 = new EntityArmorStand(worldIn, var13 + 0.5, var15, var17 + 0.5);
            float var21 = (float)MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            var20.setLocationAndAngles(var13 + 0.5, var15, var17 + 0.5, var21, 0.0f);
            this.func_179221_a(var20, worldIn.rand);
            NBTTagCompound var22 = stack.getTagCompound();
            if (var22 != null && var22.hasKey("EntityTag", 10)) {
                NBTTagCompound var23 = new NBTTagCompound();
                var20.writeToNBTOptional(var23);
                var23.merge(var22.getCompoundTag("EntityTag"));
                var20.readFromNBT(var23);
            }
            worldIn.spawnEntityInWorld(var20);
        }
        --stack.stackSize;
        return true;
    }

    private void func_179221_a(EntityArmorStand p_179221_1_, Random p_179221_2_) {
        Rotations var3 = p_179221_1_.getHeadRotation();
        float var5 = p_179221_2_.nextFloat() * 5.0f;
        float var6 = p_179221_2_.nextFloat() * 20.0f - 10.0f;
        Rotations var4 = new Rotations(var3.func_179415_b() + var5, var3.func_179416_c() + var6, var3.func_179413_d());
        p_179221_1_.setHeadRotation(var4);
        var3 = p_179221_1_.getBodyRotation();
        var5 = p_179221_2_.nextFloat() * 10.0f - 5.0f;
        var4 = new Rotations(var3.func_179415_b(), var3.func_179416_c() + var5, var3.func_179413_d());
        p_179221_1_.setBodyRotation(var4);
    }
}

