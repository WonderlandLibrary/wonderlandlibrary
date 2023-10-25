package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockHugeMushroom
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create((String)"variant", EnumType.class);
    private final Block smallBlock;

    public BlockHugeMushroom(Material p_i46392_1_, MapColor p_i46392_2_, Block p_i46392_3_) {
        super(p_i46392_1_, p_i46392_2_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)EnumType.ALL_OUTSIDE));
        this.smallBlock = p_i46392_3_;
    }

    public int quantityDropped(Random random) {
        return Math.max((int)0, (int)(random.nextInt(10) - 7));
    }

    public MapColor getMapColor(IBlockState state) {
        switch (1.$SwitchMap$net$minecraft$block$BlockHugeMushroom$EnumType[((EnumType)state.getValue(VARIANT)).ordinal()]) {
            case 1: {
                return MapColor.clothColor;
            }
            case 2: {
                return MapColor.sandColor;
            }
            case 3: {
                return MapColor.sandColor;
            }
        }
        return super.getMapColor(state);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock((Block)this.smallBlock);
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock((Block)this.smallBlock);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.byMetadata((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{VARIANT});
    }
}
