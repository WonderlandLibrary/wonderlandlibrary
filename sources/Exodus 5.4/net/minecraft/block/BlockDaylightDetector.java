/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector
extends BlockContainer {
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private final boolean inverted;

    public void updatePower(World world, BlockPos blockPos) {
        if (!world.provider.getHasNoSky()) {
            IBlockState iBlockState = world.getBlockState(blockPos);
            int n = world.getLightFor(EnumSkyBlock.SKY, blockPos) - world.getSkylightSubtracted();
            float f = world.getCelestialAngleRadians(1.0f);
            float f2 = f < (float)Math.PI ? 0.0f : (float)Math.PI * 2;
            f += (f2 - f) * 0.2f;
            n = Math.round((float)n * MathHelper.cos(f));
            n = MathHelper.clamp_int(n, 0, 15);
            if (this.inverted) {
                n = 15 - n;
            }
            if (iBlockState.getValue(POWER) != n) {
                world.setBlockState(blockPos, iBlockState.withProperty(POWER, n), 3);
            }
        }
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (entityPlayer.isAllowEdit()) {
            if (world.isRemote) {
                return true;
            }
            if (this.inverted) {
                world.setBlockState(blockPos, Blocks.daylight_detector.getDefaultState().withProperty(POWER, iBlockState.getValue(POWER)), 4);
                Blocks.daylight_detector.updatePower(world, blockPos);
            } else {
                world.setBlockState(blockPos, Blocks.daylight_detector_inverted.getDefaultState().withProperty(POWER, iBlockState.getValue(POWER)), 4);
                Blocks.daylight_detector_inverted.updatePower(world, blockPos);
            }
            return true;
        }
        return super.onBlockActivated(world, blockPos, iBlockState, entityPlayer, enumFacing, f, f2, f3);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(POWER);
    }

    @Override
    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return iBlockState.getValue(POWER);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public BlockDaylightDetector(boolean bl) {
        super(Material.wood);
        this.inverted = bl;
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setHardness(0.2f);
        this.setStepSound(soundTypeWood);
        this.setUnlocalizedName("daylightDetector");
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWER);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        if (!this.inverted) {
            super.getSubBlocks(item, creativeTabs, list);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(POWER, n);
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityDaylightDetector();
    }
}

