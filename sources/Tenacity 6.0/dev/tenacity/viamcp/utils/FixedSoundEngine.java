// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.utils;

import dev.tenacity.viamcp.ViaMCP;
import dev.tenacity.module.impl.movement.Flight;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;

public class FixedSoundEngine
{
    private static final Minecraft mc;
    
    public static boolean destroyBlock(final World world, final BlockPos pos, final boolean dropBlock) {
        final IBlockState iblockstate = world.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
        if (block.getMaterial() == Material.air) {
            return false;
        }
        if (dropBlock) {
            block.dropBlockAsItem(world, pos, iblockstate, 0);
        }
        return world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }
    
    public static boolean onItemUse(final ItemBlock iblock, final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (worldIn.canBlockBePlaced(iblock.getBlock(), pos, false, side, null, stack)) {
            final int i = iblock.getMetadata(stack.getMetadata());
            IBlockState iblockstate2 = iblock.getBlock().onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);
            if (worldIn.setBlockState(pos, iblockstate2, 3)) {
                iblockstate2 = worldIn.getBlockState(pos);
                if (iblockstate2.getBlock() == iblock.getBlock()) {
                    ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
                    iblock.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate2, playerIn, stack);
                }
                if (!Flight.hiddenBlocks.contains(pos)) {
                    if (ViaMCP.getInstance().getVersion() != 47) {
                        FixedSoundEngine.mc.theWorld.playSoundAtPos(pos.add(0.5, 0.5, 0.5), iblock.getBlock().stepSound.getPlaceSound(), (iblock.getBlock().stepSound.getVolume() + 1.0f) / 2.0f, iblock.getBlock().stepSound.getFrequency() * 0.8f, false);
                    }
                    else {
                        worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, iblock.getBlock().stepSound.getPlaceSound(), (iblock.getBlock().stepSound.getVolume() + 1.0f) / 2.0f, iblock.getBlock().stepSound.getFrequency() * 0.8f);
                    }
                }
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
