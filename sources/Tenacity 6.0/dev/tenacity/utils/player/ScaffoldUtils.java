// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.util.Vec3;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import dev.tenacity.module.Module;
import dev.tenacity.module.impl.movement.Speed;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.utils.Utils;

public class ScaffoldUtils implements Utils
{
    public static double getYLevel() {
        if (Scaffold.keepYMode.is("Off") || (Scaffold.keepYMode.is("Speed only") && !Tenacity.INSTANCE.isEnabled(Speed.class))) {
            return ScaffoldUtils.mc.thePlayer.posY - 1.0;
        }
        return (ScaffoldUtils.mc.thePlayer.posY - 1.0 >= Scaffold.keepYCoord && Math.max(ScaffoldUtils.mc.thePlayer.posY, Scaffold.keepYCoord) - Math.min(ScaffoldUtils.mc.thePlayer.posY, Scaffold.keepYCoord) <= 3.0 && !ScaffoldUtils.mc.gameSettings.keyBindJump.isKeyDown()) ? Scaffold.keepYCoord : (ScaffoldUtils.mc.thePlayer.posY - 1.0);
    }
    
    public static BlockCache getBlockInfo() {
        final BlockPos belowBlockPos = new BlockPos(ScaffoldUtils.mc.thePlayer.posX, getYLevel() - (Scaffold.isDownwards() ? 1 : 0), ScaffoldUtils.mc.thePlayer.posZ);
        if (ScaffoldUtils.mc.theWorld.getBlockState(belowBlockPos).getBlock() instanceof BlockAir) {
            for (int x = 0; x < 4; ++x) {
                for (int z = 0; z < 4; ++z) {
                    for (int i = 1; i > -3; i -= 2) {
                        final BlockPos blockPos = belowBlockPos.add(x * i, 0, z * i);
                        if (ScaffoldUtils.mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir) {
                            for (final EnumFacing direction : EnumFacing.values()) {
                                final BlockPos block = blockPos.offset(direction);
                                final Material material = ScaffoldUtils.mc.theWorld.getBlockState(block).getBlock().getMaterial();
                                if (material.isSolid() && !material.isLiquid()) {
                                    return new BlockCache(block, direction.getOpposite());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static int getBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = ScaffoldUtils.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock)itemStack.getItem();
                if (isBlockValid(itemBlock.getBlock())) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static int getBlockCount() {
        int count = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = ScaffoldUtils.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock)itemStack.getItem();
                if (isBlockValid(itemBlock.getBlock())) {
                    count += itemStack.stackSize;
                }
            }
        }
        return count;
    }
    
    private static boolean isBlockValid(final Block block) {
        return (block.isFullBlock() || block == Blocks.glass) && block != Blocks.sand && block != Blocks.gravel && block != Blocks.dispenser && block != Blocks.command_block && block != Blocks.noteblock && block != Blocks.furnace && block != Blocks.crafting_table && block != Blocks.tnt && block != Blocks.dropper && block != Blocks.beacon;
    }
    
    public static Vec3 getHypixelVec3(final BlockCache data) {
        final BlockPos pos = data.position;
        final EnumFacing face = data.facing;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            y += 0.5;
        }
        else {
            x += 0.3;
            z += 0.3;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += 0.15;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += 0.15;
        }
        return new Vec3(x, y, z);
    }
    
    public static class BlockCache
    {
        private final BlockPos position;
        private final EnumFacing facing;
        
        public BlockCache(final BlockPos position, final EnumFacing facing) {
            this.position = position;
            this.facing = facing;
        }
        
        public BlockPos getPosition() {
            return this.position;
        }
        
        public EnumFacing getFacing() {
            return this.facing;
        }
    }
}
