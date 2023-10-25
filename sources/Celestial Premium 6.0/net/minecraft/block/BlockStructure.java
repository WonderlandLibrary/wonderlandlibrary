/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStructure
extends BlockContainer {
    public static final PropertyEnum<TileEntityStructure.Mode> MODE = PropertyEnum.create("mode", TileEntityStructure.Mode.class);

    public BlockStructure() {
        super(Material.IRON, MapColor.SILVER);
        this.setDefaultState(this.blockState.getBaseState());
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityStructure();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityStructure ? ((TileEntityStructure)tileentity).usedBy(playerIn) : false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityStructure) {
            TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
            tileentitystructure.createdBy(placer);
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(MODE, TileEntityStructure.Mode.DATA);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MODE, TileEntityStructure.Mode.getById(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MODE).getModeId();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, MODE);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityStructure) {
            TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
            boolean flag = worldIn.isBlockPowered(pos);
            boolean flag1 = tileentitystructure.isPowered();
            if (flag && !flag1) {
                tileentitystructure.setPowered(true);
                this.trigger(tileentitystructure);
            } else if (!flag && flag1) {
                tileentitystructure.setPowered(false);
            }
        }
    }

    private void trigger(TileEntityStructure p_189874_1_) {
        switch (p_189874_1_.getMode()) {
            case SAVE: {
                p_189874_1_.save(false);
                break;
            }
            case LOAD: {
                p_189874_1_.load(false);
                break;
            }
            case CORNER: {
                p_189874_1_.unloadStructure();
            }
        }
    }
}

