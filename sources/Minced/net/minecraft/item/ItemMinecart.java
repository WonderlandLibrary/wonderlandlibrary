// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class ItemMinecart extends Item
{
    private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR;
    private final EntityMinecart.Type minecartType;
    
    public ItemMinecart(final EntityMinecart.Type typeIn) {
        this.maxStackSize = 1;
        this.minecartType = typeIn;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemMinecart.MINECART_DISPENSER_BEHAVIOR);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (!BlockRailBase.isRailBlock(iblockstate)) {
            return EnumActionResult.FAIL;
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!worldIn.isRemote) {
            final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double d0 = 0.0;
            if (blockrailbase$enumraildirection.isAscending()) {
                d0 = 0.5;
            }
            final EntityMinecart entityminecart = EntityMinecart.create(worldIn, pos.getX() + 0.5, pos.getY() + 0.0625 + d0, pos.getZ() + 0.5, this.minecartType);
            if (itemstack.hasDisplayName()) {
                entityminecart.setCustomNameTag(itemstack.getDisplayName());
            }
            worldIn.spawnEntity(entityminecart);
        }
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
    
    static {
        MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
                final World world = source.getWorld();
                final double d0 = source.getX() + enumfacing.getXOffset() * 1.125;
                final double d2 = Math.floor(source.getY()) + enumfacing.getYOffset();
                final double d3 = source.getZ() + enumfacing.getZOffset() * 1.125;
                final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
                final IBlockState iblockstate = world.getBlockState(blockpos);
                final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d4;
                if (BlockRailBase.isRailBlock(iblockstate)) {
                    if (blockrailbase$enumraildirection.isAscending()) {
                        d4 = 0.6;
                    }
                    else {
                        d4 = 0.1;
                    }
                }
                else {
                    if (iblockstate.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) {
                        return this.behaviourDefaultDispenseItem.dispense(source, stack);
                    }
                    final IBlockState iblockstate2 = world.getBlockState(blockpos.down());
                    final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection2 = (iblockstate2.getBlock() instanceof BlockRailBase) ? iblockstate2.getValue(((BlockRailBase)iblockstate2.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                    if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection2.isAscending()) {
                        d4 = -0.4;
                    }
                    else {
                        d4 = -0.9;
                    }
                }
                final EntityMinecart entityminecart = EntityMinecart.create(world, d0, d2 + d4, d3, ((ItemMinecart)stack.getItem()).minecartType);
                if (stack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(stack.getDisplayName());
                }
                world.spawnEntity(entityminecart);
                stack.shrink(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playEvent(1000, source.getBlockPos(), 0);
            }
        };
    }
}
