package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create((String)"facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN = PropertyBool.create((String)"open");
    public static final PropertyEnum<DoorHalf> HALF = PropertyEnum.create((String)"half", DoorHalf.class);

    protected BlockTrapDoor(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)false)).withProperty(HALF, (Comparable)DoorHalf.BOTTOM));
        float f = 0.5f;
        float f1 = 1.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return (Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN) == false;
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBounds(worldIn.getBlockState(pos));
    }

    public void setBlockBoundsForItemRender() {
        float f = 0.1875f;
        this.setBlockBounds(0.0f, 0.40625f, 0.0f, 1.0f, 0.59375f, 1.0f);
    }

    public void setBounds(IBlockState state) {
        if (state.getBlock() == this) {
            boolean flag = state.getValue(HALF) == DoorHalf.TOP;
            Boolean obool = (Boolean)state.getValue((IProperty)OPEN);
            EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
            float f = 0.1875f;
            if (flag) {
                this.setBlockBounds(0.0f, 0.8125f, 0.0f, 1.0f, 1.0f, 1.0f);
            } else {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.1875f, 1.0f);
            }
            if (obool.booleanValue()) {
                if (enumfacing == EnumFacing.NORTH) {
                    this.setBlockBounds(0.0f, 0.0f, 0.8125f, 1.0f, 1.0f, 1.0f);
                }
                if (enumfacing == EnumFacing.SOUTH) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1875f);
                }
                if (enumfacing == EnumFacing.WEST) {
                    this.setBlockBounds(0.8125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
                if (enumfacing == EnumFacing.EAST) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.1875f, 1.0f, 1.0f);
                }
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        state = state.cycleProperty((IProperty)OPEN);
        worldIn.setBlockState(pos, state, 2);
        worldIn.playAuxSFXAtEntity(playerIn, (Boolean)state.getValue((IProperty)OPEN) != false ? 1003 : 1006, pos, 0);
        return true;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
            if (!BlockTrapDoor.isValidSupportBlock(worldIn.getBlockState(blockpos).getBlock())) {
                worldIn.setBlockToAir(pos);
                this.dropBlockAsItem(worldIn, pos, state, 0);
            } else {
                boolean flag1;
                boolean flag = worldIn.isBlockPowered(pos);
                if ((flag || neighborBlock.canProvidePower()) && (flag1 = ((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) != flag) {
                    worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)flag)), 2);
                    worldIn.playAuxSFXAtEntity((EntityPlayer)null, flag ? 1003 : 1006, pos, 0);
                }
            }
        }
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState();
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf((boolean)false));
            iblockstate = iblockstate.withProperty(HALF, (Comparable)(hitY > 0.5f ? DoorHalf.TOP : DoorHalf.BOTTOM));
        }
        return iblockstate;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return !side.getAxis().isVertical() && BlockTrapDoor.isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }

    protected static EnumFacing getFacing(int meta) {
        switch (meta & 3) {
            case 0: {
                return EnumFacing.NORTH;
            }
            case 1: {
                return EnumFacing.SOUTH;
            }
            case 2: {
                return EnumFacing.WEST;
            }
        }
        return EnumFacing.EAST;
    }

    protected static int getMetaForFacing(EnumFacing facing) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                return 0;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 2;
            }
        }
        return 3;
    }

    private static boolean isValidSupportBlock(Block blockIn) {
        return blockIn.blockMaterial.isOpaque() && blockIn.isFullCube() || blockIn == Blocks.glowstone || blockIn instanceof BlockSlab || blockIn instanceof BlockStairs;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)BlockTrapDoor.getFacing(meta)).withProperty((IProperty)OPEN, (Comparable)Boolean.valueOf(((meta & 4) != 0 ? 1 : 0) != 0)).withProperty(HALF, (Comparable)((meta & 8) == 0 ? DoorHalf.BOTTOM : DoorHalf.TOP));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= BlockTrapDoor.getMetaForFacing((EnumFacing)state.getValue((IProperty)FACING));
        if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
            i |= 4;
        }
        if (state.getValue(HALF) == DoorHalf.TOP) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, OPEN, HALF});
    }
}
