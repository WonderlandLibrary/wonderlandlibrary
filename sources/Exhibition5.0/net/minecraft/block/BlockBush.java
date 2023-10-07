// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockBush extends Block
{
    private static final String __OBFID = "CL_00000208";
    
    protected BlockBush(final Material materialIn) {
        super(materialIn);
        this.setTickRandomly(true);
        final float var2 = 0.2f;
        this.setBlockBounds(0.5f - var2, 0.0f, 0.5f - var2, 0.5f + var2, var2 * 3.0f, 0.5f + var2);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    protected BlockBush() {
        this(Material.plants);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canPlaceBlockOn(worldIn.getBlockState(pos.offsetDown()).getBlock());
    }
    
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.grass || ground == Blocks.dirt || ground == Blocks.farmland;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        this.func_176475_e(worldIn, pos, state);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.func_176475_e(worldIn, pos, state);
    }
    
    protected void func_176475_e(final World worldIn, final BlockPos p_176475_2_, final IBlockState p_176475_3_) {
        if (!this.canBlockStay(worldIn, p_176475_2_, p_176475_3_)) {
            this.dropBlockAsItem(worldIn, p_176475_2_, p_176475_3_, 0);
            worldIn.setBlockState(p_176475_2_, Blocks.air.getDefaultState(), 3);
        }
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos p_180671_2_, final IBlockState p_180671_3_) {
        return this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
