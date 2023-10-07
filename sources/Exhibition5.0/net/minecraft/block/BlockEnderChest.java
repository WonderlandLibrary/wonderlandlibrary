// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public class BlockEnderChest extends BlockContainer
{
    public static final PropertyDirection field_176437_a;
    private static final String __OBFID = "CL_00000238";
    
    protected BlockEnderChest() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockEnderChest.field_176437_a, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
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
    public int getRenderType() {
        return 2;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.obsidian);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 8;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockEnderChest.field_176437_a, placer.func_174811_aO().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(BlockEnderChest.field_176437_a, placer.func_174811_aO().getOpposite()), 2);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final InventoryEnderChest var9 = playerIn.getInventoryEnderChest();
        final TileEntity var10 = worldIn.getTileEntity(pos);
        if (var9 == null || !(var10 instanceof TileEntityEnderChest)) {
            return true;
        }
        if (worldIn.getBlockState(pos.offsetUp()).getBlock().isNormalCube()) {
            return true;
        }
        if (worldIn.isRemote) {
            return true;
        }
        var9.setChestTileEntity((TileEntityEnderChest)var10);
        playerIn.displayGUIChest(var9);
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityEnderChest();
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        for (int var5 = 0; var5 < 3; ++var5) {
            final int var6 = rand.nextInt(2) * 2 - 1;
            final int var7 = rand.nextInt(2) * 2 - 1;
            final double var8 = pos.getX() + 0.5 + 0.25 * var6;
            final double var9 = pos.getY() + rand.nextFloat();
            final double var10 = pos.getZ() + 0.5 + 0.25 * var7;
            final double var11 = rand.nextFloat() * var6;
            final double var12 = (rand.nextFloat() - 0.5) * 0.125;
            final double var13 = rand.nextFloat() * var7;
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, var8, var9, var10, var11, var12, var13, new int[0]);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(BlockEnderChest.field_176437_a, var2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumFacing)state.getValue(BlockEnderChest.field_176437_a)).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockEnderChest.field_176437_a });
    }
    
    static {
        field_176437_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    }
}
