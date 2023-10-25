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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMinecart
extends Item {
    private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem(){
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

        @Override
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            double d3;
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection;
            EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
            World world = source.getWorld();
            double d0 = source.getX() + (double)enumfacing.getXOffset() * 1.125;
            double d1 = Math.floor(source.getY()) + (double)enumfacing.getYOffset();
            double d2 = source.getZ() + (double)enumfacing.getZOffset() * 1.125;
            BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            IBlockState iblockstate = world.getBlockState(blockpos);
            BlockRailBase.EnumRailDirection enumRailDirection = blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            if (BlockRailBase.isRailBlock(iblockstate)) {
                d3 = blockrailbase$enumraildirection.isAscending() ? 0.6 : 0.1;
            } else {
                if (iblockstate.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }
                IBlockState iblockstate1 = world.getBlockState(blockpos.down());
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1.getBlock() instanceof BlockRailBase ? iblockstate1.getValue(((BlockRailBase)iblockstate1.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                d3 = enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending() ? -0.4 : -0.9;
            }
            EntityMinecart entityminecart = EntityMinecart.create(world, d0, d1 + d3, d2, ((ItemMinecart)stack.getItem()).minecartType);
            if (stack.hasDisplayName()) {
                entityminecart.setCustomNameTag(stack.getDisplayName());
            }
            world.spawnEntityInWorld(entityminecart);
            stack.func_190918_g(1);
            return stack;
        }

        @Override
        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
    };
    private final EntityMinecart.Type minecartType;

    public ItemMinecart(EntityMinecart.Type typeIn) {
        this.maxStackSize = 1;
        this.minecartType = typeIn;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, MINECART_DISPENSER_BEHAVIOR);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        if (!BlockRailBase.isRailBlock(iblockstate)) {
            return EnumActionResult.FAIL;
        }
        ItemStack itemstack = stack.getHeldItem(pos);
        if (!playerIn.isRemote) {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double d0 = 0.0;
            if (blockrailbase$enumraildirection.isAscending()) {
                d0 = 0.5;
            }
            EntityMinecart entityminecart = EntityMinecart.create(playerIn, (double)worldIn.getX() + 0.5, (double)worldIn.getY() + 0.0625 + d0, (double)worldIn.getZ() + 0.5, this.minecartType);
            if (itemstack.hasDisplayName()) {
                entityminecart.setCustomNameTag(itemstack.getDisplayName());
            }
            playerIn.spawnEntityInWorld(entityminecart);
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
}

