package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockOldLog
extends BlockLog {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create((String)"variant", BlockPlanks.EnumType.class, (Predicate)new /* Unavailable Anonymous Inner Class!! */);

    public BlockOldLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)BlockPlanks.EnumType.OAK).withProperty((IProperty)LOG_AXIS, (Comparable)BlockLog.EnumAxis.Y));
    }

    public MapColor getMapColor(IBlockState state) {
        BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue(VARIANT);
        switch (2.$SwitchMap$net$minecraft$block$BlockLog$EnumAxis[((BlockLog.EnumAxis)state.getValue((IProperty)LOG_AXIS)).ordinal()]) {
            default: {
                switch (2.$SwitchMap$net$minecraft$block$BlockPlanks$EnumType[blockplanks$enumtype.ordinal()]) {
                    default: {
                        return BlockPlanks.EnumType.SPRUCE.getMapColor();
                    }
                    case 2: {
                        return BlockPlanks.EnumType.DARK_OAK.getMapColor();
                    }
                    case 3: {
                        return MapColor.quartzColor;
                    }
                    case 4: 
                }
                return BlockPlanks.EnumType.SPRUCE.getMapColor();
            }
            case 4: 
        }
        return blockplanks$enumtype.getMapColor();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, (Comparable)BlockPlanks.EnumType.byMetadata((int)((meta & 3) % 4)));
        switch (meta & 0xC) {
            case 0: {
                iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, (Comparable)BlockLog.EnumAxis.Y);
                break;
            }
            case 4: {
                iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, (Comparable)BlockLog.EnumAxis.X);
                break;
            }
            case 8: {
                iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, (Comparable)BlockLog.EnumAxis.Z);
                break;
            }
            default: {
                iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, (Comparable)BlockLog.EnumAxis.NONE);
            }
        }
        return iblockstate;
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
        switch (2.$SwitchMap$net$minecraft$block$BlockLog$EnumAxis[((BlockLog.EnumAxis)state.getValue((IProperty)LOG_AXIS)).ordinal()]) {
            case 1: {
                i |= 4;
                break;
            }
            case 2: {
                i |= 8;
                break;
            }
            case 3: {
                i |= 0xC;
            }
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{VARIANT, LOG_AXIS});
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock((Block)this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata());
    }

    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }
}
