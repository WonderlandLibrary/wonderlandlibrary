// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockWoodSlab extends BlockSlab
{
    public static final PropertyEnum field_176557_b;
    private static final String __OBFID = "CL_00000337";
    
    public BlockWoodSlab() {
        super(Material.wood);
        IBlockState var1 = this.blockState.getBaseState();
        if (!this.isDouble()) {
            var1 = var1.withProperty(BlockSlab.HALF_PROP, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(var1.withProperty(BlockWoodSlab.field_176557_b, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }
    
    @Override
    public String getFullSlabName(final int p_150002_1_) {
        return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.func_176837_a(p_150002_1_).func_176840_c();
    }
    
    @Override
    public IProperty func_176551_l() {
        return BlockWoodSlab.field_176557_b;
    }
    
    @Override
    public Object func_176553_a(final ItemStack p_176553_1_) {
        return BlockPlanks.EnumType.func_176837_a(p_176553_1_.getMetadata() & 0x7);
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab)) {
            for (final BlockPlanks.EnumType var7 : BlockPlanks.EnumType.values()) {
                list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(BlockWoodSlab.field_176557_b, BlockPlanks.EnumType.func_176837_a(meta & 0x7));
        if (!this.isDouble()) {
            var2 = var2.withProperty(BlockSlab.HALF_PROP, ((meta & 0x8) == 0x0) ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(BlockWoodSlab.field_176557_b)).func_176839_a();
        if (!this.isDouble() && state.getValue(BlockSlab.HALF_PROP) == EnumBlockHalf.TOP) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, new IProperty[] { BlockWoodSlab.field_176557_b }) : new BlockState(this, new IProperty[] { BlockSlab.HALF_PROP, BlockWoodSlab.field_176557_b });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(BlockWoodSlab.field_176557_b)).func_176839_a();
    }
    
    static {
        field_176557_b = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
    }
}
