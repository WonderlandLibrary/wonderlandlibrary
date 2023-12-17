// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.inventory.Container;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;

public class BlockBrewingStand extends BlockContainer
{
    public static final PropertyBool[] HAS_BOTTLE;
    protected static final AxisAlignedBB BASE_AABB;
    protected static final AxisAlignedBB STICK_AABB;
    
    public BlockBrewingStand() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[0], false).withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[1], false).withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[2], false));
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("item.brewingStand.name");
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBrewingStand();
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockBrewingStand.STICK_AABB);
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockBrewingStand.BASE_AABB);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockBrewingStand.BASE_AABB;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBrewingStand) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.addStat(StatList.BREWINGSTAND_INTERACTION);
        }
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand)tileentity).setName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final double d0 = pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
        final double d2 = pos.getY() + 0.7f + rand.nextFloat() * 0.3f;
        final double d3 = pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.BREWING_STAND;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.BREWING_STAND);
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
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState();
        for (int i = 0; i < 3; ++i) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[i], (meta & 1 << i) > 0);
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        for (int j = 0; j < 3; ++j) {
            if (state.getValue((IProperty<Boolean>)BlockBrewingStand.HAS_BOTTLE[j])) {
                i |= 1 << j;
            }
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockBrewingStand.HAS_BOTTLE[0], BlockBrewingStand.HAS_BOTTLE[1], BlockBrewingStand.HAS_BOTTLE[2] });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        HAS_BOTTLE = new PropertyBool[] { PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2") };
        BASE_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        STICK_AABB = new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 0.875, 0.5625);
    }
}
