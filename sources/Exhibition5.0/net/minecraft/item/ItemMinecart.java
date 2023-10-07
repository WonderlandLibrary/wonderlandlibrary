// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class ItemMinecart extends Item
{
    private static final IBehaviorDispenseItem dispenserMinecartBehavior;
    private final EntityMinecart.EnumMinecartType minecartType;
    private static final String __OBFID = "CL_00000049";
    
    public ItemMinecart(final EntityMinecart.EnumMinecartType p_i45785_1_) {
        this.maxStackSize = 1;
        this.minecartType = p_i45785_1_;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, ItemMinecart.dispenserMinecartBehavior);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState var9 = worldIn.getBlockState(pos);
        if (BlockRailBase.func_176563_d(var9)) {
            if (!worldIn.isRemote) {
                final BlockRailBase.EnumRailDirection var10 = (BlockRailBase.EnumRailDirection)((var9.getBlock() instanceof BlockRailBase) ? var9.getValue(((BlockRailBase)var9.getBlock()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                double var11 = 0.0;
                if (var10.func_177018_c()) {
                    var11 = 0.5;
                }
                final EntityMinecart var12 = EntityMinecart.func_180458_a(worldIn, pos.getX() + 0.5, pos.getY() + 0.0625 + var11, pos.getZ() + 0.5, this.minecartType);
                if (stack.hasDisplayName()) {
                    var12.setCustomNameTag(stack.getDisplayName());
                }
                worldIn.spawnEntityInWorld(var12);
            }
            --stack.stackSize;
            return true;
        }
        return false;
    }
    
    static {
        dispenserMinecartBehavior = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00000050";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                final World var4 = source.getWorld();
                final double var5 = source.getX() + var3.getFrontOffsetX() * 1.125;
                final double var6 = Math.floor(source.getY()) + var3.getFrontOffsetY();
                final double var7 = source.getZ() + var3.getFrontOffsetZ() * 1.125;
                final BlockPos var8 = source.getBlockPos().offset(var3);
                final IBlockState var9 = var4.getBlockState(var8);
                final BlockRailBase.EnumRailDirection var10 = (BlockRailBase.EnumRailDirection)((var9.getBlock() instanceof BlockRailBase) ? var9.getValue(((BlockRailBase)var9.getBlock()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                double var11;
                if (BlockRailBase.func_176563_d(var9)) {
                    if (var10.func_177018_c()) {
                        var11 = 0.6;
                    }
                    else {
                        var11 = 0.1;
                    }
                }
                else {
                    if (var9.getBlock().getMaterial() != Material.air || !BlockRailBase.func_176563_d(var4.getBlockState(var8.offsetDown()))) {
                        return this.behaviourDefaultDispenseItem.dispense(source, stack);
                    }
                    final IBlockState var12 = var4.getBlockState(var8.offsetDown());
                    final BlockRailBase.EnumRailDirection var13 = (BlockRailBase.EnumRailDirection)((var12.getBlock() instanceof BlockRailBase) ? var12.getValue(((BlockRailBase)var12.getBlock()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                    if (var3 != EnumFacing.DOWN && var13.func_177018_c()) {
                        var11 = -0.4;
                    }
                    else {
                        var11 = -0.9;
                    }
                }
                final EntityMinecart var14 = EntityMinecart.func_180458_a(var4, var5, var6 + var11, var7, ((ItemMinecart)stack.getItem()).minecartType);
                if (stack.hasDisplayName()) {
                    var14.setCustomNameTag(stack.getDisplayName());
                }
                var4.spawnEntityInWorld(var14);
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
            }
        };
    }
}
