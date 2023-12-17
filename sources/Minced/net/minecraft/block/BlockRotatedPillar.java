// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.Rotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRotatedPillar extends Block
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS;
    
    protected BlockRotatedPillar(final Material materialIn) {
        super(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockRotatedPillar(final Material materialIn, final MapColor color) {
        super(materialIn, color);
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (state.getValue(BlockRotatedPillar.AXIS)) {
                    case X: {
                        return state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
                    }
                    case Z: {
                        return state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
                    }
                    default: {
                        return state;
                    }
                }
                break;
            }
            default: {
                return state;
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
        final int i = meta & 0xC;
        if (i == 4) {
            enumfacing$axis = EnumFacing.Axis.X;
        }
        else if (i == 8) {
            enumfacing$axis = EnumFacing.Axis.Z;
        }
        return this.getDefaultState().withProperty(BlockRotatedPillar.AXIS, enumfacing$axis);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        final EnumFacing.Axis enumfacing$axis = state.getValue(BlockRotatedPillar.AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            i |= 0x4;
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockRotatedPillar.AXIS });
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockRotatedPillar.AXIS, facing.getAxis());
    }
    
    static {
        AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    }
}
