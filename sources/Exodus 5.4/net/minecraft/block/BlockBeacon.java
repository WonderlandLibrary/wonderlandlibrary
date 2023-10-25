/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BlockBeacon
extends BlockContainer {
    public BlockBeacon() {
        super(Material.glass, MapColor.diamondColor);
        this.setHardness(3.0f);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        TileEntity tileEntity;
        super.onBlockPlacedBy(world, blockPos, iBlockState, entityLivingBase, itemStack);
        if (itemStack.hasDisplayName() && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileEntity).setName(itemStack.getDisplayName());
        }
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityBeacon();
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityBeacon) {
            entityPlayer.displayGUIChest((TileEntityBeacon)tileEntity);
            entityPlayer.triggerAchievement(StatList.field_181730_N);
        }
        return true;
    }

    public static void updateColorAsync(final World world, final BlockPos blockPos) {
        HttpUtil.field_180193_a.submit(new Runnable(){

            @Override
            public void run() {
                Chunk chunk = world.getChunkFromBlockCoords(blockPos);
                int n = blockPos.getY() - 1;
                while (n >= 0) {
                    final BlockPos blockPos2 = new BlockPos(blockPos.getX(), n, blockPos.getZ());
                    if (!chunk.canSeeSky(blockPos2)) break;
                    IBlockState iBlockState = world.getBlockState(blockPos2);
                    if (iBlockState.getBlock() == Blocks.beacon) {
                        ((WorldServer)world).addScheduledTask(new Runnable(){

                            @Override
                            public void run() {
                                TileEntity tileEntity = world.getTileEntity(blockPos2);
                                if (tileEntity instanceof TileEntityBeacon) {
                                    ((TileEntityBeacon)tileEntity).updateBeacon();
                                    world.addBlockEvent(blockPos2, Blocks.beacon, 1, 0);
                                }
                            }
                        });
                    }
                    --n;
                }
            }
        });
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityBeacon) {
            ((TileEntityBeacon)tileEntity).updateBeacon();
            world.addBlockEvent(blockPos, this, 1, 0);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}

