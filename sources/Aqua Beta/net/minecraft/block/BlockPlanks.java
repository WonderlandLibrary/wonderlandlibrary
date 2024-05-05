package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockPlanks
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create((String)"variant", EnumType.class);

    public BlockPlanks() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int damageDropped(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumType blockplanks$enumtype : EnumType.values()) {
            list.add((Object)new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.byMetadata((int)meta));
    }

    public MapColor getMapColor(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMapColor();
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{VARIANT});
    }
}
