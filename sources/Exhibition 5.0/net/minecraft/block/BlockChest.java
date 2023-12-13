// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.inventory.Container;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.world.ILockableContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.block.properties.PropertyDirection;

public class BlockChest extends BlockContainer
{
    public static final PropertyDirection FACING_PROP;
    private final Random rand;
    public final int chestType;
    private static final String __OBFID = "CL_00000214";
    
    protected BlockChest(final int type) {
        super(Material.wood);
        this.rand = new Random();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockChest.FACING_PROP, EnumFacing.NORTH));
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
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        if (access.getBlockState(pos.offsetNorth()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0f, 0.9375f, 0.875f, 0.9375f);
        }
        else if (access.getBlockState(pos.offsetSouth()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 1.0f);
        }
        else if (access.getBlockState(pos.offsetWest()).getBlock() == this) {
            this.setBlockBounds(0.0f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
        else if (access.getBlockState(pos.offsetEast()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 1.0f, 0.875f, 0.9375f);
        }
        else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForSurroundingChests(worldIn, pos, state);
        for (final EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos var6 = pos.offset(var5);
            final IBlockState var7 = worldIn.getBlockState(var6);
            if (var7.getBlock() == this) {
                this.checkForSurroundingChests(worldIn, var6, var7);
            }
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockChest.FACING_PROP, placer.func_174811_aO());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final EnumFacing var6 = EnumFacing.getHorizontal(MathHelper.floor_double(placer.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3).getOpposite();
        state = state.withProperty(BlockChest.FACING_PROP, var6);
        final BlockPos var7 = pos.offsetNorth();
        final BlockPos var8 = pos.offsetSouth();
        final BlockPos var9 = pos.offsetWest();
        final BlockPos var10 = pos.offsetEast();
        final boolean var11 = this == worldIn.getBlockState(var7).getBlock();
        final boolean var12 = this == worldIn.getBlockState(var8).getBlock();
        final boolean var13 = this == worldIn.getBlockState(var9).getBlock();
        final boolean var14 = this == worldIn.getBlockState(var10).getBlock();
        if (!var11 && !var12 && !var13 && !var14) {
            worldIn.setBlockState(pos, state, 3);
        }
        else if (var6.getAxis() == EnumFacing.Axis.X && (var11 || var12)) {
            if (var11) {
                worldIn.setBlockState(var7, state, 3);
            }
            else {
                worldIn.setBlockState(var8, state, 3);
            }
            worldIn.setBlockState(pos, state, 3);
        }
        else if (var6.getAxis() == EnumFacing.Axis.Z && (var13 || var14)) {
            if (var13) {
                worldIn.setBlockState(var9, state, 3);
            }
            else {
                worldIn.setBlockState(var10, state, 3);
            }
            worldIn.setBlockState(pos, state, 3);
        }
        if (stack.hasDisplayName()) {
            final TileEntity var15 = worldIn.getTileEntity(pos);
            if (var15 instanceof TileEntityChest) {
                ((TileEntityChest)var15).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    public IBlockState checkForSurroundingChests(final World worldIn, final BlockPos p_176455_2_, IBlockState p_176455_3_) {
        if (worldIn.isRemote) {
            return p_176455_3_;
        }
        final IBlockState var4 = worldIn.getBlockState(p_176455_2_.offsetNorth());
        final IBlockState var5 = worldIn.getBlockState(p_176455_2_.offsetSouth());
        final IBlockState var6 = worldIn.getBlockState(p_176455_2_.offsetWest());
        final IBlockState var7 = worldIn.getBlockState(p_176455_2_.offsetEast());
        EnumFacing var8 = (EnumFacing)p_176455_3_.getValue(BlockChest.FACING_PROP);
        final Block var9 = var4.getBlock();
        final Block var10 = var5.getBlock();
        final Block var11 = var6.getBlock();
        final Block var12 = var7.getBlock();
        if (var9 != this && var10 != this) {
            final boolean var13 = var9.isFullBlock();
            final boolean var14 = var10.isFullBlock();
            if (var11 == this || var12 == this) {
                final BlockPos var15 = (var11 == this) ? p_176455_2_.offsetWest() : p_176455_2_.offsetEast();
                final IBlockState var16 = worldIn.getBlockState(var15.offsetNorth());
                final IBlockState var17 = worldIn.getBlockState(var15.offsetSouth());
                var8 = EnumFacing.SOUTH;
                EnumFacing var18;
                if (var11 == this) {
                    var18 = (EnumFacing)var6.getValue(BlockChest.FACING_PROP);
                }
                else {
                    var18 = (EnumFacing)var7.getValue(BlockChest.FACING_PROP);
                }
                if (var18 == EnumFacing.NORTH) {
                    var8 = EnumFacing.NORTH;
                }
                final Block var19 = var16.getBlock();
                final Block var20 = var17.getBlock();
                if ((var13 || var19.isFullBlock()) && !var14 && !var20.isFullBlock()) {
                    var8 = EnumFacing.SOUTH;
                }
                if ((var14 || var20.isFullBlock()) && !var13 && !var19.isFullBlock()) {
                    var8 = EnumFacing.NORTH;
                }
            }
        }
        else {
            final BlockPos var21 = (var9 == this) ? p_176455_2_.offsetNorth() : p_176455_2_.offsetSouth();
            final IBlockState var22 = worldIn.getBlockState(var21.offsetWest());
            final IBlockState var23 = worldIn.getBlockState(var21.offsetEast());
            var8 = EnumFacing.EAST;
            EnumFacing var24;
            if (var9 == this) {
                var24 = (EnumFacing)var4.getValue(BlockChest.FACING_PROP);
            }
            else {
                var24 = (EnumFacing)var5.getValue(BlockChest.FACING_PROP);
            }
            if (var24 == EnumFacing.WEST) {
                var8 = EnumFacing.WEST;
            }
            final Block var25 = var22.getBlock();
            final Block var26 = var23.getBlock();
            if ((var11.isFullBlock() || var25.isFullBlock()) && !var12.isFullBlock() && !var26.isFullBlock()) {
                var8 = EnumFacing.EAST;
            }
            if ((var12.isFullBlock() || var26.isFullBlock()) && !var11.isFullBlock() && !var25.isFullBlock()) {
                var8 = EnumFacing.WEST;
            }
        }
        p_176455_3_ = p_176455_3_.withProperty(BlockChest.FACING_PROP, var8);
        worldIn.setBlockState(p_176455_2_, p_176455_3_, 3);
        return p_176455_3_;
    }
    
    public IBlockState func_176458_f(final World worldIn, final BlockPos p_176458_2_, final IBlockState p_176458_3_) {
        EnumFacing var4 = null;
        for (final EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            final IBlockState var7 = worldIn.getBlockState(p_176458_2_.offset(var6));
            if (var7.getBlock() == this) {
                return p_176458_3_;
            }
            if (!var7.getBlock().isFullBlock()) {
                continue;
            }
            if (var4 != null) {
                var4 = null;
                break;
            }
            var4 = var6;
        }
        if (var4 != null) {
            return p_176458_3_.withProperty(BlockChest.FACING_PROP, var4.getOpposite());
        }
        EnumFacing var8 = (EnumFacing)p_176458_3_.getValue(BlockChest.FACING_PROP);
        if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.getOpposite();
        }
        if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.rotateY();
        }
        if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.getOpposite();
        }
        return p_176458_3_.withProperty(BlockChest.FACING_PROP, var8);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        int var3 = 0;
        final BlockPos var4 = pos.offsetWest();
        final BlockPos var5 = pos.offsetEast();
        final BlockPos var6 = pos.offsetNorth();
        final BlockPos var7 = pos.offsetSouth();
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
    
    private boolean isSurroundingBlockChest(final World worldIn, final BlockPos p_176454_2_) {
        if (worldIn.getBlockState(p_176454_2_).getBlock() != this) {
            return false;
        }
        for (final EnumFacing var4 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(p_176454_2_.offset(var4)).getBlock() == this) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        final TileEntity var5 = worldIn.getTileEntity(pos);
        if (var5 instanceof TileEntityChest) {
            var5.updateContainingBlockInfo();
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)var4);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final ILockableContainer var9 = this.getLockableContainer(worldIn, pos);
        if (var9 != null) {
            playerIn.displayGUIChest(var9);
        }
        return true;
    }
    
    public ILockableContainer getLockableContainer(final World worldIn, final BlockPos p_180676_2_) {
        final TileEntity var3 = worldIn.getTileEntity(p_180676_2_);
        if (!(var3 instanceof TileEntityChest)) {
            return null;
        }
        Object var4 = var3;
        if (this.cannotOpenChest(worldIn, p_180676_2_)) {
            return null;
        }
        for (final EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos var7 = p_180676_2_.offset(var6);
            final Block var8 = worldIn.getBlockState(var7).getBlock();
            if (var8 == this) {
                if (this.cannotOpenChest(worldIn, var7)) {
                    return null;
                }
                final TileEntity var9 = worldIn.getTileEntity(var7);
                if (!(var9 instanceof TileEntityChest)) {
                    continue;
                }
                if (var6 != EnumFacing.WEST && var6 != EnumFacing.NORTH) {
                    var4 = new InventoryLargeChest("container.chestDouble", (ILockableContainer)var4, (ILockableContainer)var9);
                }
                else {
                    var4 = new InventoryLargeChest("container.chestDouble", (ILockableContainer)var9, (ILockableContainer)var4);
                }
            }
        }
        return (ILockableContainer)var4;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityChest();
    }
    
    @Override
    public boolean canProvidePower() {
        return this.chestType == 1;
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        if (!this.canProvidePower()) {
            return 0;
        }
        int var5 = 0;
        final TileEntity var6 = worldIn.getTileEntity(pos);
        if (var6 instanceof TileEntityChest) {
            var5 = ((TileEntityChest)var6).numPlayersUsing;
        }
        return MathHelper.clamp_int(var5, 0, 15);
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (side == EnumFacing.UP) ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }
    
    private boolean cannotOpenChest(final World worldIn, final BlockPos p_176457_2_) {
        return this.isBelowSolidBlock(worldIn, p_176457_2_) || this.isOcelotSittingOnChest(worldIn, p_176457_2_);
    }
    
    private boolean isBelowSolidBlock(final World worldIn, final BlockPos p_176456_2_) {
        return worldIn.getBlockState(p_176456_2_.offsetUp()).getBlock().isNormalCube();
    }
    
    private boolean isOcelotSittingOnChest(final World worldIn, final BlockPos p_176453_2_) {
        for (final Entity var4 : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(p_176453_2_.getX(), p_176453_2_.getY() + 1, p_176453_2_.getZ(), p_176453_2_.getX() + 1, p_176453_2_.getY() + 2, p_176453_2_.getZ() + 1))) {
            final EntityOcelot var5 = (EntityOcelot)var4;
            if (var5.isSitting()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(BlockChest.FACING_PROP, var2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumFacing)state.getValue(BlockChest.FACING_PROP)).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockChest.FACING_PROP });
    }
    
    static {
        FACING_PROP = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    }
}
