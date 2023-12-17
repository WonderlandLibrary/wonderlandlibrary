// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public class BlockFurnace extends BlockContainer
{
    public static final PropertyDirection FACING;
    private final boolean isBurning;
    private static boolean keepInventory;
    
    protected BlockFurnace(final boolean isBurning) {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, EnumFacing.NORTH));
        this.isBurning = isBurning;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.FURNACE);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }
    
    private void setDefaultFacing(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.north());
            final IBlockState iblockstate2 = worldIn.getBlockState(pos.south());
            final IBlockState iblockstate3 = worldIn.getBlockState(pos.west());
            final IBlockState iblockstate4 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING);
            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate2.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate2.isFullBlock() && !iblockstate.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate3.isFullBlock() && !iblockstate4.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate4.isFullBlock() && !iblockstate3.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFurnace.FACING, enumfacing), 2);
        }
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        if (this.isBurning) {
            final EnumFacing enumfacing = stateIn.getValue((IProperty<EnumFacing>)BlockFurnace.FACING);
            final double d0 = pos.getX() + 0.5;
            final double d2 = pos.getY() + rand.nextDouble() * 6.0 / 16.0;
            final double d3 = pos.getZ() + 0.5;
            final double d4 = 0.52;
            final double d5 = rand.nextDouble() * 0.6 - 0.3;
            if (rand.nextDouble() < 0.1) {
                worldIn.playSound(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            }
            switch (enumfacing) {
                case WEST: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case EAST: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case NORTH: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d5, d2, d3 - 0.52, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d5, d2, d3 - 0.52, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case SOUTH: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d5, d2, d3 + 0.52, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d5, d2, d3 + 0.52, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityFurnace) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.addStat(StatList.FURNACE_INTERACTION);
        }
        return true;
    }
    
    public static void setState(final boolean active, final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        BlockFurnace.keepInventory = true;
        if (active) {
            worldIn.setBlockState(pos, Blocks.LIT_FURNACE.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (Comparable)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
            worldIn.setBlockState(pos, Blocks.LIT_FURNACE.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (Comparable)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
        }
        else {
            worldIn.setBlockState(pos, Blocks.FURNACE.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (Comparable)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
            worldIn.setBlockState(pos, Blocks.FURNACE.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (Comparable)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
        }
        BlockFurnace.keepInventory = false;
        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityFurnace();
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFurnace.FACING, placer.getHorizontalFacing().getOpposite()), 2);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityFurnace) {
                ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!BlockFurnace.keepInventory) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityFurnace) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Blocks.FURNACE);
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING).getIndex();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockFurnace.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFurnace.FACING });
    }
    
    static {
        FACING = BlockHorizontal.FACING;
    }
}
