/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner
extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.banner;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean func_181623_g() {
        return true;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        this.setBlockBoundsBasedOnState(world, blockPos);
        return super.getSelectedBoundingBox(world, blockPos);
    }

    @Override
    public boolean isPassable(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityBanner();
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.banner;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    protected BlockBanner() {
        super(Material.wood);
        float f = 0.25f;
        float f2 = 1.0f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityBanner) {
            TileEntityBanner tileEntityBanner = (TileEntityBanner)tileEntity;
            ItemStack itemStack = new ItemStack(Items.banner, 1, ((TileEntityBanner)tileEntity).getBaseColor());
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            TileEntityBanner.func_181020_a(nBTTagCompound, tileEntityBanner.getBaseColor(), tileEntityBanner.func_181021_d());
            itemStack.setTagInfo("BlockEntityTag", nBTTagCompound);
            BlockBanner.spawnAsEntity(world, blockPos, itemStack);
        } else {
            super.harvestBlock(world, entityPlayer, blockPos, iBlockState, null);
        }
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.banner.white.name");
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityBanner) {
            ItemStack itemStack = new ItemStack(Items.banner, 1, ((TileEntityBanner)tileEntity).getBaseColor());
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            tileEntity.writeToNBT(nBTTagCompound);
            nBTTagCompound.removeTag("x");
            nBTTagCompound.removeTag("y");
            nBTTagCompound.removeTag("z");
            nBTTagCompound.removeTag("id");
            itemStack.setTagInfo("BlockEntityTag", nBTTagCompound);
            BlockBanner.spawnAsEntity(world, blockPos, itemStack);
        } else {
            super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, n);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return !this.func_181087_e(world, blockPos) && super.canPlaceBlockAt(world, blockPos);
    }

    public static class BlockBannerHanging
    extends BlockBanner {
        public BlockBannerHanging() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        }

        @Override
        public IBlockState getStateFromMeta(int n) {
            EnumFacing enumFacing = EnumFacing.getFront(n);
            if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
                enumFacing = EnumFacing.NORTH;
            }
            return this.getDefaultState().withProperty(FACING, enumFacing);
        }

        @Override
        public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
            EnumFacing enumFacing = iBlockAccess.getBlockState(blockPos).getValue(FACING);
            float f = 0.0f;
            float f2 = 0.78125f;
            float f3 = 0.0f;
            float f4 = 1.0f;
            float f5 = 0.125f;
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            switch (enumFacing) {
                default: {
                    this.setBlockBounds(f3, f, 1.0f - f5, f4, f2, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(f3, f, 0.0f, f4, f2, f5);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(1.0f - f5, f, f3, 1.0f, f2, f4);
                    break;
                }
                case EAST: {
                    this.setBlockBounds(0.0f, f, f3, f5, f2, f4);
                }
            }
        }

        @Override
        public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
            EnumFacing enumFacing = iBlockState.getValue(FACING);
            if (!world.getBlockState(blockPos.offset(enumFacing.getOpposite())).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(world, blockPos, iBlockState, 0);
                world.setBlockToAir(blockPos);
            }
            super.onNeighborBlockChange(world, blockPos, iBlockState, block);
        }

        @Override
        public int getMetaFromState(IBlockState iBlockState) {
            return iBlockState.getValue(FACING).getIndex();
        }

        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, FACING);
        }
    }

    public static class BlockBannerStanding
    extends BlockBanner {
        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, ROTATION);
        }

        public BlockBannerStanding() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0));
        }

        @Override
        public int getMetaFromState(IBlockState iBlockState) {
            return iBlockState.getValue(ROTATION);
        }

        @Override
        public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
            if (!world.getBlockState(blockPos.down()).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(world, blockPos, iBlockState, 0);
                world.setBlockToAir(blockPos);
            }
            super.onNeighborBlockChange(world, blockPos, iBlockState, block);
        }

        @Override
        public IBlockState getStateFromMeta(int n) {
            return this.getDefaultState().withProperty(ROTATION, n);
        }
    }
}

