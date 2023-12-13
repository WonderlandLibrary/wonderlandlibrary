// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockFenceGate extends BlockDirectional
{
    public static final PropertyBool field_176466_a;
    public static final PropertyBool field_176465_b;
    public static final PropertyBool field_176467_M;
    private static final String __OBFID = "CL_00000243";
    
    public BlockFenceGate() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFenceGate.field_176466_a, false).withProperty(BlockFenceGate.field_176465_b, false).withProperty(BlockFenceGate.field_176467_M, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing.Axis var4 = ((EnumFacing)state.getValue(BlockDirectional.AGE)).getAxis();
        if ((var4 == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.offsetWest()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.offsetEast()).getBlock() == Blocks.cobblestone_wall)) || (var4 == EnumFacing.Axis.X && (worldIn.getBlockState(pos.offsetNorth()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.offsetSouth()).getBlock() == Blocks.cobblestone_wall))) {
            state = state.withProperty(BlockFenceGate.field_176467_M, true);
        }
        return state;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid() && super.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue(BlockFenceGate.field_176466_a)) {
            return null;
        }
        final EnumFacing.Axis var4 = ((EnumFacing)state.getValue(BlockDirectional.AGE)).getAxis();
        return (var4 == EnumFacing.Axis.Z) ? new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + 0.375f, pos.getX() + 1, pos.getY() + 1.5f, pos.getZ() + 0.625f) : new AxisAlignedBB(pos.getX() + 0.375f, pos.getY(), pos.getZ(), pos.getX() + 0.625f, pos.getY() + 1.5f, pos.getZ() + 1);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final EnumFacing.Axis var3 = ((EnumFacing)access.getBlockState(pos).getValue(BlockDirectional.AGE)).getAxis();
        if (var3 == EnumFacing.Axis.Z) {
            this.setBlockBounds(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
        }
        else {
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
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return (boolean)blockAccess.getBlockState(pos).getValue(BlockFenceGate.field_176466_a);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, placer.func_174811_aO()).withProperty(BlockFenceGate.field_176466_a, false).withProperty(BlockFenceGate.field_176465_b, false).withProperty(BlockFenceGate.field_176467_M, false);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue(BlockFenceGate.field_176466_a)) {
            state = state.withProperty(BlockFenceGate.field_176466_a, false);
            worldIn.setBlockState(pos, state, 2);
        }
        else {
            final EnumFacing var9 = EnumFacing.fromAngle(playerIn.rotationYaw);
            if (state.getValue(BlockDirectional.AGE) == var9.getOpposite()) {
                state = state.withProperty(BlockDirectional.AGE, var9);
            }
            state = state.withProperty(BlockFenceGate.field_176466_a, true);
            worldIn.setBlockState(pos, state, 2);
        }
        worldIn.playAuxSFXAtEntity(playerIn, ((boolean)state.getValue(BlockFenceGate.field_176466_a)) ? 1003 : 1006, pos, 0);
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final boolean var5 = worldIn.isBlockPowered(pos);
            if (var5 || neighborBlock.canProvidePower()) {
                if (var5 && !(boolean)state.getValue(BlockFenceGate.field_176466_a) && !(boolean)state.getValue(BlockFenceGate.field_176465_b)) {
                    worldIn.setBlockState(pos, state.withProperty(BlockFenceGate.field_176466_a, true).withProperty(BlockFenceGate.field_176465_b, true), 2);
                    worldIn.playAuxSFXAtEntity(null, 1003, pos, 0);
                }
                else if (!var5 && (boolean)state.getValue(BlockFenceGate.field_176466_a) && (boolean)state.getValue(BlockFenceGate.field_176465_b)) {
                    worldIn.setBlockState(pos, state.withProperty(BlockFenceGate.field_176466_a, false).withProperty(BlockFenceGate.field_176465_b, false), 2);
                    worldIn.playAuxSFXAtEntity(null, 1006, pos, 0);
                }
                else if (var5 != (boolean)state.getValue(BlockFenceGate.field_176465_b)) {
                    worldIn.setBlockState(pos, state.withProperty(BlockFenceGate.field_176465_b, var5), 2);
                }
            }
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockFenceGate.field_176466_a, (meta & 0x4) != 0x0).withProperty(BlockFenceGate.field_176465_b, (meta & 0x8) != 0x0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        if (state.getValue(BlockFenceGate.field_176465_b)) {
            var3 |= 0x8;
        }
        if (state.getValue(BlockFenceGate.field_176466_a)) {
            var3 |= 0x4;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDirectional.AGE, BlockFenceGate.field_176466_a, BlockFenceGate.field_176465_b, BlockFenceGate.field_176467_M });
    }
    
    static {
        field_176466_a = PropertyBool.create("open");
        field_176465_b = PropertyBool.create("powered");
        field_176467_M = PropertyBool.create("in_wall");
    }
}
