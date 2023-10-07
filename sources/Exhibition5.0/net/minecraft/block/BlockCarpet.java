// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockCarpet extends Block
{
    public static final PropertyEnum field_176330_a;
    private static final String __OBFID = "CL_00000338";
    
    protected BlockCarpet() {
        super(Material.carpet);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCarpet.field_176330_a, EnumDyeColor.WHITE));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsFromMeta(0);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsFromMeta(0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.setBlockBoundsFromMeta(0);
    }
    
    protected void setBlockBoundsFromMeta(final int meta) {
        final byte var2 = 0;
        final float var3 = 1 * (1 + var2) / 16.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, var3, 1.0f);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.checkAndDropBlock(worldIn, pos, state);
    }
    
    private boolean checkAndDropBlock(final World worldIn, final BlockPos p_176328_2_, final IBlockState p_176328_3_) {
        if (!this.canBlockStay(worldIn, p_176328_2_)) {
            this.dropBlockAsItem(worldIn, p_176328_2_, p_176328_3_, 0);
            worldIn.setBlockToAir(p_176328_2_);
            return false;
        }
        return true;
    }
    
    private boolean canBlockStay(final World worldIn, final BlockPos p_176329_2_) {
        return !worldIn.isAirBlock(p_176329_2_.offsetDown());
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumDyeColor)state.getValue(BlockCarpet.field_176330_a)).func_176765_a();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (int var4 = 0; var4 < 16; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCarpet.field_176330_a, EnumDyeColor.func_176764_b(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumDyeColor)state.getValue(BlockCarpet.field_176330_a)).func_176765_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCarpet.field_176330_a });
    }
    
    static {
        field_176330_a = PropertyEnum.create("color", EnumDyeColor.class);
    }
}
