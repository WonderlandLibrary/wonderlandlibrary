package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockDirt
extends Block {
    public static final PropertyEnum<DirtType> VARIANT = PropertyEnum.create((String)"variant", DirtType.class);
    public static final PropertyBool SNOWY = PropertyBool.create((String)"snowy");

    protected BlockDirt() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)DirtType.DIRT).withProperty((IProperty)SNOWY, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public MapColor getMapColor(IBlockState state) {
        return ((DirtType)state.getValue(VARIANT)).func_181066_d();
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(VARIANT) == DirtType.PODZOL) {
            Block block = worldIn.getBlockState(pos.up()).getBlock();
            state = state.withProperty((IProperty)SNOWY, (Comparable)Boolean.valueOf((block == Blocks.snow || block == Blocks.snow_layer ? 1 : 0) != 0));
        }
        return state;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack((Block)this, 1, DirtType.DIRT.getMetadata()));
        list.add((Object)new ItemStack((Block)this, 1, DirtType.COARSE_DIRT.getMetadata()));
        list.add((Object)new ItemStack((Block)this, 1, DirtType.PODZOL.getMetadata()));
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() != this ? 0 : ((DirtType)iblockstate.getValue(VARIANT)).getMetadata();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)DirtType.byMetadata((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((DirtType)state.getValue(VARIANT)).getMetadata();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{VARIANT, SNOWY});
    }

    public int damageDropped(IBlockState state) {
        DirtType blockdirt$dirttype = (DirtType)state.getValue(VARIANT);
        if (blockdirt$dirttype == DirtType.PODZOL) {
            blockdirt$dirttype = DirtType.DIRT;
        }
        return blockdirt$dirttype.getMetadata();
    }
}
