// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockPane extends Block
{
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    private final boolean field_150099_b;
    private static final String __OBFID = "CL_00000322";
    
    protected BlockPane(final Material p_i45675_1_, final boolean p_i45675_2_) {
        super(p_i45675_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPane.NORTH, false).withProperty(BlockPane.EAST, false).withProperty(BlockPane.SOUTH, false).withProperty(BlockPane.WEST, false));
        this.field_150099_b = p_i45675_2_;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockPane.NORTH, this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetNorth()).getBlock())).withProperty(BlockPane.SOUTH, this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetSouth()).getBlock())).withProperty(BlockPane.WEST, this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetWest()).getBlock())).withProperty(BlockPane.EAST, this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetEast()).getBlock()));
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return this.field_150099_b ? super.getItemDropped(state, rand, fortune) : null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock() != this && super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        final boolean var7 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetNorth()).getBlock());
        final boolean var8 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetSouth()).getBlock());
        final boolean var9 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetWest()).getBlock());
        final boolean var10 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.offsetEast()).getBlock());
        if ((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
            if (var9) {
                this.setBlockBounds(0.0f, 0.0f, 0.4375f, 0.5f, 1.0f, 0.5625f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
            else if (var10) {
                this.setBlockBounds(0.5f, 0.0f, 0.4375f, 1.0f, 1.0f, 0.5625f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.4375f, 1.0f, 1.0f, 0.5625f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        if ((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
            if (var7) {
                this.setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1.0f, 0.5f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
            else if (var8) {
                this.setBlockBounds(0.4375f, 0.0f, 0.5f, 0.5625f, 1.0f, 1.0f);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        else {
            this.setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1.0f, 1.0f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        float var3 = 0.4375f;
        float var4 = 0.5625f;
        float var5 = 0.4375f;
        float var6 = 0.5625f;
        final boolean var7 = this.canPaneConnectToBlock(access.getBlockState(pos.offsetNorth()).getBlock());
        final boolean var8 = this.canPaneConnectToBlock(access.getBlockState(pos.offsetSouth()).getBlock());
        final boolean var9 = this.canPaneConnectToBlock(access.getBlockState(pos.offsetWest()).getBlock());
        final boolean var10 = this.canPaneConnectToBlock(access.getBlockState(pos.offsetEast()).getBlock());
        if ((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
            if (var9) {
                var3 = 0.0f;
            }
            else if (var10) {
                var4 = 1.0f;
            }
        }
        else {
            var3 = 0.0f;
            var4 = 1.0f;
        }
        if ((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
            if (var7) {
                var5 = 0.0f;
            }
            else if (var8) {
                var6 = 1.0f;
            }
        }
        else {
            var5 = 0.0f;
            var6 = 1.0f;
        }
        this.setBlockBounds(var3, 0.0f, var5, var4, 1.0f, var6);
    }
    
    public final boolean canPaneConnectToBlock(final Block p_150098_1_) {
        return p_150098_1_.isFullBlock() || p_150098_1_ == this || p_150098_1_ == Blocks.glass || p_150098_1_ == Blocks.stained_glass || p_150098_1_ == Blocks.stained_glass_pane || p_150098_1_ instanceof BlockPane;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPane.NORTH, BlockPane.EAST, BlockPane.WEST, BlockPane.SOUTH });
    }
    
    static {
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }
}
