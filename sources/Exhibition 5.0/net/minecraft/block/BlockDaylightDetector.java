// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import java.util.List;
import net.minecraft.block.state.BlockState;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockDaylightDetector extends BlockContainer
{
    public static final PropertyInteger field_176436_a;
    private final boolean field_176435_b;
    private static final String __OBFID = "CL_00000223";
    
    public BlockDaylightDetector(final boolean p_i45729_1_) {
        super(Material.wood);
        this.field_176435_b = p_i45729_1_;
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDaylightDetector.field_176436_a, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setHardness(0.2f);
        this.setStepSound(Block.soundTypeWood);
        this.setUnlocalizedName("daylightDetector");
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (int)state.getValue(BlockDaylightDetector.field_176436_a);
    }
    
    public void func_180677_d(final World worldIn, final BlockPos p_180677_2_) {
        if (!worldIn.provider.getHasNoSky()) {
            final IBlockState var3 = worldIn.getBlockState(p_180677_2_);
            int var4 = worldIn.getLightFor(EnumSkyBlock.SKY, p_180677_2_) - worldIn.getSkylightSubtracted();
            float var5 = worldIn.getCelestialAngleRadians(1.0f);
            final float var6 = (var5 < 3.1415927f) ? 0.0f : 6.2831855f;
            var5 += (var6 - var5) * 0.2f;
            var4 = Math.round(var4 * MathHelper.cos(var5));
            var4 = MathHelper.clamp_int(var4, 0, 15);
            if (this.field_176435_b) {
                var4 = 15 - var4;
            }
            if ((int)var3.getValue(BlockDaylightDetector.field_176436_a) != var4) {
                worldIn.setBlockState(p_180677_2_, var3.withProperty(BlockDaylightDetector.field_176436_a, var4), 3);
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.func_175142_cm()) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }
        if (worldIn.isRemote) {
            return true;
        }
        if (this.field_176435_b) {
            worldIn.setBlockState(pos, Blocks.daylight_detector.getDefaultState().withProperty(BlockDaylightDetector.field_176436_a, state.getValue(BlockDaylightDetector.field_176436_a)), 4);
            Blocks.daylight_detector.func_180677_d(worldIn, pos);
        }
        else {
            worldIn.setBlockState(pos, Blocks.daylight_detector_inverted.getDefaultState().withProperty(BlockDaylightDetector.field_176436_a, state.getValue(BlockDaylightDetector.field_176436_a)), 4);
            Blocks.daylight_detector_inverted.func_180677_d(worldIn, pos);
        }
        return true;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
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
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDaylightDetector();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDaylightDetector.field_176436_a, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockDaylightDetector.field_176436_a);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDaylightDetector.field_176436_a });
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        if (!this.field_176435_b) {
            super.getSubBlocks(itemIn, tab, list);
        }
    }
    
    static {
        field_176436_a = PropertyInteger.create("power", 0, 15);
    }
}
