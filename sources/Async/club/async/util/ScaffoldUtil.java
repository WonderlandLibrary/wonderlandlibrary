package club.async.util;

import club.async.interfaces.MinecraftInterface;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Arrays;
import java.util.List;

public final class ScaffoldUtil implements MinecraftInterface {

    private final static List<Block> blockBlacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.tnt, Blocks.chest,
            Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet,
            Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox,
            Blocks.iron_ore, Blocks.lapis_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
            Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
            Blocks.wooden_button, Blocks.lever, Blocks.enchanting_table);
    
    public static BlockData getBlockData(BlockPos blockPos) {
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(blockPos.add(0, -1, 0)).getBlock()))
            return new BlockData(blockPos.add(0, -1, 0), EnumFacing.UP);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(blockPos.add(-1, 0, 0)).getBlock()))
            return new BlockData(blockPos.add(-1, 0, 0), EnumFacing.EAST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(blockPos.add(1, 0, 0)).getBlock()))
            return new BlockData(blockPos.add(1, 0, 0), EnumFacing.WEST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(blockPos.add(0, 0, -1)).getBlock()))
            return new BlockData(blockPos.add(0, 0, -1), EnumFacing.SOUTH);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(blockPos.add(0, 0, 1)).getBlock()))
            return new BlockData(blockPos.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add = blockPos.add(-1, 0, 0);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock()))
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock()))
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock()))
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock()))
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add2 = blockPos.add(1, 0, 0);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock()))
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock()))
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock()))
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock()))
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add3 = blockPos.add(0, 0, -1);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock()))
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock()))
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock()))
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock()))
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        BlockPos add4 = blockPos.add(0, 0, 1);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock()))
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock()))
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock()))
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        if (!blockBlacklist.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock()))
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        return null;
    }

    public static int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock)
                if (!blockBlacklist.contains(((ItemBlock) stack.getItem()).getBlock()))
                    return i - 36;
        }
        return -1;
    }

    public static int getBlockCount() {
        int count = 0;
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock)
                if (!blockBlacklist.contains(((ItemBlock) stack.getItem()).getBlock()))
                    count += stack.stackSize;
        }
        return count;
    }

    public static class BlockData {
        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }

        public EnumFacing face;
        public BlockPos position;
    }

}
