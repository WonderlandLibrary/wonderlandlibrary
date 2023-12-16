// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import optifine.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import java.util.IdentityHashMap;
import java.util.Map;

public class BlockLeavesBase extends Block
{
    protected boolean field_150121_P;
    private static final String __OBFID = "CL_00000326";
    private static Map mapOriginalOpacity;
    
    static {
        BlockLeavesBase.mapOriginalOpacity = new IdentityHashMap();
    }
    
    protected BlockLeavesBase(final Material materialIn, final boolean fancyGraphics) {
        super(materialIn);
        this.field_150121_P = fancyGraphics;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return (!Config.isCullFacesLeaves() || worldIn.getBlockState(pos).getBlock() != this) && super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    public static void setLightOpacity(final Block block, final int opacity) {
        if (!BlockLeavesBase.mapOriginalOpacity.containsKey(block)) {
            BlockLeavesBase.mapOriginalOpacity.put(block, block.getLightOpacity());
        }
        block.setLightOpacity(opacity);
    }
    
    public static void restoreLightOpacity(final Block block) {
        if (BlockLeavesBase.mapOriginalOpacity.containsKey(block)) {
            final int opacity = BlockLeavesBase.mapOriginalOpacity.get(block);
            setLightOpacity(block, opacity);
        }
    }
}
