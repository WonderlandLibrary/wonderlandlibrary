// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockEndPortalFrame extends Block
{
    public static final PropertyDirection field_176508_a;
    public static final PropertyBool field_176507_b;
    private static final String __OBFID = "CL_00000237";
    
    public BlockEndPortalFrame() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockEndPortalFrame.field_176508_a, EnumFacing.NORTH).withProperty(BlockEndPortalFrame.field_176507_b, false));
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8125f, 1.0f);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8125f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        if (worldIn.getBlockState(pos).getValue(BlockEndPortalFrame.field_176507_b)) {
            this.setBlockBounds(0.3125f, 0.8125f, 0.3125f, 0.6875f, 1.0f, 0.6875f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        this.setBlockBoundsForItemRender();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockEndPortalFrame.field_176508_a, placer.func_174811_aO().getOpposite()).withProperty(BlockEndPortalFrame.field_176507_b, false);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(BlockEndPortalFrame.field_176507_b) ? 15 : 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockEndPortalFrame.field_176507_b, (meta & 0x4) != 0x0).withProperty(BlockEndPortalFrame.field_176508_a, EnumFacing.getHorizontal(meta & 0x3));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockEndPortalFrame.field_176508_a)).getHorizontalIndex();
        if (state.getValue(BlockEndPortalFrame.field_176507_b)) {
            var3 |= 0x4;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockEndPortalFrame.field_176508_a, BlockEndPortalFrame.field_176507_b });
    }
    
    static {
        field_176508_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
        field_176507_b = PropertyBool.create("eye");
    }
}
