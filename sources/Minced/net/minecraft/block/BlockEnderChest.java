// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyDirection;

public class BlockEnderChest extends BlockContainer
{
    public static final PropertyDirection FACING;
    protected static final AxisAlignedBB ENDER_CHEST_AABB;
    
    protected BlockEnderChest() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockEnderChest.FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockEnderChest.ENDER_CHEST_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean hasCustomBreakingProgress(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.OBSIDIAN);
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
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEnderChest.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockEnderChest.FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final InventoryEnderChest inventoryenderchest = playerIn.getInventoryEnderChest();
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (inventoryenderchest == null || !(tileentity instanceof TileEntityEnderChest)) {
            return true;
        }
        if (worldIn.getBlockState(pos.up()).isNormalCube()) {
            return true;
        }
        if (worldIn.isRemote) {
            return true;
        }
        inventoryenderchest.setChestTileEntity((TileEntityEnderChest)tileentity);
        playerIn.displayGUIChest(inventoryenderchest);
        playerIn.addStat(StatList.ENDERCHEST_OPENED);
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityEnderChest();
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        for (int i = 0; i < 3; ++i) {
            final int j = rand.nextInt(2) * 2 - 1;
            final int k = rand.nextInt(2) * 2 - 1;
            final double d0 = pos.getX() + 0.5 + 0.25 * j;
            final double d2 = pos.getY() + rand.nextFloat();
            final double d3 = pos.getZ() + 0.5 + 0.25 * k;
            final double d4 = rand.nextFloat() * j;
            final double d5 = (rand.nextFloat() - 0.5) * 0.125;
            final double d6 = rand.nextFloat() * k;
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d2, d3, d4, d5, d6, new int[0]);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEnderChest.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockEnderChest.FACING).getIndex();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockEnderChest.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockEnderChest.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockEnderChest.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockEnderChest.FACING });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        ENDER_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
    }
}
