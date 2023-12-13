// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemLead;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockFence extends Block
{
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    private static final String __OBFID = "CL_00000242";
    
    public BlockFence(final Material p_i45721_1_) {
        super(p_i45721_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFence.NORTH, false).withProperty(BlockFence.EAST, false).withProperty(BlockFence.SOUTH, false).withProperty(BlockFence.WEST, false));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        final boolean var7 = this.func_176524_e(worldIn, pos.offsetNorth());
        final boolean var8 = this.func_176524_e(worldIn, pos.offsetSouth());
        final boolean var9 = this.func_176524_e(worldIn, pos.offsetWest());
        final boolean var10 = this.func_176524_e(worldIn, pos.offsetEast());
        float var11 = 0.375f;
        float var12 = 0.625f;
        float var13 = 0.375f;
        float var14 = 0.625f;
        if (var7) {
            var13 = 0.0f;
        }
        if (var8) {
            var14 = 1.0f;
        }
        if (var7 || var8) {
            this.setBlockBounds(var11, 0.0f, var13, var12, 1.5f, var14);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        var13 = 0.375f;
        var14 = 0.625f;
        if (var9) {
            var11 = 0.0f;
        }
        if (var10) {
            var12 = 1.0f;
        }
        if (var9 || var10 || (!var7 && !var8)) {
            this.setBlockBounds(var11, 0.0f, var13, var12, 1.5f, var14);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        if (var7) {
            var13 = 0.0f;
        }
        if (var8) {
            var14 = 1.0f;
        }
        this.setBlockBounds(var11, 0.0f, var13, var12, 1.0f, var14);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final boolean var3 = this.func_176524_e(access, pos.offsetNorth());
        final boolean var4 = this.func_176524_e(access, pos.offsetSouth());
        final boolean var5 = this.func_176524_e(access, pos.offsetWest());
        final boolean var6 = this.func_176524_e(access, pos.offsetEast());
        float var7 = 0.375f;
        float var8 = 0.625f;
        float var9 = 0.375f;
        float var10 = 0.625f;
        if (var3) {
            var9 = 0.0f;
        }
        if (var4) {
            var10 = 1.0f;
        }
        if (var5) {
            var7 = 0.0f;
        }
        if (var6) {
            var8 = 1.0f;
        }
        this.setBlockBounds(var7, 0.0f, var9, var8, 1.0f, var10);
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
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return false;
    }
    
    public boolean func_176524_e(final IBlockAccess p_176524_1_, final BlockPos p_176524_2_) {
        final Block var3 = p_176524_1_.getBlockState(p_176524_2_).getBlock();
        return var3 != Blocks.barrier && ((var3 instanceof BlockFence && var3.blockMaterial == this.blockMaterial) || var3 instanceof BlockFenceGate || (var3.blockMaterial.isOpaque() && var3.isFullCube() && var3.blockMaterial != Material.gourd));
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        return worldIn.isRemote || ItemLead.func_180618_a(playerIn, worldIn, pos);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockFence.NORTH, this.func_176524_e(worldIn, pos.offsetNorth())).withProperty(BlockFence.EAST, this.func_176524_e(worldIn, pos.offsetEast())).withProperty(BlockFence.SOUTH, this.func_176524_e(worldIn, pos.offsetSouth())).withProperty(BlockFence.WEST, this.func_176524_e(worldIn, pos.offsetWest()));
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFence.NORTH, BlockFence.EAST, BlockFence.WEST, BlockFence.SOUTH });
    }
    
    static {
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }
}
