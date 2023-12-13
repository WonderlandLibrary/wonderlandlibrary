/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser
extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
    public static final RegistryDefaulted dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    protected Random rand = new Random();
    private static final String __OBFID = "CL_00000229";

    protected BlockDispenser() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, (Comparable)((Object)EnumFacing.NORTH)).withProperty(TRIGGERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public int tickRate(World worldIn) {
        return 4;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos, state);
    }

    private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing var4 = (EnumFacing)((Object)state.getValue(FACING));
            boolean var5 = worldIn.getBlockState(pos.offsetNorth()).getBlock().isFullBlock();
            boolean var6 = worldIn.getBlockState(pos.offsetSouth()).getBlock().isFullBlock();
            if (var4 == EnumFacing.NORTH && var5 && !var6) {
                var4 = EnumFacing.SOUTH;
            } else if (var4 == EnumFacing.SOUTH && var6 && !var5) {
                var4 = EnumFacing.NORTH;
            } else {
                boolean var7 = worldIn.getBlockState(pos.offsetWest()).getBlock().isFullBlock();
                boolean var8 = worldIn.getBlockState(pos.offsetEast()).getBlock().isFullBlock();
                if (var4 == EnumFacing.WEST && var7 && !var8) {
                    var4 = EnumFacing.EAST;
                } else if (var4 == EnumFacing.EAST && var8 && !var7) {
                    var4 = EnumFacing.WEST;
                }
            }
            worldIn.setBlockState(pos, state.withProperty(FACING, (Comparable)((Object)var4)).withProperty(TRIGGERED, Boolean.valueOf(false)), 2);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityDispenser) {
            playerIn.displayGUIChest((TileEntityDispenser)var9);
        }
        return true;
    }

    protected void func_176439_d(World worldIn, BlockPos p_176439_2_) {
        BlockSourceImpl var3 = new BlockSourceImpl(worldIn, p_176439_2_);
        TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();
        if (var4 != null) {
            int var5 = var4.func_146017_i();
            if (var5 < 0) {
                worldIn.playAuxSFX(1001, p_176439_2_, 0);
            } else {
                ItemStack var6 = var4.getStackInSlot(var5);
                IBehaviorDispenseItem var7 = this.func_149940_a(var6);
                if (var7 != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
                    ItemStack var8 = var7.dispense(var3, var6);
                    var4.setInventorySlotContents(var5, var8.stackSize == 0 ? null : var8);
                }
            }
        }
    }

    protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_) {
        return (IBehaviorDispenseItem)dispenseBehaviorRegistry.getObject(p_149940_1_ == null ? null : p_149940_1_.getItem());
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean var5 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.offsetUp());
        boolean var6 = (Boolean)state.getValue(TRIGGERED);
        if (var5 && !var6) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
        } else if (!var5 && var6) {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.func_176439_d(worldIn, pos);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDispenser();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, (Comparable)((Object)BlockPistonBase.func_180695_a(worldIn, pos, placer))).withProperty(TRIGGERED, Boolean.valueOf(false));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity var6;
        worldIn.setBlockState(pos, state.withProperty(FACING, (Comparable)((Object)BlockPistonBase.func_180695_a(worldIn, pos, placer))), 2);
        if (stack.hasDisplayName() && (var6 = worldIn.getTileEntity(pos)) instanceof TileEntityDispenser) {
            ((TileEntityDispenser)var6).func_146018_a(stack.getDisplayName());
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof TileEntityDispenser) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDispenser)var4);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public static IPosition getDispensePosition(IBlockSource coords) {
        EnumFacing var1 = BlockDispenser.getFacing(coords.getBlockMetadata());
        double var2 = coords.getX() + 0.7 * (double)var1.getFrontOffsetX();
        double var4 = coords.getY() + 0.7 * (double)var1.getFrontOffsetY();
        double var6 = coords.getZ() + 0.7 * (double)var1.getFrontOffsetZ();
        return new PositionImpl(var2, var4, var6);
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(worldIn.getTileEntity(pos));
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(FACING, (Comparable)((Object)EnumFacing.SOUTH));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, (Comparable)((Object)BlockDispenser.getFacing(meta))).withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(FACING))).getIndex();
        if (((Boolean)state.getValue(TRIGGERED)).booleanValue()) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, TRIGGERED);
    }
}

