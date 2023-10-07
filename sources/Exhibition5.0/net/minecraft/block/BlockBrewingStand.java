// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.inventory.Container;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.block.properties.PropertyBool;

public class BlockBrewingStand extends BlockContainer
{
    public static final PropertyBool[] BOTTLE_PROPS;
    private final Random rand;
    private static final String __OBFID = "CL_00000207";
    
    public BlockBrewingStand() {
        super(Material.iron);
        this.rand = new Random();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBrewingStand.BOTTLE_PROPS[0], false).withProperty(BlockBrewingStand.BOTTLE_PROPS[1], false).withProperty(BlockBrewingStand.BOTTLE_PROPS[2], false));
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBrewingStand();
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        this.setBlockBounds(0.4375f, 0.0f, 0.4375f, 0.5625f, 0.875f, 0.5625f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBoundsForItemRender();
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityBrewingStand) {
            playerIn.displayGUIChest((IInventory)var9);
        }
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (stack.hasDisplayName()) {
            final TileEntity var6 = worldIn.getTileEntity(pos);
            if (var6 instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand)var6).func_145937_a(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final double var5 = pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
        final double var6 = pos.getY() + 0.7f + rand.nextFloat() * 0.3f;
        final double var7 = pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var6, var7, 0.0, 0.0, 0.0, new int[0]);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)var4);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.brewing_stand;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.brewing_stand;
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState();
        for (int var3 = 0; var3 < 3; ++var3) {
            var2 = var2.withProperty(BlockBrewingStand.BOTTLE_PROPS[var3], (meta & 1 << var3) > 0);
        }
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        for (int var3 = 0; var3 < 3; ++var3) {
            if (state.getValue(BlockBrewingStand.BOTTLE_PROPS[var3])) {
                var2 |= 1 << var3;
            }
        }
        return var2;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockBrewingStand.BOTTLE_PROPS[0], BlockBrewingStand.BOTTLE_PROPS[1], BlockBrewingStand.BOTTLE_PROPS[2] });
    }
    
    static {
        BOTTLE_PROPS = new PropertyBool[] { PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2") };
    }
}
