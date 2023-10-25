/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockCommandBlock
extends BlockContainer {
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(TRIGGERED, false);
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileEntity).getCommandBlockLogic().getSuccessCount() : 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileEntity).getCommandBlockLogic().tryOpenEditCommandBlock(entityPlayer) : false;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        if (iBlockState.getValue(TRIGGERED).booleanValue()) {
            n |= 1;
        }
        return n;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityCommandBlock();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityCommandBlock) {
            ((TileEntityCommandBlock)tileEntity).getCommandBlockLogic().trigger(world);
            world.updateComparatorOutputLevel(blockPos, this);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityCommandBlock) {
            CommandBlockLogic commandBlockLogic = ((TileEntityCommandBlock)tileEntity).getCommandBlockLogic();
            if (itemStack.hasDisplayName()) {
                commandBlockLogic.setName(itemStack.getDisplayName());
            }
            if (!world.isRemote) {
                commandBlockLogic.setTrackOutput(world.getGameRules().getBoolean("sendCommandFeedback"));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(TRIGGERED, (n & 1) > 0);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!world.isRemote) {
            boolean bl = world.isBlockPowered(blockPos);
            boolean bl2 = iBlockState.getValue(TRIGGERED);
            if (bl && !bl2) {
                world.setBlockState(blockPos, iBlockState.withProperty(TRIGGERED, true), 4);
                world.scheduleUpdate(blockPos, this, this.tickRate(world));
            } else if (!bl && bl2) {
                world.setBlockState(blockPos, iBlockState.withProperty(TRIGGERED, false), 4);
            }
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TRIGGERED);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    public BlockCommandBlock() {
        super(Material.iron, MapColor.adobeColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, false));
    }
}

