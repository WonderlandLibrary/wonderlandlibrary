// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.IdentityHashMap;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import java.util.Map;

public class BlockAir extends Block
{
    private static Map mapOriginalOpacity;
    
    protected BlockAir() {
        super(Material.air);
    }
    
    @Override
    public int getRenderType() {
        return -1;
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
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return false;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return true;
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
    
    static {
        BlockAir.mapOriginalOpacity = new IdentityHashMap();
    }
}
