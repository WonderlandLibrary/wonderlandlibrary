// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCactus extends Block
{
    public static final PropertyInteger AGE_PROP;
    private static final String __OBFID = "CL_00000210";
    
    protected BlockCactus() {
        super(Material.cactus);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCactus.AGE_PROP, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final BlockPos var5 = pos.offsetUp();
        if (worldIn.isAirBlock(var5)) {
            int var6;
            for (var6 = 1; worldIn.getBlockState(pos.offsetDown(var6)).getBlock() == this; ++var6) {}
            if (var6 < 3) {
                final int var7 = (int)state.getValue(BlockCactus.AGE_PROP);
                if (var7 == 15) {
                    worldIn.setBlockState(var5, this.getDefaultState());
                    final IBlockState var8 = state.withProperty(BlockCactus.AGE_PROP, 0);
                    worldIn.setBlockState(pos, var8, 4);
                    this.onNeighborBlockChange(worldIn, var5, var8, this);
                }
                else {
                    worldIn.setBlockState(pos, state.withProperty(BlockCactus.AGE_PROP, var7 + 1), 4);
                }
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final float var4 = 0.0625f;
        return new AxisAlignedBB(pos.getX() + var4, pos.getY(), pos.getZ() + var4, pos.getX() + 1 - var4, pos.getY() + 1 - var4, pos.getZ() + 1 - var4);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        final float var3 = 0.0625f;
        return new AxisAlignedBB(pos.getX() + var3, pos.getY(), pos.getZ() + var3, pos.getX() + 1 - var3, pos.getY() + 1, pos.getZ() + 1 - var3);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos p_176586_2_) {
        for (final EnumFacing var4 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(p_176586_2_.offset(var4)).getBlock().getMaterial().isSolid()) {
                return false;
            }
        }
        final Block var5 = worldIn.getBlockState(p_176586_2_.offsetDown()).getBlock();
        return var5 == Blocks.cactus || var5 == Blocks.sand;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.cactus, 1.0f);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCactus.AGE_PROP, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockCactus.AGE_PROP);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCactus.AGE_PROP });
    }
    
    static {
        AGE_PROP = PropertyInteger.create("age", 0, 15);
    }
}
