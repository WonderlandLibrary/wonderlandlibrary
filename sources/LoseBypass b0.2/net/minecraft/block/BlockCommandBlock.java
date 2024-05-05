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

    public BlockCommandBlock() {
        super(Material.iron, MapColor.adobeColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, false));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCommandBlock();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (worldIn.isRemote) return;
        boolean flag = worldIn.isBlockPowered(pos);
        boolean flag1 = state.getValue(TRIGGERED);
        if (flag && !flag1) {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, true), 4);
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            return;
        }
        if (flag) return;
        if (!flag1) return;
        worldIn.setBlockState(pos, state.withProperty(TRIGGERED, false), 4);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityCommandBlock)) return;
        ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().trigger(worldIn);
        worldIn.updateComparatorOutputLevel(pos, this);
    }

    @Override
    public int tickRate(World worldIn) {
        return 1;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityCommandBlock)) return false;
        boolean bl = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().tryOpenEditCommandBlock(playerIn);
        return bl;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityCommandBlock)) return 0;
        int n = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount();
        return n;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityCommandBlock)) return;
        CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
        if (stack.hasDisplayName()) {
            commandblocklogic.setName(stack.getDisplayName());
        }
        if (worldIn.isRemote) return;
        commandblocklogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState();
        if ((meta & 1) > 0) {
            bl = true;
            return iBlockState.withProperty(TRIGGERED, bl);
        }
        bl = false;
        return iBlockState.withProperty(TRIGGERED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(TRIGGERED) == false) return i;
        i |= 1;
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TRIGGERED);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(TRIGGERED, false);
    }
}

