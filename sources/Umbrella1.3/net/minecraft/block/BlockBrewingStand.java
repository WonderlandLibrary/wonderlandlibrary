/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockBrewingStand
extends BlockContainer {
    public static final PropertyBool[] BOTTLE_PROPS = new PropertyBool[]{PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2")};
    private final Random rand = new Random();
    private static final String __OBFID = "CL_00000207";

    public BlockBrewingStand() {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BOTTLE_PROPS[0], Boolean.valueOf(false)).withProperty(BOTTLE_PROPS[1], Boolean.valueOf(false)).withProperty(BOTTLE_PROPS[2], Boolean.valueOf(false)));
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBrewingStand();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityBrewingStand) {
            playerIn.displayGUIChest((TileEntityBrewingStand)var9);
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity var6;
        if (stack.hasDisplayName() && (var6 = worldIn.getTileEntity(pos)) instanceof TileEntityBrewingStand) {
            ((TileEntityBrewingStand)var6).func_145937_a(stack.getDisplayName());
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        double var5 = (float)pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
        double var7 = (float)pos.getY() + 0.7f + rand.nextFloat() * 0.3f;
        double var9 = (float)pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var7, var9, 0.0, 0.0, 0.0, new int[0]);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBrewingStand)var4);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.brewing_stand;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.brewing_stand;
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState();
        for (int var3 = 0; var3 < 3; ++var3) {
            var2 = var2.withProperty(BOTTLE_PROPS[var3], Boolean.valueOf((meta & 1 << var3) > 0));
        }
        return var2;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        for (int var3 = 0; var3 < 3; ++var3) {
            if (!((Boolean)state.getValue(BOTTLE_PROPS[var3])).booleanValue()) continue;
            var2 |= 1 << var3;
        }
        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, BOTTLE_PROPS[0], BOTTLE_PROPS[1], BOTTLE_PROPS[2]);
    }
}

