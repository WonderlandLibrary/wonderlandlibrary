// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection field_176426_a;
    public static final PropertyEnum field_176425_b;
    private static final String __OBFID = "CL_00000368";
    
    public BlockPistonMoving() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPistonMoving.field_176426_a, EnumFacing.NORTH).withProperty(BlockPistonMoving.field_176425_b, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0f);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return null;
    }
    
    public static TileEntity func_176423_a(final IBlockState p_176423_0_, final EnumFacing p_176423_1_, final boolean p_176423_2_, final boolean p_176423_3_) {
        return new TileEntityPiston(p_176423_0_, p_176423_1_, p_176423_2_, p_176423_3_);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof TileEntityPiston) {
            ((TileEntityPiston)var4).clearPistonTileEntity();
        }
        else {
            super.breakBlock(worldIn, pos, state);
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        final BlockPos var4 = pos.offset(((EnumFacing)state.getValue(BlockPistonMoving.field_176426_a)).getOpposite());
        final IBlockState var5 = worldIn.getBlockState(var4);
        if (var5.getBlock() instanceof BlockPistonBase && (boolean)var5.getValue(BlockPistonBase.EXTENDED)) {
            worldIn.setBlockToAir(var4);
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
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            worldIn.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            final TileEntityPiston var6 = this.func_176422_e(worldIn, pos);
            if (var6 != null) {
                final IBlockState var7 = var6.func_174927_b();
                var7.getBlock().dropBlockAsItem(worldIn, pos, var7, 0);
            }
        }
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        return null;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            worldIn.getTileEntity(pos);
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntityPiston var4 = this.func_176422_e(worldIn, pos);
        if (var4 == null) {
            return null;
        }
        float var5 = var4.func_145860_a(0.0f);
        if (var4.isExtending()) {
            var5 = 1.0f - var5;
        }
        return this.func_176424_a(worldIn, pos, var4.func_174927_b(), var5, var4.func_174930_e());
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final TileEntityPiston var3 = this.func_176422_e(access, pos);
        if (var3 != null) {
            final IBlockState var4 = var3.func_174927_b();
            final Block var5 = var4.getBlock();
            if (var5 == this || var5.getMaterial() == Material.air) {
                return;
            }
            float var6 = var3.func_145860_a(0.0f);
            if (var3.isExtending()) {
                var6 = 1.0f - var6;
            }
            var5.setBlockBoundsBasedOnState(access, pos);
            if (var5 == Blocks.piston || var5 == Blocks.sticky_piston) {
                var6 = 0.0f;
            }
            final EnumFacing var7 = var3.func_174930_e();
            this.minX = var5.getBlockBoundsMinX() - var7.getFrontOffsetX() * var6;
            this.minY = var5.getBlockBoundsMinY() - var7.getFrontOffsetY() * var6;
            this.minZ = var5.getBlockBoundsMinZ() - var7.getFrontOffsetZ() * var6;
            this.maxX = var5.getBlockBoundsMaxX() - var7.getFrontOffsetX() * var6;
            this.maxY = var5.getBlockBoundsMaxY() - var7.getFrontOffsetY() * var6;
            this.maxZ = var5.getBlockBoundsMaxZ() - var7.getFrontOffsetZ() * var6;
        }
    }
    
    public AxisAlignedBB func_176424_a(final World worldIn, final BlockPos p_176424_2_, final IBlockState p_176424_3_, final float p_176424_4_, final EnumFacing p_176424_5_) {
        if (p_176424_3_.getBlock() == this || p_176424_3_.getBlock().getMaterial() == Material.air) {
            return null;
        }
        final AxisAlignedBB var6 = p_176424_3_.getBlock().getCollisionBoundingBox(worldIn, p_176424_2_, p_176424_3_);
        if (var6 == null) {
            return null;
        }
        double var7 = var6.minX;
        double var8 = var6.minY;
        double var9 = var6.minZ;
        double var10 = var6.maxX;
        double var11 = var6.maxY;
        double var12 = var6.maxZ;
        if (p_176424_5_.getFrontOffsetX() < 0) {
            var7 -= p_176424_5_.getFrontOffsetX() * p_176424_4_;
        }
        else {
            var10 -= p_176424_5_.getFrontOffsetX() * p_176424_4_;
        }
        if (p_176424_5_.getFrontOffsetY() < 0) {
            var8 -= p_176424_5_.getFrontOffsetY() * p_176424_4_;
        }
        else {
            var11 -= p_176424_5_.getFrontOffsetY() * p_176424_4_;
        }
        if (p_176424_5_.getFrontOffsetZ() < 0) {
            var9 -= p_176424_5_.getFrontOffsetZ() * p_176424_4_;
        }
        else {
            var12 -= p_176424_5_.getFrontOffsetZ() * p_176424_4_;
        }
        return new AxisAlignedBB(var7, var8, var9, var10, var11, var12);
    }
    
    private TileEntityPiston func_176422_e(final IBlockAccess p_176422_1_, final BlockPos p_176422_2_) {
        final TileEntity var3 = p_176422_1_.getTileEntity(p_176422_2_);
        return (var3 instanceof TileEntityPiston) ? ((TileEntityPiston)var3) : null;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return null;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPistonMoving.field_176426_a, BlockPistonExtension.func_176322_b(meta)).withProperty(BlockPistonMoving.field_176425_b, ((meta & 0x8) > 0) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockPistonMoving.field_176426_a)).getIndex();
        if (state.getValue(BlockPistonMoving.field_176425_b) == BlockPistonExtension.EnumPistonType.STICKY) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPistonMoving.field_176426_a, BlockPistonMoving.field_176425_b });
    }
    
    static {
        field_176426_a = BlockPistonExtension.field_176326_a;
        field_176425_b = BlockPistonExtension.field_176325_b;
    }
}
