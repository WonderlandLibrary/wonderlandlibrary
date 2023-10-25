/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
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

public class BlockNewLog
extends BlockLog {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>(){

        public boolean apply(BlockPlanks.EnumType enumType) {
            return enumType.getMetadata() >= 4;
        }
    });

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(VARIANT).getMetadata() - 4;
        switch ((BlockLog.EnumAxis)iBlockState.getValue(LOG_AXIS)) {
            case X: {
                n |= 4;
                break;
            }
            case Z: {
                n |= 8;
                break;
            }
            case NONE: {
                n |= 0xC;
            }
        }
        return n;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        list.add(new ItemStack(item, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        BlockPlanks.EnumType enumType = iBlockState.getValue(VARIANT);
        switch ((BlockLog.EnumAxis)iBlockState.getValue(LOG_AXIS)) {
            default: {
                switch (enumType) {
                    default: {
                        return MapColor.stoneColor;
                    }
                    case DARK_OAK: 
                }
                return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
            }
            case Y: 
        }
        return enumType.func_181070_c();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT, LOG_AXIS);
    }

    public BlockNewLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).getMetadata() - 4;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        IBlockState iBlockState = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((n & 3) + 4));
        switch (n & 0xC) {
            case 0: {
                iBlockState = iBlockState.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            }
            case 4: {
                iBlockState = iBlockState.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            }
            case 8: {
                iBlockState = iBlockState.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            }
            default: {
                iBlockState = iBlockState.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
            }
        }
        return iBlockState;
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        return new ItemStack(Item.getItemFromBlock(this), 1, iBlockState.getValue(VARIANT).getMetadata() - 4);
    }
}

