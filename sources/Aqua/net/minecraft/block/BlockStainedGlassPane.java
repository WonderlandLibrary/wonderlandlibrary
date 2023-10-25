package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockStainedGlassPane
extends BlockPane {
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create((String)"color", EnumDyeColor.class);

    public BlockStainedGlassPane() {
        super(Material.glass, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)false)).withProperty(COLOR, (Comparable)EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            list.add((Object)new ItemStack(itemIn, 1, i));
        }
    }

    public MapColor getMapColor(IBlockState state) {
        return ((EnumDyeColor)state.getValue(COLOR)).getMapColor();
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, (Comparable)EnumDyeColor.byMetadata((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{NORTH, EAST, WEST, SOUTH, COLOR});
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.updateColorAsync((World)worldIn, (BlockPos)pos);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.updateColorAsync((World)worldIn, (BlockPos)pos);
        }
    }
}
