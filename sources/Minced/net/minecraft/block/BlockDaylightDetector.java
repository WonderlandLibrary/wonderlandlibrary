// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.NonNullList;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockDaylightDetector extends BlockContainer
{
    public static final PropertyInteger POWER;
    protected static final AxisAlignedBB DAYLIGHT_DETECTOR_AABB;
    private final boolean inverted;
    
    public BlockDaylightDetector(final boolean inverted) {
        super(Material.WOOD);
        this.inverted = inverted;
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, 0));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setHardness(0.2f);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey("daylightDetector");
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockDaylightDetector.DAYLIGHT_DETECTOR_AABB;
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getValue((IProperty<Integer>)BlockDaylightDetector.POWER);
    }
    
    public void updatePower(final World worldIn, final BlockPos pos) {
        if (worldIn.provider.hasSkyLight()) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            int i = worldIn.getLightFor(EnumSkyBlock.SKY, pos) - worldIn.getSkylightSubtracted();
            float f = worldIn.getCelestialAngleRadians(1.0f);
            if (this.inverted) {
                i = 15 - i;
            }
            if (i > 0 && !this.inverted) {
                final float f2 = (f < 3.1415927f) ? 0.0f : 6.2831855f;
                f += (f2 - f) * 0.2f;
                i = Math.round(i * MathHelper.cos(f));
            }
            i = MathHelper.clamp(i, 0, 15);
            if (iblockstate.getValue((IProperty<Integer>)BlockDaylightDetector.POWER) != i) {
                worldIn.setBlockState(pos, iblockstate.withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, i), 3);
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.isAllowEdit()) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
        if (worldIn.isRemote) {
            return true;
        }
        if (this.inverted) {
            worldIn.setBlockState(pos, Blocks.DAYLIGHT_DETECTOR.getDefaultState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, (Comparable)state.getValue((IProperty<V>)BlockDaylightDetector.POWER)), 4);
            Blocks.DAYLIGHT_DETECTOR.updatePower(worldIn, pos);
        }
        else {
            worldIn.setBlockState(pos, Blocks.DAYLIGHT_DETECTOR_INVERTED.getDefaultState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, (Comparable)state.getValue((IProperty<V>)BlockDaylightDetector.POWER)), 4);
            Blocks.DAYLIGHT_DETECTOR_INVERTED.updatePower(worldIn, pos);
        }
        return true;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Blocks.DAYLIGHT_DETECTOR);
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
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
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDaylightDetector();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockDaylightDetector.POWER);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockDaylightDetector.POWER });
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        if (!this.inverted) {
            super.getSubBlocks(itemIn, items);
        }
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return (face == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    static {
        POWER = PropertyInteger.create("power", 0, 15);
        DAYLIGHT_DETECTOR_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0);
    }
}
