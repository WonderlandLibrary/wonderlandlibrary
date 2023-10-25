package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockLever
extends Block {
    public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create((String)"facing", EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");

    protected BlockLever() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, (Comparable)EnumOrientation.NORTH).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return BlockLever.func_181090_a(worldIn, pos, side.getOpposite());
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (!BlockLever.func_181090_a(worldIn, pos, enumfacing)) continue;
            return true;
        }
        return false;
    }

    protected static boolean func_181090_a(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
        return BlockButton.func_181088_a((World)p_181090_0_, (BlockPos)p_181090_1_, (EnumFacing)p_181090_2_);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false));
        if (BlockLever.func_181090_a(worldIn, pos, facing.getOpposite())) {
            return iblockstate.withProperty(FACING, (Comparable)EnumOrientation.forFacings((EnumFacing)facing, (EnumFacing)placer.getHorizontalFacing()));
        }
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing == facing || !BlockLever.func_181090_a(worldIn, pos, enumfacing.getOpposite())) continue;
            return iblockstate.withProperty(FACING, (Comparable)EnumOrientation.forFacings((EnumFacing)enumfacing, (EnumFacing)placer.getHorizontalFacing()));
        }
        if (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down())) {
            return iblockstate.withProperty(FACING, (Comparable)EnumOrientation.forFacings((EnumFacing)EnumFacing.UP, (EnumFacing)placer.getHorizontalFacing()));
        }
        return iblockstate;
    }

    public static int getMetadataForFacing(EnumFacing facing) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                return 0;
            }
            case 2: {
                return 5;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 3;
            }
            case 5: {
                return 2;
            }
            case 6: {
                return 1;
            }
        }
        return -1;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (this.func_181091_e(worldIn, pos, state) && !BlockLever.func_181090_a(worldIn, pos, ((EnumOrientation)state.getValue(FACING)).getFacing().getOpposite())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean func_181091_e(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
        if (this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
            return true;
        }
        this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
        p_181091_1_.setBlockToAir(p_181091_2_);
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        float f = 0.1875f;
        switch (1.$SwitchMap$net$minecraft$block$BlockLever$EnumOrientation[((EnumOrientation)worldIn.getBlockState(pos).getValue(FACING)).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                break;
            }
            case 2: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
                break;
            }
            case 5: 
            case 6: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
                break;
            }
            case 7: 
            case 8: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.4f, 0.5f - f, 0.5f + f, 1.0f, 0.5f + f);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty((IProperty)POWERED);
        worldIn.setBlockState(pos, state, 3);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, (Boolean)state.getValue((IProperty)POWERED) != false ? 0.6f : 0.5f);
        worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
        EnumFacing enumfacing = ((EnumOrientation)state.getValue(FACING)).getFacing();
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), (Block)this);
        return true;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
            EnumFacing enumfacing = ((EnumOrientation)state.getValue(FACING)).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), (Block)this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue((IProperty)POWERED) != false ? 15 : 0;
    }

    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue((IProperty)POWERED) == false ? 0 : (((EnumOrientation)state.getValue(FACING)).getFacing() == side ? 15 : 0);
    }

    public boolean canProvidePower() {
        return true;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, (Comparable)EnumOrientation.byMetadata((int)(meta & 7))).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumOrientation)state.getValue(FACING)).getMetadata();
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, POWERED});
    }
}
