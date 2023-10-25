/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockNote
extends BlockContainer {
    private static final List<String> INSTRUMENTS = Lists.newArrayList((Object[])new String[]{"harp", "bd", "snare", "hat", "bassattack"});

    public BlockNote() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    private String getInstrument(int n) {
        if (n < 0 || n >= INSTRUMENTS.size()) {
            n = 0;
        }
        return INSTRUMENTS.get(n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityNote();
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        TileEntity tileEntity;
        if (!world.isRemote && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityNote) {
            ((TileEntityNote)tileEntity).triggerNote(world, blockPos);
            entityPlayer.triggerAchievement(StatList.field_181734_R);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        boolean bl = world.isBlockPowered(blockPos);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityNote) {
            TileEntityNote tileEntityNote = (TileEntityNote)tileEntity;
            if (tileEntityNote.previousRedstoneState != bl) {
                if (bl) {
                    tileEntityNote.triggerNote(world, blockPos);
                }
                tileEntityNote.previousRedstoneState = bl;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityNote) {
            TileEntityNote tileEntityNote = (TileEntityNote)tileEntity;
            tileEntityNote.changePitch();
            tileEntityNote.triggerNote(world, blockPos);
            entityPlayer.triggerAchievement(StatList.field_181735_S);
        }
        return true;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean onBlockEventReceived(World world, BlockPos blockPos, IBlockState iBlockState, int n, int n2) {
        float f = (float)Math.pow(2.0, (double)(n2 - 12) / 12.0);
        world.playSoundEffect((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, "note." + this.getInstrument(n), 3.0f, f);
        world.spawnParticle(EnumParticleTypes.NOTE, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.2, (double)blockPos.getZ() + 0.5, (double)n2 / 24.0, 0.0, 0.0, new int[0]);
        return true;
    }
}

