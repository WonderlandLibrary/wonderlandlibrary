// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyDirection;

public class BlockBanner extends BlockContainer
{
    public static final PropertyDirection FACING_PROP;
    public static final PropertyInteger ROTATION_PROP;
    private static final String __OBFID = "CL_00002143";
    
    protected BlockBanner() {
        super(Material.wood);
        final float var1 = 0.25f;
        final float var2 = 1.0f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, var2, 0.5f + var1);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBanner();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.banner;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.banner;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        final TileEntity var6 = worldIn.getTileEntity(pos);
        if (var6 instanceof TileEntityBanner) {
            final ItemStack var7 = new ItemStack(Items.banner, 1, ((TileEntityBanner)var6).getBaseColor());
            final NBTTagCompound var8 = new NBTTagCompound();
            var6.writeToNBT(var8);
            var8.removeTag("x");
            var8.removeTag("y");
            var8.removeTag("z");
            var8.removeTag("id");
            var7.setTagInfo("BlockEntityTag", var8);
            Block.spawnAsEntity(worldIn, pos, var7);
        }
        else {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (te instanceof TileEntityBanner) {
            final ItemStack var6 = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
            final NBTTagCompound var7 = new NBTTagCompound();
            te.writeToNBT(var7);
            var7.removeTag("x");
            var7.removeTag("y");
            var7.removeTag("z");
            var7.removeTag("id");
            var6.setTagInfo("BlockEntityTag", var7);
            Block.spawnAsEntity(worldIn, pos, var6);
        }
        else {
            super.harvestBlock(worldIn, playerIn, pos, state, null);
        }
    }
    
    static {
        FACING_PROP = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
        ROTATION_PROP = PropertyInteger.create("rotation", 0, 15);
    }
    
    public static class BlockBannerHanging extends BlockBanner
    {
        private static final String __OBFID = "CL_00002140";
        
        public BlockBannerHanging() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBanner.FACING_PROP, EnumFacing.NORTH));
        }
        
        @Override
        public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
            final EnumFacing var3 = (EnumFacing)access.getBlockState(pos).getValue(BlockBanner.FACING_PROP);
            final float var4 = 0.0f;
            final float var5 = 0.78125f;
            final float var6 = 0.0f;
            final float var7 = 1.0f;
            final float var8 = 0.125f;
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            switch (SwitchEnumFacing.SWITCH_MAP[var3.ordinal()]) {
                default: {
                    this.setBlockBounds(var6, var4, 1.0f - var8, var7, var5, 1.0f);
                    break;
                }
                case 2: {
                    this.setBlockBounds(var6, var4, 0.0f, var7, var5, var8);
                    break;
                }
                case 3: {
                    this.setBlockBounds(1.0f - var8, var4, var6, 1.0f, var5, var7);
                    break;
                }
                case 4: {
                    this.setBlockBounds(0.0f, var4, var6, var8, var5, var7);
                    break;
                }
            }
        }
        
        @Override
        public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
            final EnumFacing var5 = (EnumFacing)state.getValue(BlockBanner.FACING_PROP);
            if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
        
        @Override
        public IBlockState getStateFromMeta(final int meta) {
            EnumFacing var2 = EnumFacing.getFront(meta);
            if (var2.getAxis() == EnumFacing.Axis.Y) {
                var2 = EnumFacing.NORTH;
            }
            return this.getDefaultState().withProperty(BlockBanner.FACING_PROP, var2);
        }
        
        @Override
        public int getMetaFromState(final IBlockState state) {
            return ((EnumFacing)state.getValue(BlockBanner.FACING_PROP)).getIndex();
        }
        
        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, new IProperty[] { BlockBanner.FACING_PROP });
        }
    }
    
    public static class BlockBannerStanding extends BlockBanner
    {
        private static final String __OBFID = "CL_00002141";
        
        public BlockBannerStanding() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBanner.ROTATION_PROP, 0));
        }
        
        @Override
        public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
            if (!worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
        
        @Override
        public IBlockState getStateFromMeta(final int meta) {
            return this.getDefaultState().withProperty(BlockBanner.ROTATION_PROP, meta);
        }
        
        @Override
        public int getMetaFromState(final IBlockState state) {
            return (int)state.getValue(BlockBanner.ROTATION_PROP);
        }
        
        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, new IProperty[] { BlockBanner.ROTATION_PROP });
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] SWITCH_MAP;
        private static final String __OBFID = "CL_00002142";
        
        static {
            SWITCH_MAP = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.SWITCH_MAP[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.SWITCH_MAP[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.SWITCH_MAP[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.SWITCH_MAP[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
