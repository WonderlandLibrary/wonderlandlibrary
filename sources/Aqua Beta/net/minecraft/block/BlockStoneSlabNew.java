package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public abstract class BlockStoneSlabNew
extends BlockSlab {
    public static final PropertyBool SEAMLESS = PropertyBool.create((String)"seamless");
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create((String)"variant", EnumType.class);

    public BlockStoneSlabNew() {
        super(Material.rock);
        IBlockState iblockstate = this.blockState.getBaseState();
        iblockstate = this.isDouble() ? iblockstate.withProperty((IProperty)SEAMLESS, (Comparable)Boolean.valueOf((boolean)false)) : iblockstate.withProperty((IProperty)HALF, (Comparable)BlockSlab.EnumBlockHalf.BOTTOM);
        this.setDefaultState(iblockstate.withProperty(VARIANT, (Comparable)EnumType.RED_SANDSTONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)(this.getUnlocalizedName() + ".red_sandstone.name"));
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock((Block)Blocks.stone_slab2);
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock((Block)Blocks.stone_slab2);
    }

    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName() + "." + EnumType.byMetadata((int)meta).getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    public Object getVariant(ItemStack stack) {
        return EnumType.byMetadata((int)(stack.getMetadata() & 7));
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (itemIn != Item.getItemFromBlock((Block)Blocks.double_stone_slab2)) {
            for (EnumType blockstoneslabnew$enumtype : EnumType.values()) {
                list.add((Object)new ItemStack(itemIn, 1, blockstoneslabnew$enumtype.getMetadata()));
            }
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, (Comparable)EnumType.byMetadata((int)(meta & 7)));
        iblockstate = this.isDouble() ? iblockstate.withProperty((IProperty)SEAMLESS, (Comparable)Boolean.valueOf(((meta & 8) != 0 ? 1 : 0) != 0)) : iblockstate.withProperty((IProperty)HALF, (Comparable)((meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP));
        return iblockstate;
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumType)state.getValue(VARIANT)).getMetadata();
        if (this.isDouble()) {
            if (((Boolean)state.getValue((IProperty)SEAMLESS)).booleanValue()) {
                i |= 8;
            }
        } else if (state.getValue((IProperty)HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState((Block)this, new IProperty[]{SEAMLESS, VARIANT}) : new BlockState((Block)this, new IProperty[]{HALF, VARIANT});
    }

    public MapColor getMapColor(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).func_181068_c();
    }

    public int damageDropped(IBlockState state) {
        return ((EnumType)state.getValue(VARIANT)).getMetadata();
    }
}
