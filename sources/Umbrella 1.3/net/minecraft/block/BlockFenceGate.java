/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate
extends BlockDirectional {
    public static final PropertyBool field_176466_a = PropertyBool.create("open");
    public static final PropertyBool field_176465_b = PropertyBool.create("powered");
    public static final PropertyBool field_176467_M = PropertyBool.create("in_wall");
    private static final String __OBFID = "CL_00000243";

    public BlockFenceGate() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176466_a, Boolean.valueOf(false)).withProperty(field_176465_b, Boolean.valueOf(false)).withProperty(field_176467_M, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis var4 = ((EnumFacing)((Object)state.getValue(AGE))).getAxis();
        if (var4 == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.offsetWest()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.offsetEast()).getBlock() == Blocks.cobblestone_wall) || var4 == EnumFacing.Axis.X && (worldIn.getBlockState(pos.offsetNorth()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.offsetSouth()).getBlock() == Blocks.cobblestone_wall)) {
            state = state.withProperty(field_176467_M, Boolean.valueOf(true));
        }
        return state;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean)state.getValue(field_176466_a)).booleanValue()) {
            return null;
        }
        EnumFacing.Axis var4 = ((EnumFacing)((Object)state.getValue(AGE))).getAxis();
        return var4 == EnumFacing.Axis.Z ? new AxisAlignedBB(pos.getX(), pos.getY(), (float)pos.getZ() + 0.375f, pos.getX() + 1, (float)pos.getY() + 1.5f, (float)pos.getZ() + 0.625f) : new AxisAlignedBB((float)pos.getX() + 0.375f, pos.getY(), pos.getZ(), (float)pos.getX() + 0.625f, (float)pos.getY() + 1.5f, pos.getZ() + 1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        EnumFacing.Axis var3 = ((EnumFacing)((Object)access.getBlockState(pos).getValue(AGE))).getAxis();
        if (var3 == EnumFacing.Axis.Z) {
            this.setBlockBounds(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
        } else {
            this.setBlockBounds(0.375f, 0.0f, 0.0f, 0.625f, 1.0f, 1.0f);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return (Boolean)blockAccess.getBlockState(pos).getValue(field_176466_a);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)placer.func_174811_aO())).withProperty(field_176466_a, Boolean.valueOf(false)).withProperty(field_176465_b, Boolean.valueOf(false)).withProperty(field_176467_M, Boolean.valueOf(false));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean)state.getValue(field_176466_a)).booleanValue()) {
            state = state.withProperty(field_176466_a, Boolean.valueOf(false));
            worldIn.setBlockState(pos, state, 2);
        } else {
            EnumFacing var9 = EnumFacing.fromAngle(playerIn.rotationYaw);
            if (state.getValue(AGE) == var9.getOpposite()) {
                state = state.withProperty(AGE, (Comparable)((Object)var9));
            }
            state = state.withProperty(field_176466_a, Boolean.valueOf(true));
            worldIn.setBlockState(pos, state, 2);
        }
        worldIn.playAuxSFXAtEntity(playerIn, (Boolean)state.getValue(field_176466_a) != false ? 1003 : 1006, pos, 0);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean var5;
        if (!worldIn.isRemote && ((var5 = worldIn.isBlockPowered(pos)) || neighborBlock.canProvidePower())) {
            if (var5 && !((Boolean)state.getValue(field_176466_a)).booleanValue() && !((Boolean)state.getValue(field_176465_b)).booleanValue()) {
                worldIn.setBlockState(pos, state.withProperty(field_176466_a, Boolean.valueOf(true)).withProperty(field_176465_b, Boolean.valueOf(true)), 2);
                worldIn.playAuxSFXAtEntity(null, 1003, pos, 0);
            } else if (!var5 && ((Boolean)state.getValue(field_176466_a)).booleanValue() && ((Boolean)state.getValue(field_176465_b)).booleanValue()) {
                worldIn.setBlockState(pos, state.withProperty(field_176466_a, Boolean.valueOf(false)).withProperty(field_176465_b, Boolean.valueOf(false)), 2);
                worldIn.playAuxSFXAtEntity(null, 1006, pos, 0);
            } else if (var5 != (Boolean)state.getValue(field_176465_b)) {
                worldIn.setBlockState(pos, state.withProperty(field_176465_b, Boolean.valueOf(var5)), 2);
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)EnumFacing.getHorizontal(meta))).withProperty(field_176466_a, Boolean.valueOf((meta & 4) != 0)).withProperty(field_176465_b, Boolean.valueOf((meta & 8) != 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(AGE))).getHorizontalIndex();
        if (((Boolean)state.getValue(field_176465_b)).booleanValue()) {
            var3 |= 8;
        }
        if (((Boolean)state.getValue(field_176466_a)).booleanValue()) {
            var3 |= 4;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, field_176466_a, field_176465_b, field_176467_M);
    }
}

