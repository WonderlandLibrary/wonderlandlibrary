package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockQuartz
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create((String)"variant", EnumType.class);

    public BlockQuartz() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (meta == EnumType.LINES_Y.getMetadata()) {
            switch (1.$SwitchMap$net$minecraft$util$EnumFacing$Axis[facing.getAxis().ordinal()]) {
                case 1: {
                    return this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.LINES_Z);
                }
                case 2: {
                    return this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.LINES_X);
                }
            }
            return this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.LINES_Y);
        }
        return meta == EnumType.CHISELED.getMetadata() ? this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.CHISELED) : this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.DEFAULT);
    }

    public int damageDropped(IBlockState state) {
        EnumType blockquartz$enumtype = (EnumType)state.getValue(VARIANT);
        return blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z ? blockquartz$enumtype.getMetadata() : EnumType.LINES_Y.getMetadata();
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        EnumType blockquartz$enumtype = (EnumType)state.getValue(VARIANT);
        return blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock((Block)this), 1, EnumType.LINES_Y.getMetadata());
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, EnumType.DEFAULT.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, EnumType.CHISELED.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, EnumType.LINES_Y.getMetadata()));
    }

    public MapColor getMapColor(IBlockState state) {
        return MapColor.quartzColor;
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
