// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.util.Rotations;
import java.util.Random;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemArmorStand extends Item
{
    private static final String __OBFID = "CL_00002182";
    
    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        final boolean var9 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        final BlockPos var10 = var9 ? pos : pos.offset(side);
        if (!playerIn.func_175151_a(var10, side, stack)) {
            return false;
        }
        final BlockPos var11 = var10.offsetUp();
        boolean var12 = !worldIn.isAirBlock(var10) && !worldIn.getBlockState(var10).getBlock().isReplaceable(worldIn, var10);
        var12 |= (!worldIn.isAirBlock(var11) && !worldIn.getBlockState(var11).getBlock().isReplaceable(worldIn, var11));
        if (var12) {
            return false;
        }
        final double var13 = var10.getX();
        final double var14 = var10.getY();
        final double var15 = var10.getZ();
        final List var16 = worldIn.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.fromBounds(var13, var14, var15, var13 + 1.0, var14 + 2.0, var15 + 1.0));
        if (var16.size() > 0) {
            return false;
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockToAir(var10);
            worldIn.setBlockToAir(var11);
            final EntityArmorStand var17 = new EntityArmorStand(worldIn, var13 + 0.5, var14, var15 + 0.5);
            final float var18 = MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            var17.setLocationAndAngles(var13 + 0.5, var14, var15 + 0.5, var18, 0.0f);
            this.func_179221_a(var17, worldIn.rand);
            final NBTTagCompound var19 = stack.getTagCompound();
            if (var19 != null && var19.hasKey("EntityTag", 10)) {
                final NBTTagCompound var20 = new NBTTagCompound();
                var17.writeToNBTOptional(var20);
                var20.merge(var19.getCompoundTag("EntityTag"));
                var17.readFromNBT(var20);
            }
            worldIn.spawnEntityInWorld(var17);
        }
        --stack.stackSize;
        return true;
    }
    
    private void func_179221_a(final EntityArmorStand p_179221_1_, final Random p_179221_2_) {
        Rotations var3 = p_179221_1_.getHeadRotation();
        float var4 = p_179221_2_.nextFloat() * 5.0f;
        final float var5 = p_179221_2_.nextFloat() * 20.0f - 10.0f;
        Rotations var6 = new Rotations(var3.func_179415_b() + var4, var3.func_179416_c() + var5, var3.func_179413_d());
        p_179221_1_.setHeadRotation(var6);
        var3 = p_179221_1_.getBodyRotation();
        var4 = p_179221_2_.nextFloat() * 10.0f - 5.0f;
        var6 = new Rotations(var3.func_179415_b(), var3.func_179416_c() + var4, var3.func_179413_d());
        p_179221_1_.setBodyRotation(var6);
    }
}
