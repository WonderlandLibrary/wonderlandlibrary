// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.IdentityHashMap;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import java.util.Map;

public class BlockAir extends Block
{
    private static Map mapOriginalOpacity;
    
    protected BlockAir() {
        super(Material.AIR);
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockAir.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return false;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
    
    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    public static void setLightOpacity(final Block p_setLightOpacity_0_, final int p_setLightOpacity_1_) {
        if (!BlockAir.mapOriginalOpacity.containsKey(p_setLightOpacity_0_)) {
            BlockAir.mapOriginalOpacity.put(p_setLightOpacity_0_, p_setLightOpacity_0_.lightOpacity);
        }
        p_setLightOpacity_0_.lightOpacity = p_setLightOpacity_1_;
    }
    
    public static void restoreLightOpacity(final Block p_restoreLightOpacity_0_) {
        if (BlockAir.mapOriginalOpacity.containsKey(p_restoreLightOpacity_0_)) {
            final int i = BlockAir.mapOriginalOpacity.get(p_restoreLightOpacity_0_);
            setLightOpacity(p_restoreLightOpacity_0_, i);
        }
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        BlockAir.mapOriginalOpacity = new IdentityHashMap();
    }
}
