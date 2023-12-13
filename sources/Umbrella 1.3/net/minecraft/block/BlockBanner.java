/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner
extends BlockContainer {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger ROTATION_PROP = PropertyInteger.create("rotation", 0, 15);
    private static final String __OBFID = "CL_00002143";

    protected BlockBanner() {
        super(Material.wood);
        float var1 = 0.25f;
        float var2 = 1.0f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, var2, 0.5f + var1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBanner();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.banner;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.banner;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        TileEntity var6 = worldIn.getTileEntity(pos);
        if (var6 instanceof TileEntityBanner) {
            ItemStack var7 = new ItemStack(Items.banner, 1, ((TileEntityBanner)var6).getBaseColor());
            NBTTagCompound var8 = new NBTTagCompound();
            var6.writeToNBT(var8);
            var8.removeTag("x");
            var8.removeTag("y");
            var8.removeTag("z");
            var8.removeTag("id");
            var7.setTagInfo("BlockEntityTag", var8);
            BlockBanner.spawnAsEntity(worldIn, pos, var7);
        } else {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (te instanceof TileEntityBanner) {
            ItemStack var6 = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
            NBTTagCompound var7 = new NBTTagCompound();
            te.writeToNBT(var7);
            var7.removeTag("x");
            var7.removeTag("y");
            var7.removeTag("z");
            var7.removeTag("id");
            var6.setTagInfo("BlockEntityTag", var7);
            BlockBanner.spawnAsEntity(worldIn, pos, var6);
        } else {
            super.harvestBlock(worldIn, playerIn, pos, state, null);
        }
    }

    public static class BlockBannerHanging
    extends BlockBanner {
        private static final String __OBFID = "CL_00002140";

        public BlockBannerHanging() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.NORTH)));
        }

        @Override
        public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
            EnumFacing var3 = (EnumFacing)((Object)access.getBlockState(pos).getValue(FACING_PROP));
            float var4 = 0.0f;
            float var5 = 0.78125f;
            float var6 = 0.0f;
            float var7 = 1.0f;
            float var8 = 0.125f;
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            switch (var3) {
                default: {
                    this.setBlockBounds(var6, var4, 1.0f - var8, var7, var5, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(var6, var4, 0.0f, var7, var5, var8);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(1.0f - var8, var4, var6, 1.0f, var5, var7);
                    break;
                }
                case EAST: {
                    this.setBlockBounds(0.0f, var4, var6, var8, var5, var7);
                }
            }
        }

        @Override
        public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
            EnumFacing var5 = (EnumFacing)((Object)state.getValue(FACING_PROP));
            if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
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

    public static class BlockBannerStanding
    extends BlockBanner {
        private static final String __OBFID = "CL_00002141";

        public BlockBannerStanding() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION_PROP, Integer.valueOf(0)));
        }

        @Override
        public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
            if (!worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }

        @Override
        public IBlockState getStateFromMeta(int meta) {
            return this.getDefaultState().withProperty(ROTATION_PROP, Integer.valueOf(meta));
        }

        @Override
        public int getMetaFromState(IBlockState state) {
            return (Integer)state.getValue(ROTATION_PROP);
        }

        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, ROTATION_PROP);
        }
    }
}

