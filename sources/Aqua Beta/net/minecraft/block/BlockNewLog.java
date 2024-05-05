package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockNewLog
extends BlockLog {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create((String)"variant", BlockPlanks.EnumType.class, (Predicate)new /* Unavailable Anonymous Inner Class!! */);

    public BlockNewLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)BlockPlanks.EnumType.ACACIA).withProperty((IProperty)LOG_AXIS, (Comparable)BlockLog.EnumAxis.Y));
    }

    public MapColor getMapColor(IBlockState state) {
        BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue(VARIANT);
        switch (2.$SwitchMap$net$minecraft$block$BlockLog$EnumAxis[((BlockLog.EnumAxis)state.getValue((IProperty)LOG_AXIS)).ordinal()]) {
            default: {
                switch (2.$SwitchMap$net$minecraft$block$BlockPlanks$EnumType[blockplanks$enumtype.ordinal()]) {
                    default: {
                        return MapColor.stoneColor;
                    }
                    case 2: 
                }
                return BlockPlanks.EnumType.DARK_OAK.getMapColor();
            }
            case 4: 
        }
        return blockplanks$enumtype.getMapColor();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, (Comparable)BlockPlanks.EnumType.byMetadata((int)((meta & 3) + 4)));
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
        i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
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
        return new ItemStack(Item.getItemFromBlock((Block)this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4);
    }

    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
    }
}
