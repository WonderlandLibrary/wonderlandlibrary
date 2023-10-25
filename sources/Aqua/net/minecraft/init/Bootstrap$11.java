package net.minecraft.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static final class Bootstrap.11
extends BehaviorDefaultDispenseItem {
    private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

    Bootstrap.11() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        Item item;
        BlockPos blockpos;
        World world = source.getWorld();
        IBlockState iblockstate = world.getBlockState(blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata())));
        Block block = iblockstate.getBlock();
        Material material = block.getMaterial();
        if (Material.water.equals((Object)material) && block instanceof BlockLiquid && (Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL) == 0) {
            item = Items.water_bucket;
        } else {
            if (!Material.lava.equals((Object)material) || !(block instanceof BlockLiquid) || (Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL) != 0) {
                return super.dispenseStack(source, stack);
            }
            item = Items.lava_bucket;
        }
        world.setBlockToAir(blockpos);
        if (--stack.stackSize == 0) {
            stack.setItem(item);
            stack.stackSize = 1;
        } else if (((TileEntityDispenser)source.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0) {
            this.field_150840_b.dispense(source, new ItemStack(item));
        }
        return stack;
    }
}
