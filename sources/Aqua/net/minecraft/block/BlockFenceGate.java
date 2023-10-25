package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyBool OPEN = PropertyBool.create((String)"open");
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");
    public static final PropertyBool IN_WALL = PropertyBool.create((String)"in_wall");

    public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
        super(Material.wood, p_i46394_1_.getMapColor());
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)IN_WALL, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
        if (enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.east()).getBlock() == Blocks.cobblestone_wall) || enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.south()).getBlock() == Blocks.cobblestone_wall)) {
            state = state.withProperty((IProperty)IN_WALL, (Comparable)Boolean.valueOf((boolean)true));
        }
        return state;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
            return null;
        }
        EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
        return enumfacing$axis == EnumFacing.Axis.Z ? new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)((float)pos.getZ() + 0.375f), (double)(pos.getX() + 1), (double)((float)pos.getY() + 1.5f), (double)((float)pos.getZ() + 0.625f)) : new AxisAlignedBB((double)((float)pos.getX() + 0.375f), (double)pos.getY(), (double)pos.getZ(), (double)((float)pos.getX() + 0.625f), (double)((float)pos.getY() + 1.5f), (double)(pos.getZ() + 1));
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = ((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)).getAxis();
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            this.setBlockBounds(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
        } else {
            this.setBlockBounds(0.375f, 0.0f, 0.0f, 0.625f, 1.0f, 1.0f);
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return (Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)IN_WALL, (Comparable)Boolean.valueOf((boolean)false));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
            state = state.withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)false));
            worldIn.setBlockState(pos, state, 2);
        } else {
            EnumFacing enumfacing = EnumFacing.fromAngle((double)playerIn.rotationYaw);
            if (state.getValue((IProperty)FACING) == enumfacing.getOpposite()) {
                state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
            }
            state = state.withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)true));
            worldIn.setBlockState(pos, state, 2);
        }
        worldIn.playAuxSFXAtEntity(playerIn, (Boolean)state.getValue((IProperty)OPEN) != false ? 1003 : 1006, pos, 0);
        return true;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean flag;
        if (!worldIn.isRemote && ((flag = worldIn.isBlockPowered(pos)) || neighborBlock.canProvidePower())) {
            if (flag && !((Boolean)state.getValue((IProperty)OPEN)).booleanValue() && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)true)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)), 2);
                worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1003, pos, 0);
            } else if (!flag && ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)), 2);
                worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1006, pos, 0);
            } else if (flag != (Boolean)state.getValue((IProperty)POWERED)) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)flag)), 2);
            }
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)meta)).withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf(((meta & 4) != 0 ? 1 : 0) != 0)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 8) != 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 8;
        }
        if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
            i |= 4;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, OPEN, POWERED, IN_WALL});
    }
}
