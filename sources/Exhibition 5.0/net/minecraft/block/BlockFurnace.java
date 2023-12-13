// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.BlockPos;
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
    private static boolean field_149934_M;
    private static final String __OBFID = "CL_00000248";
    
    protected BlockFurnace(final boolean p_i45407_1_) {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH));
        this.isBurning = p_i45407_1_;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.furnace);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.func_176445_e(worldIn, pos, state);
    }
    
    private void func_176445_e(final World worldIn, final BlockPos p_176445_2_, final IBlockState p_176445_3_) {
        if (!worldIn.isRemote) {
            final Block var4 = worldIn.getBlockState(p_176445_2_.offsetNorth()).getBlock();
            final Block var5 = worldIn.getBlockState(p_176445_2_.offsetSouth()).getBlock();
            final Block var6 = worldIn.getBlockState(p_176445_2_.offsetWest()).getBlock();
            final Block var7 = worldIn.getBlockState(p_176445_2_.offsetEast()).getBlock();
            EnumFacing var8 = (EnumFacing)p_176445_3_.getValue(BlockFurnace.FACING);
            if (var8 == EnumFacing.NORTH && var4.isFullBlock() && !var5.isFullBlock()) {
                var8 = EnumFacing.SOUTH;
            }
            else if (var8 == EnumFacing.SOUTH && var5.isFullBlock() && !var4.isFullBlock()) {
                var8 = EnumFacing.NORTH;
            }
            else if (var8 == EnumFacing.WEST && var6.isFullBlock() && !var7.isFullBlock()) {
                var8 = EnumFacing.EAST;
            }
            else if (var8 == EnumFacing.EAST && var7.isFullBlock() && !var6.isFullBlock()) {
                var8 = EnumFacing.WEST;
            }
            worldIn.setBlockState(p_176445_2_, p_176445_3_.withProperty(BlockFurnace.FACING, var8), 2);
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isBurning) {
            final EnumFacing var5 = (EnumFacing)state.getValue(BlockFurnace.FACING);
            final double var6 = pos.getX() + 0.5;
            final double var7 = pos.getY() + rand.nextDouble() * 6.0 / 16.0;
            final double var8 = pos.getZ() + 0.5;
            final double var9 = 0.52;
            final double var10 = rand.nextDouble() * 0.6 - 0.3;
            switch (SwitchEnumFacing.field_180356_a[var5.ordinal()]) {
                case 1: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 - var9, var7, var8 + var10, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 - var9, var7, var8 + var10, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case 2: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var9, var7, var8 + var10, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var9, var7, var8 + var10, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case 3: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var10, var7, var8 - var9, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var10, var7, var8 - var9, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case 4: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var10, var7, var8 + var9, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var10, var7, var8 + var9, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityFurnace) {
            playerIn.displayGUIChest((IInventory)var9);
        }
        return true;
    }
    
    public static void func_176446_a(final boolean p_176446_0_, final World worldIn, final BlockPos p_176446_2_) {
        final IBlockState var3 = worldIn.getBlockState(p_176446_2_);
        final TileEntity var4 = worldIn.getTileEntity(p_176446_2_);
        BlockFurnace.field_149934_M = true;
        if (p_176446_0_) {
            worldIn.setBlockState(p_176446_2_, Blocks.lit_furnace.getDefaultState().withProperty(BlockFurnace.FACING, var3.getValue(BlockFurnace.FACING)), 3);
            worldIn.setBlockState(p_176446_2_, Blocks.lit_furnace.getDefaultState().withProperty(BlockFurnace.FACING, var3.getValue(BlockFurnace.FACING)), 3);
        }
        else {
            worldIn.setBlockState(p_176446_2_, Blocks.furnace.getDefaultState().withProperty(BlockFurnace.FACING, var3.getValue(BlockFurnace.FACING)), 3);
            worldIn.setBlockState(p_176446_2_, Blocks.furnace.getDefaultState().withProperty(BlockFurnace.FACING, var3.getValue(BlockFurnace.FACING)), 3);
        }
        BlockFurnace.field_149934_M = false;
        if (var4 != null) {
            var4.validate();
            worldIn.setTileEntity(p_176446_2_, var4);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityFurnace();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockFurnace.FACING, placer.func_174811_aO().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(BlockFurnace.FACING, placer.func_174811_aO().getOpposite()), 2);
        if (stack.hasDisplayName()) {
            final TileEntity var6 = worldIn.getTileEntity(pos);
            if (var6 instanceof TileEntityFurnace) {
                ((TileEntityFurnace)var6).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!BlockFurnace.field_149934_M) {
            final TileEntity var4 = worldIn.getTileEntity(pos);
            if (var4 instanceof TileEntityFurnace) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)var4);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
        super.breakBlock(worldIn, pos, state);
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
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.furnace);
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return this.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.SOUTH);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(BlockFurnace.FACING, var2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumFacing)state.getValue(BlockFurnace.FACING)).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFurnace.FACING });
    }
    
    static {
        FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_180356_a;
        private static final String __OBFID = "CL_00002111";
        
        static {
            field_180356_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_180356_a[EnumFacing.WEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_180356_a[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_180356_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_180356_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
