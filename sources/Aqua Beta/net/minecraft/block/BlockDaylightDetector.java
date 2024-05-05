package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyInteger POWER = PropertyInteger.create((String)"power", (int)0, (int)15);
    private final boolean inverted;

    public BlockDaylightDetector(boolean inverted) {
        super(Material.wood);
        this.inverted = inverted;
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)0)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setHardness(0.2f);
        this.setStepSound(soundTypeWood);
        this.setUnlocalizedName("daylightDetector");
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Integer)state.getValue((IProperty)POWER);
    }

    public void updatePower(World worldIn, BlockPos pos) {
        if (!worldIn.provider.getHasNoSky()) {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            int i = worldIn.getLightFor(EnumSkyBlock.SKY, pos) - worldIn.getSkylightSubtracted();
            float f = worldIn.getCelestialAngleRadians(1.0f);
            float f1 = f < (float)Math.PI ? 0.0f : (float)Math.PI * 2;
            f += (f1 - f) * 0.2f;
            i = Math.round((float)((float)i * MathHelper.cos((float)f)));
            i = MathHelper.clamp_int((int)i, (int)0, (int)15);
            if (this.inverted) {
                i = 15 - i;
            }
            if ((Integer)iblockstate.getValue((IProperty)POWER) != i) {
                worldIn.setBlockState(pos, iblockstate.withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)i)), 3);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isAllowEdit()) {
            if (worldIn.isRemote) {
                return true;
            }
            if (this.inverted) {
                worldIn.setBlockState(pos, Blocks.daylight_detector.getDefaultState().withProperty((IProperty)POWER, state.getValue((IProperty)POWER)), 4);
                Blocks.daylight_detector.updatePower(worldIn, pos);
            } else {
                worldIn.setBlockState(pos, Blocks.daylight_detector_inverted.getDefaultState().withProperty((IProperty)POWER, state.getValue((IProperty)POWER)), 4);
                Blocks.daylight_detector_inverted.updatePower(worldIn, pos);
            }
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock((Block)Blocks.daylight_detector);
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock((Block)Blocks.daylight_detector);
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return 3;
    }

    public boolean canProvidePower() {
        return true;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDaylightDetector();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)POWER);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{POWER});
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (!this.inverted) {
            super.getSubBlocks(itemIn, tab, list);
        }
    }
}
