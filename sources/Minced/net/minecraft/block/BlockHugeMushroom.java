// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockHugeMushroom extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    private final Block smallBlock;
    
    public BlockHugeMushroom(final Material materialIn, final MapColor color, final Block smallBlockIn) {
        super(materialIn, color);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHugeMushroom.VARIANT, EnumType.ALL_OUTSIDE));
        this.smallBlock = smallBlockIn;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        switch (state.getValue(BlockHugeMushroom.VARIANT)) {
            case ALL_STEM: {
                return MapColor.CLOTH;
            }
            case ALL_INSIDE: {
                return MapColor.SAND;
            }
            case STEM: {
                return MapColor.SAND;
            }
            default: {
                return super.getMapColor(state, worldIn, pos);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(this.smallBlock);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this.smallBlock);
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockHugeMushroom.VARIANT).getMetadata();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        Label_0393: {
            switch (rot) {
                case CLOCKWISE_180: {
                    switch (state.getValue(BlockHugeMushroom.VARIANT)) {
                        case STEM: {
                            break Label_0393;
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_EAST);
                        }
                        case NORTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_WEST);
                        }
                        case WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.EAST);
                        }
                        case EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_EAST);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_WEST);
                        }
                        default: {
                            return state;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (state.getValue(BlockHugeMushroom.VARIANT)) {
                        case STEM: {
                            break Label_0393;
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_WEST);
                        }
                        case NORTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_WEST);
                        }
                        case WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH);
                        }
                        case EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_EAST);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.EAST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_EAST);
                        }
                        default: {
                            return state;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (state.getValue(BlockHugeMushroom.VARIANT)) {
                        case STEM: {
                            break Label_0393;
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_EAST);
                        }
                        case NORTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_EAST);
                        }
                        case WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH);
                        }
                        case EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_WEST);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.WEST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_WEST);
                        }
                        default: {
                            return state;
                        }
                    }
                    break;
                }
            }
        }
        return state;
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        final EnumType blockhugemushroom$enumtype = state.getValue(BlockHugeMushroom.VARIANT);
        Label_0325: {
            switch (mirrorIn) {
                case LEFT_RIGHT: {
                    switch (blockhugemushroom$enumtype) {
                        case NORTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_WEST);
                        }
                        case NORTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_EAST);
                        }
                        default: {
                            return super.withMirror(state, mirrorIn);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_WEST);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_EAST);
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    switch (blockhugemushroom$enumtype) {
                        case NORTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_EAST);
                        }
                        default: {
                            break Label_0325;
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_WEST);
                        }
                        case WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.EAST);
                        }
                        case EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_EAST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_WEST);
                        }
                    }
                    break;
                }
            }
        }
        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockHugeMushroom.VARIANT });
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        NORTH_WEST(1, "north_west"), 
        NORTH(2, "north"), 
        NORTH_EAST(3, "north_east"), 
        WEST(4, "west"), 
        CENTER(5, "center"), 
        EAST(6, "east"), 
        SOUTH_WEST(7, "south_west"), 
        SOUTH(8, "south"), 
        SOUTH_EAST(9, "south_east"), 
        STEM(10, "stem"), 
        ALL_INSIDE(0, "all_inside"), 
        ALL_OUTSIDE(14, "all_outside"), 
        ALL_STEM(15, "all_stem");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        
        private EnumType(final int meta, final String name) {
            this.meta = meta;
            this.name = name;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            final EnumType blockhugemushroom$enumtype = EnumType.META_LOOKUP[meta];
            return (blockhugemushroom$enumtype == null) ? EnumType.META_LOOKUP[0] : blockhugemushroom$enumtype;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        static {
            META_LOOKUP = new EnumType[16];
            for (final EnumType blockhugemushroom$enumtype : values()) {
                EnumType.META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype;
            }
        }
    }
}
