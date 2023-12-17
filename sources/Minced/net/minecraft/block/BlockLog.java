// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockLog extends BlockRotatedPillar
{
    public static final PropertyEnum<EnumAxis> LOG_AXIS;
    
    public BlockLog() {
        super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0f);
        this.setSoundType(SoundType.WOOD);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = 4;
        final int j = 5;
        if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5))) {
            for (final BlockPos blockpos : BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4))) {
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getMaterial() == Material.LEAVES && !iblockstate.getValue((IProperty<Boolean>)BlockLeaves.CHECK_DECAY)) {
                    worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, true), 4);
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getStateFromMeta(meta).withProperty(BlockLog.LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (state.getValue(BlockLog.LOG_AXIS)) {
                    case X: {
                        return state.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z);
                    }
                    case Z: {
                        return state.withProperty(BlockLog.LOG_AXIS, EnumAxis.X);
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
    
    static {
        LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);
    }
    
    public enum EnumAxis implements IStringSerializable
    {
        X("x"), 
        Y("y"), 
        Z("z"), 
        NONE("none");
        
        private final String name;
        
        private EnumAxis(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumAxis fromFacingAxis(final EnumFacing.Axis axis) {
            switch (axis) {
                case X: {
                    return EnumAxis.X;
                }
                case Y: {
                    return EnumAxis.Y;
                }
                case Z: {
                    return EnumAxis.Z;
                }
                default: {
                    return EnumAxis.NONE;
                }
            }
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
