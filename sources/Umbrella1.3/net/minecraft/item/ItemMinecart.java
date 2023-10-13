/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMinecart
extends Item {
    private static final IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem(){
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
        private static final String __OBFID = "CL_00000050";

        @Override
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            double var14;
            BlockRailBase.EnumRailDirection var13;
            EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
            World var4 = source.getWorld();
            double var5 = source.getX() + (double)var3.getFrontOffsetX() * 1.125;
            double var7 = Math.floor(source.getY()) + (double)var3.getFrontOffsetY();
            double var9 = source.getZ() + (double)var3.getFrontOffsetZ() * 1.125;
            BlockPos var11 = source.getBlockPos().offset(var3);
            IBlockState var12 = var4.getBlockState(var11);
            BlockRailBase.EnumRailDirection enumRailDirection = var13 = var12.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)((Object)var12.getValue(((BlockRailBase)var12.getBlock()).func_176560_l())) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            if (BlockRailBase.func_176563_d(var12)) {
                var14 = var13.func_177018_c() ? 0.6 : 0.1;
            } else {
                if (var12.getBlock().getMaterial() != Material.air || !BlockRailBase.func_176563_d(var4.getBlockState(var11.offsetDown()))) {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }
                IBlockState var16 = var4.getBlockState(var11.offsetDown());
                BlockRailBase.EnumRailDirection var17 = var16.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)((Object)var16.getValue(((BlockRailBase)var16.getBlock()).func_176560_l())) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                var14 = var3 != EnumFacing.DOWN && var17.func_177018_c() ? -0.4 : -0.9;
            }
            EntityMinecart var18 = EntityMinecart.func_180458_a(var4, var5, var7 + var14, var9, ((ItemMinecart)stack.getItem()).minecartType);
            if (stack.hasDisplayName()) {
                var18.setCustomNameTag(stack.getDisplayName());
            }
            var4.spawnEntityInWorld(var18);
            stack.splitStack(1);
            return stack;
        }

        @Override
        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
        }
    };
    private final EntityMinecart.EnumMinecartType minecartType;
    private static final String __OBFID = "CL_00000049";

    public ItemMinecart(EntityMinecart.EnumMinecartType p_i45785_1_) {
        this.maxStackSize = 1;
        this.minecartType = p_i45785_1_;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var9 = worldIn.getBlockState(pos);
        if (BlockRailBase.func_176563_d(var9)) {
            if (!worldIn.isRemote) {
                BlockRailBase.EnumRailDirection var10 = var9.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)((Object)var9.getValue(((BlockRailBase)var9.getBlock()).func_176560_l())) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double var11 = 0.0;
                if (var10.func_177018_c()) {
                    var11 = 0.5;
                }
                EntityMinecart var13 = EntityMinecart.func_180458_a(worldIn, (double)pos.getX() + 0.5, (double)pos.getY() + 0.0625 + var11, (double)pos.getZ() + 0.5, this.minecartType);
                if (stack.hasDisplayName()) {
                    var13.setCustomNameTag(stack.getDisplayName());
                }
                worldIn.spawnEntityInWorld(var13);
            }
            --stack.stackSize;
            return true;
        }
        return false;
    }
}

