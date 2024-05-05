/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockOldLog
extends BlockLog {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>(){

        @Override
        public boolean apply(BlockPlanks.EnumType p_apply_1_) {
            if (p_apply_1_.getMetadata() >= 4) return false;
            return true;
        }
    });

    public BlockOldLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        BlockPlanks.EnumType blockplanks$enumtype = state.getValue(VARIANT);
        switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
            default: {
                switch (2.$SwitchMap$net$minecraft$block$BlockPlanks$EnumType[blockplanks$enumtype.ordinal()]) {
                    default: {
                        return BlockPlanks.EnumType.SPRUCE.func_181070_c();
                    }
                    case 2: {
                        return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
                    }
                    case 3: {
                        return MapColor.quartzColor;
                    }
                    case 4: 
                }
                return BlockPlanks.EnumType.SPRUCE.func_181070_c();
            }
            case Y: 
        }
        return blockplanks$enumtype.func_181070_c();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((meta & 3) % 4));
        switch (meta & 0xC) {
            case 0: {
                return iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
            }
            case 4: {
                return iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
            }
            case 8: {
                return iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
            }
        }
        return iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(VARIANT).getMetadata();
        switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
            case X: {
                return i |= 4;
            }
            case Z: {
                return i |= 8;
            }
            case NONE: {
                i |= 0xC;
                return i;
            }
        }
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT, LOG_AXIS);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).getMetadata());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }
}

