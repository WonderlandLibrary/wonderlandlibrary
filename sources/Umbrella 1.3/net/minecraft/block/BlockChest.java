/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest
extends BlockContainer {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private final Random rand = new Random();
    public final int chestType;
    private static final String __OBFID = "CL_00000214";

    protected BlockChest(int type) {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.NORTH)));
        this.chestType = type;
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
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        if (access.getBlockState(pos.offsetNorth()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0f, 0.9375f, 0.875f, 0.9375f);
        } else if (access.getBlockState(pos.offsetSouth()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 1.0f);
        } else if (access.getBlockState(pos.offsetWest()).getBlock() == this) {
            this.setBlockBounds(0.0f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        } else if (access.getBlockState(pos.offsetEast()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 1.0f, 0.875f, 0.9375f);
        } else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForSurroundingChests(worldIn, pos, state);
        for (EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
            BlockPos var6 = pos.offset(var5);
            IBlockState var7 = worldIn.getBlockState(var6);
            if (var7.getBlock() != this) continue;
            this.checkForSurroundingChests(worldIn, var6, var7);
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)placer.func_174811_aO()));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity var15;
        boolean var14;
        EnumFacing var6 = EnumFacing.getHorizontal(MathHelper.floor_double((double)(placer.rotationYaw * 4.0f / 360.0f) + 0.5) & 3).getOpposite();
        state = state.withProperty(FACING_PROP, (Comparable)((Object)var6));
        BlockPos var7 = pos.offsetNorth();
        BlockPos var8 = pos.offsetSouth();
        BlockPos var9 = pos.offsetWest();
        BlockPos var10 = pos.offsetEast();
        boolean var11 = this == worldIn.getBlockState(var7).getBlock();
        boolean var12 = this == worldIn.getBlockState(var8).getBlock();
        boolean var13 = this == worldIn.getBlockState(var9).getBlock();
        boolean bl = var14 = this == worldIn.getBlockState(var10).getBlock();
        if (!(var11 || var12 || var13 || var14)) {
            worldIn.setBlockState(pos, state, 3);
        } else if (var6.getAxis() == EnumFacing.Axis.X && (var11 || var12)) {
            if (var11) {
                worldIn.setBlockState(var7, state, 3);
            } else {
                worldIn.setBlockState(var8, state, 3);
            }
            worldIn.setBlockState(pos, state, 3);
        } else if (var6.getAxis() == EnumFacing.Axis.Z && (var13 || var14)) {
            if (var13) {
                worldIn.setBlockState(var9, state, 3);
            } else {
                worldIn.setBlockState(var10, state, 3);
            }
            worldIn.setBlockState(pos, state, 3);
        }
        if (stack.hasDisplayName() && (var15 = worldIn.getTileEntity(pos)) instanceof TileEntityChest) {
            ((TileEntityChest)var15).setCustomName(stack.getDisplayName());
        }
    }

    public IBlockState checkForSurroundingChests(World worldIn, BlockPos p_176455_2_, IBlockState p_176455_3_) {
        if (worldIn.isRemote) {
            return p_176455_3_;
        }
        IBlockState var4 = worldIn.getBlockState(p_176455_2_.offsetNorth());
        IBlockState var5 = worldIn.getBlockState(p_176455_2_.offsetSouth());
        IBlockState var6 = worldIn.getBlockState(p_176455_2_.offsetWest());
        IBlockState var7 = worldIn.getBlockState(p_176455_2_.offsetEast());
        EnumFacing var8 = (EnumFacing)((Object)p_176455_3_.getValue(FACING_PROP));
        Block var9 = var4.getBlock();
        Block var10 = var5.getBlock();
        Block var11 = var6.getBlock();
        Block var12 = var7.getBlock();
        if (var9 != this && var10 != this) {
            boolean var21 = var9.isFullBlock();
            boolean var22 = var10.isFullBlock();
            if (var11 == this || var12 == this) {
                BlockPos var23 = var11 == this ? p_176455_2_.offsetWest() : p_176455_2_.offsetEast();
                IBlockState var24 = worldIn.getBlockState(var23.offsetNorth());
                IBlockState var25 = worldIn.getBlockState(var23.offsetSouth());
                var8 = EnumFacing.SOUTH;
                EnumFacing var26 = var11 == this ? (EnumFacing)((Object)var6.getValue(FACING_PROP)) : (EnumFacing)((Object)var7.getValue(FACING_PROP));
                if (var26 == EnumFacing.NORTH) {
                    var8 = EnumFacing.NORTH;
                }
                Block var19 = var24.getBlock();
                Block var20 = var25.getBlock();
                if ((var21 || var19.isFullBlock()) && !var22 && !var20.isFullBlock()) {
                    var8 = EnumFacing.SOUTH;
                }
                if ((var22 || var20.isFullBlock()) && !var21 && !var19.isFullBlock()) {
                    var8 = EnumFacing.NORTH;
                }
            }
        } else {
            BlockPos var13 = var9 == this ? p_176455_2_.offsetNorth() : p_176455_2_.offsetSouth();
            IBlockState var14 = worldIn.getBlockState(var13.offsetWest());
            IBlockState var15 = worldIn.getBlockState(var13.offsetEast());
            var8 = EnumFacing.EAST;
            EnumFacing var16 = var9 == this ? (EnumFacing)((Object)var4.getValue(FACING_PROP)) : (EnumFacing)((Object)var5.getValue(FACING_PROP));
            if (var16 == EnumFacing.WEST) {
                var8 = EnumFacing.WEST;
            }
            Block var17 = var14.getBlock();
            Block var18 = var15.getBlock();
            if ((var11.isFullBlock() || var17.isFullBlock()) && !var12.isFullBlock() && !var18.isFullBlock()) {
                var8 = EnumFacing.EAST;
            }
            if ((var12.isFullBlock() || var18.isFullBlock()) && !var11.isFullBlock() && !var17.isFullBlock()) {
                var8 = EnumFacing.WEST;
            }
        }
        p_176455_3_ = p_176455_3_.withProperty(FACING_PROP, (Comparable)((Object)var8));
        worldIn.setBlockState(p_176455_2_, p_176455_3_, 3);
        return p_176455_3_;
    }

    public IBlockState func_176458_f(World worldIn, BlockPos p_176458_2_, IBlockState p_176458_3_) {
        EnumFacing var4 = null;
        for (EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState var7 = worldIn.getBlockState(p_176458_2_.offset(var6));
            if (var7.getBlock() == this) {
                return p_176458_3_;
            }
            if (!var7.getBlock().isFullBlock()) continue;
            if (var4 != null) {
                var4 = null;
                break;
            }
            var4 = var6;
        }
        if (var4 != null) {
            return p_176458_3_.withProperty(FACING_PROP, (Comparable)((Object)var4.getOpposite()));
        }
        EnumFacing var8 = (EnumFacing)((Object)p_176458_3_.getValue(FACING_PROP));
        if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.getOpposite();
        }
        if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.rotateY();
        }
        if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.getOpposite();
        }
        return p_176458_3_.withProperty(FACING_PROP, (Comparable)((Object)var8));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        int var3 = 0;
        BlockPos var4 = pos.offsetWest();
        BlockPos var5 = pos.offsetEast();
        BlockPos var6 = pos.offsetNorth();
        BlockPos var7 = pos.offsetSouth();
        if (worldIn.getBlockState(var4).getBlock() == this) {
            if (this.isSurroundingBlockChest(worldIn, var4)) {
                return false;
            }
            ++var3;
        }
        if (worldIn.getBlockState(var5).getBlock() == this) {
            if (this.isSurroundingBlockChest(worldIn, var5)) {
                return false;
            }
            ++var3;
        }
        if (worldIn.getBlockState(var6).getBlock() == this) {
            if (this.isSurroundingBlockChest(worldIn, var6)) {
                return false;
            }
            ++var3;
        }
        if (worldIn.getBlockState(var7).getBlock() == this) {
            if (this.isSurroundingBlockChest(worldIn, var7)) {
                return false;
            }
            ++var3;
        }
        return var3 <= 1;
    }

    private boolean isSurroundingBlockChest(World worldIn, BlockPos p_176454_2_) {
        EnumFacing var4;
        if (worldIn.getBlockState(p_176454_2_).getBlock() != this) {
            return false;
        }
        Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (var3.hasNext()) continue;
            return false;
        } while (worldIn.getBlockState(p_176454_2_.offset(var4 = (EnumFacing)var3.next())).getBlock() != this);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        TileEntity var5 = worldIn.getTileEntity(pos);
        if (var5 instanceof TileEntityChest) {
            var5.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)((Object)var4));
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        ILockableContainer var9 = this.getLockableContainer(worldIn, pos);
        if (var9 != null) {
            playerIn.displayGUIChest(var9);
        }
        return true;
    }

    public ILockableContainer getLockableContainer(World worldIn, BlockPos p_180676_2_) {
        TileEntity var3 = worldIn.getTileEntity(p_180676_2_);
        if (!(var3 instanceof TileEntityChest)) {
            return null;
        }
        ILockableContainer var4 = (TileEntityChest)var3;
        if (this.cannotOpenChest(worldIn, p_180676_2_)) {
            return null;
        }
        for (EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            BlockPos var7 = p_180676_2_.offset(var6);
            Block var8 = worldIn.getBlockState(var7).getBlock();
            if (var8 != this) continue;
            if (this.cannotOpenChest(worldIn, var7)) {
                return null;
            }
            TileEntity var9 = worldIn.getTileEntity(var7);
            if (!(var9 instanceof TileEntityChest)) continue;
            var4 = var6 != EnumFacing.WEST && var6 != EnumFacing.NORTH ? new InventoryLargeChest("container.chestDouble", var4, (TileEntityChest)var9) : new InventoryLargeChest("container.chestDouble", (TileEntityChest)var9, var4);
        }
        return var4;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChest();
    }

    @Override
    public boolean canProvidePower() {
        return this.chestType == 1;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!this.canProvidePower()) {
            return 0;
        }
        int var5 = 0;
        TileEntity var6 = worldIn.getTileEntity(pos);
        if (var6 instanceof TileEntityChest) {
            var5 = ((TileEntityChest)var6).numPlayersUsing;
        }
        return MathHelper.clamp_int(var5, 0, 15);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return side == EnumFacing.UP ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }

    private boolean cannotOpenChest(World worldIn, BlockPos p_176457_2_) {
        return this.isBelowSolidBlock(worldIn, p_176457_2_) || this.isOcelotSittingOnChest(worldIn, p_176457_2_);
    }

    private boolean isBelowSolidBlock(World worldIn, BlockPos p_176456_2_) {
        return worldIn.getBlockState(p_176456_2_.offsetUp()).getBlock().isNormalCube();
    }

    private boolean isOcelotSittingOnChest(World worldIn, BlockPos p_176453_2_) {
        Entity var4;
        EntityOcelot var5;
        Iterator var3 = worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(p_176453_2_.getX(), p_176453_2_.getY() + 1, p_176453_2_.getZ(), p_176453_2_.getX() + 1, p_176453_2_.getY() + 2, p_176453_2_.getZ() + 1)).iterator();
        do {
            if (var3.hasNext()) continue;
            return false;
        } while (!(var5 = (EntityOcelot)(var4 = (Entity)var3.next())).isSitting());
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)var2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)((Object)state.getValue(FACING_PROP))).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING_PROP);
    }
}

