// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.block.state.BlockState;
import net.minecraft.inventory.Container;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.dispenser.IPosition;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockDispenser extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyBool TRIGGERED;
    public static final RegistryDefaulted dispenseBehaviorRegistry;
    protected Random rand;
    private static final String __OBFID = "CL_00000229";
    
    protected BlockDispenser() {
        super(Material.rock);
        this.rand = new Random();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDispenser.FACING, EnumFacing.NORTH).withProperty(BlockDispenser.TRIGGERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 4;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos, state);
    }
    
    private void setDefaultDirection(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing var4 = (EnumFacing)state.getValue(BlockDispenser.FACING);
            final boolean var5 = worldIn.getBlockState(pos.offsetNorth()).getBlock().isFullBlock();
            final boolean var6 = worldIn.getBlockState(pos.offsetSouth()).getBlock().isFullBlock();
            if (var4 == EnumFacing.NORTH && var5 && !var6) {
                var4 = EnumFacing.SOUTH;
            }
            else if (var4 == EnumFacing.SOUTH && var6 && !var5) {
                var4 = EnumFacing.NORTH;
            }
            else {
                final boolean var7 = worldIn.getBlockState(pos.offsetWest()).getBlock().isFullBlock();
                final boolean var8 = worldIn.getBlockState(pos.offsetEast()).getBlock().isFullBlock();
                if (var4 == EnumFacing.WEST && var7 && !var8) {
                    var4 = EnumFacing.EAST;
                }
                else if (var4 == EnumFacing.EAST && var8 && !var7) {
                    var4 = EnumFacing.WEST;
                }
            }
            worldIn.setBlockState(pos, state.withProperty(BlockDispenser.FACING, var4).withProperty(BlockDispenser.TRIGGERED, false), 2);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityDispenser) {
            playerIn.displayGUIChest((IInventory)var9);
        }
        return true;
    }
    
    protected void func_176439_d(final World worldIn, final BlockPos p_176439_2_) {
        final BlockSourceImpl var3 = new BlockSourceImpl(worldIn, p_176439_2_);
        final TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();
        if (var4 != null) {
            final int var5 = var4.func_146017_i();
            if (var5 < 0) {
                worldIn.playAuxSFX(1001, p_176439_2_, 0);
            }
            else {
                final ItemStack var6 = var4.getStackInSlot(var5);
                final IBehaviorDispenseItem var7 = this.func_149940_a(var6);
                if (var7 != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
                    final ItemStack var8 = var7.dispense(var3, var6);
                    var4.setInventorySlotContents(var5, (var8.stackSize == 0) ? null : var8);
                }
            }
        }
    }
    
    protected IBehaviorDispenseItem func_149940_a(final ItemStack p_149940_1_) {
        return (IBehaviorDispenseItem)BlockDispenser.dispenseBehaviorRegistry.getObject((p_149940_1_ == null) ? null : p_149940_1_.getItem());
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final boolean var5 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.offsetUp());
        final boolean var6 = (boolean)state.getValue(BlockDispenser.TRIGGERED);
        if (var5 && !var6) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            worldIn.setBlockState(pos, state.withProperty(BlockDispenser.TRIGGERED, true), 4);
        }
        else if (!var5 && var6) {
            worldIn.setBlockState(pos, state.withProperty(BlockDispenser.TRIGGERED, false), 4);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            this.func_176439_d(worldIn, pos);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDispenser();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockDispenser.FACING, BlockPistonBase.func_180695_a(worldIn, pos, placer)).withProperty(BlockDispenser.TRIGGERED, false);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(BlockDispenser.FACING, BlockPistonBase.func_180695_a(worldIn, pos, placer)), 2);
        if (stack.hasDisplayName()) {
            final TileEntity var6 = worldIn.getTileEntity(pos);
            if (var6 instanceof TileEntityDispenser) {
                ((TileEntityDispenser)var6).func_146018_a(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof TileEntityDispenser) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)var4);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    public static IPosition getDispensePosition(final IBlockSource coords) {
        final EnumFacing var1 = getFacing(coords.getBlockMetadata());
        final double var2 = coords.getX() + 0.7 * var1.getFrontOffsetX();
        final double var3 = coords.getY() + 0.7 * var1.getFrontOffsetY();
        final double var4 = coords.getZ() + 0.7 * var1.getFrontOffsetZ();
        return new PositionImpl(var2, var3, var4);
    }
    
    public static EnumFacing getFacing(final int meta) {
        return EnumFacing.getFront(meta & 0x7);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return Container.calcRedstoneFromInventory(worldIn.getTileEntity(pos));
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return this.getDefaultState().withProperty(BlockDispenser.FACING, EnumFacing.SOUTH);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDispenser.FACING, getFacing(meta)).withProperty(BlockDispenser.TRIGGERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockDispenser.FACING)).getIndex();
        if (state.getValue(BlockDispenser.TRIGGERED)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDispenser.FACING, BlockDispenser.TRIGGERED });
    }
    
    static {
        FACING = PropertyDirection.create("facing");
        TRIGGERED = PropertyBool.create("triggered");
        dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    }
}
