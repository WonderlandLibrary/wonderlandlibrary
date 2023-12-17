// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockSoulSand extends Block
{
    protected static final AxisAlignedBB SOUL_SAND_AABB;
    
    public BlockSoulSand() {
        super(Material.SAND, MapColor.BROWN);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockSoulSand.SOUL_SAND_AABB;
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        entityIn.motionX *= 0.4;
        entityIn.motionZ *= 0.4;
    }
    
    static {
        SOUL_SAND_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0);
    }
}
