package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockStone
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create((String)"variant", EnumType.class);

    public BlockStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)EnumType.STONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)(this.getUnlocalizedName() + "." + EnumType.STONE.getUnlocalizedName() + ".name"));
    }

    public MapColor getMapColor(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).func_181072_c();
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(VARIANT) == EnumType.STONE ? Item.getItemFromBlock((Block)Blocks.cobblestone) : Item.getItemFromBlock((Block)Blocks.stone);
    }

    public int damageDropped(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumType blockstone$enumtype : EnumType.values()) {
            list.add((Object)new ItemStack(itemIn, 1, blockstone$enumtype.getMetadata()));
        }
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
