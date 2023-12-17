// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.item.EntityBoat;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockLilyPad extends BlockBush
{
    protected static final AxisAlignedBB LILY_PAD_AABB;
    
    protected BlockLilyPad() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!(entityIn instanceof EntityBoat)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockLilyPad.LILY_PAD_AABB);
        }
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        super.onEntityCollision(worldIn, pos, state, entityIn);
        if (entityIn instanceof EntityBoat) {
            worldIn.destroyBlock(new BlockPos(pos), true);
        }
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockLilyPad.LILY_PAD_AABB;
    }
    
    @Override
    protected boolean canSustainBush(final IBlockState state) {
        return state.getBlock() == Blocks.WATER || state.getMaterial() == Material.ICE;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.down());
            final Material material = iblockstate.getMaterial();
            return (material == Material.WATER && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) || material == Material.ICE;
        }
        return false;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    static {
        LILY_PAD_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.09375, 0.9375);
    }
}
