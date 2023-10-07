// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockSign extends BlockContainer
{
    private static final String __OBFID = "CL_00000306";
    
    protected BlockSign() {
        super(Material.wood);
        final float var1 = 0.25f;
        final float var2 = 1.0f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, var2, 0.5f + var1);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntitySign();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.sign;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.sign;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity var9 = worldIn.getTileEntity(pos);
        return var9 instanceof TileEntitySign && ((TileEntitySign)var9).func_174882_b(playerIn);
    }
}
