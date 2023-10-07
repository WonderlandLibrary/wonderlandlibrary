// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;

public class BlockHay extends BlockRotatedPillar
{
    private static final String __OBFID = "CL_00000256";
    
    public BlockHay() {
        super(Material.grass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRotatedPillar.field_176298_M, EnumFacing.Axis.Y));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing.Axis var2 = EnumFacing.Axis.Y;
        final int var3 = meta & 0xC;
        if (var3 == 4) {
            var2 = EnumFacing.Axis.X;
        }
        else if (var3 == 8) {
            var2 = EnumFacing.Axis.Z;
        }
        return this.getDefaultState().withProperty(BlockRotatedPillar.field_176298_M, var2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        final EnumFacing.Axis var3 = (EnumFacing.Axis)state.getValue(BlockRotatedPillar.field_176298_M);
        if (var3 == EnumFacing.Axis.X) {
            var2 |= 0x4;
        }
        else if (var3 == EnumFacing.Axis.Z) {
            var2 |= 0x8;
        }
        return var2;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRotatedPillar.field_176298_M });
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockRotatedPillar.field_176298_M, facing.getAxis());
    }
}
