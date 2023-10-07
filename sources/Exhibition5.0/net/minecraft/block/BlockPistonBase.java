// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonBase extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool EXTENDED;
    private final boolean isSticky;
    private static final String __OBFID = "CL_00000366";
    
    public BlockPistonBase(final boolean p_i45443_1_) {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPistonBase.FACING, EnumFacing.NORTH).withProperty(BlockPistonBase.EXTENDED, false));
        this.isSticky = p_i45443_1_;
        this.setStepSound(Block.soundTypePiston);
        this.setHardness(0.5f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(BlockPistonBase.FACING, func_180695_a(worldIn, pos, placer)), 2);
        if (!worldIn.isRemote) {
            this.func_176316_e(worldIn, pos, state);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            this.func_176316_e(worldIn, pos, state);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            this.func_176316_e(worldIn, pos, state);
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockPistonBase.FACING, func_180695_a(worldIn, pos, placer)).withProperty(BlockPistonBase.EXTENDED, false);
    }
    
    private void func_176316_e(final World worldIn, final BlockPos p_176316_2_, final IBlockState p_176316_3_) {
        final EnumFacing var4 = (EnumFacing)p_176316_3_.getValue(BlockPistonBase.FACING);
        final boolean var5 = this.func_176318_b(worldIn, p_176316_2_, var4);
        if (var5 && !(boolean)p_176316_3_.getValue(BlockPistonBase.EXTENDED)) {
            if (new BlockPistonStructureHelper(worldIn, p_176316_2_, var4, true).func_177253_a()) {
                worldIn.addBlockEvent(p_176316_2_, this, 0, var4.getIndex());
            }
        }
        else if (!var5 && (boolean)p_176316_3_.getValue(BlockPistonBase.EXTENDED)) {
            worldIn.setBlockState(p_176316_2_, p_176316_3_.withProperty(BlockPistonBase.EXTENDED, false), 2);
            worldIn.addBlockEvent(p_176316_2_, this, 1, var4.getIndex());
        }
    }
    
    private boolean func_176318_b(final World worldIn, final BlockPos p_176318_2_, final EnumFacing p_176318_3_) {
        for (final EnumFacing var7 : EnumFacing.values()) {
            if (var7 != p_176318_3_ && worldIn.func_175709_b(p_176318_2_.offset(var7), var7)) {
                return true;
            }
        }
        if (worldIn.func_175709_b(p_176318_2_, EnumFacing.NORTH)) {
            return true;
        }
        final BlockPos var8 = p_176318_2_.offsetUp();
        for (final EnumFacing var11 : EnumFacing.values()) {
            if (var11 != EnumFacing.DOWN && worldIn.func_175709_b(var8.offset(var11), var11)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        final EnumFacing var6 = (EnumFacing)state.getValue(BlockPistonBase.FACING);
        if (!worldIn.isRemote) {
            final boolean var7 = this.func_176318_b(worldIn, pos, var6);
            if (var7 && eventID == 1) {
                worldIn.setBlockState(pos, state.withProperty(BlockPistonBase.EXTENDED, true), 2);
                return false;
            }
            if (!var7 && eventID == 0) {
                return false;
            }
        }
        if (eventID == 0) {
            if (!this.func_176319_a(worldIn, pos, var6, true)) {
                return false;
            }
            worldIn.setBlockState(pos, state.withProperty(BlockPistonBase.EXTENDED, true), 2);
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "tile.piston.out", 0.5f, worldIn.rand.nextFloat() * 0.25f + 0.6f);
        }
        else if (eventID == 1) {
            final TileEntity var8 = worldIn.getTileEntity(pos.offset(var6));
            if (var8 instanceof TileEntityPiston) {
                ((TileEntityPiston)var8).clearPistonTileEntity();
            }
            worldIn.setBlockState(pos, Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.field_176426_a, var6).withProperty(BlockPistonMoving.field_176425_b, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.func_176423_a(this.getStateFromMeta(eventParam), var6, false, true));
            if (this.isSticky) {
                final BlockPos var9 = pos.add(var6.getFrontOffsetX() * 2, var6.getFrontOffsetY() * 2, var6.getFrontOffsetZ() * 2);
                final Block var10 = worldIn.getBlockState(var9).getBlock();
                boolean var11 = false;
                if (var10 == Blocks.piston_extension) {
                    final TileEntity var12 = worldIn.getTileEntity(var9);
                    if (var12 instanceof TileEntityPiston) {
                        final TileEntityPiston var13 = (TileEntityPiston)var12;
                        if (var13.func_174930_e() == var6 && var13.isExtending()) {
                            var13.clearPistonTileEntity();
                            var11 = true;
                        }
                    }
                }
                if (!var11 && var10.getMaterial() != Material.air && func_180696_a(var10, worldIn, var9, var6.getOpposite(), false) && (var10.getMobilityFlag() == 0 || var10 == Blocks.piston || var10 == Blocks.sticky_piston)) {
                    this.func_176319_a(worldIn, pos, var6, false);
                }
            }
            else {
                worldIn.setBlockToAir(pos.offset(var6));
            }
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "tile.piston.in", 0.5f, worldIn.rand.nextFloat() * 0.15f + 0.6f);
        }
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final IBlockState var3 = access.getBlockState(pos);
        if (var3.getBlock() == this && (boolean)var3.getValue(BlockPistonBase.EXTENDED)) {
            final float var4 = 0.25f;
            final EnumFacing var5 = (EnumFacing)var3.getValue(BlockPistonBase.FACING);
            if (var5 != null) {
                switch (SwitchEnumFacing.field_177243_a[var5.ordinal()]) {
                    case 1: {
                        this.setBlockBounds(0.0f, 0.25f, 0.0f, 1.0f, 1.0f, 1.0f);
                        break;
                    }
                    case 2: {
                        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
                        break;
                    }
                    case 3: {
                        this.setBlockBounds(0.0f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                        break;
                    }
                    case 4: {
                        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                        break;
                    }
                    case 5: {
                        this.setBlockBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                        break;
                    }
                    case 6: {
                        this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 1.0f);
                        break;
                    }
                }
            }
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    public static EnumFacing func_176317_b(final int p_176317_0_) {
        final int var1 = p_176317_0_ & 0x7;
        return (var1 > 5) ? null : EnumFacing.getFront(var1);
    }
    
    public static EnumFacing func_180695_a(final World worldIn, final BlockPos p_180695_1_, final EntityLivingBase p_180695_2_) {
        if (MathHelper.abs((float)p_180695_2_.posX - p_180695_1_.getX()) < 2.0f && MathHelper.abs((float)p_180695_2_.posZ - p_180695_1_.getZ()) < 2.0f) {
            final double var3 = p_180695_2_.posY + p_180695_2_.getEyeHeight();
            if (var3 - p_180695_1_.getY() > 2.0) {
                return EnumFacing.UP;
            }
            if (p_180695_1_.getY() - var3 > 0.0) {
                return EnumFacing.DOWN;
            }
        }
        return p_180695_2_.func_174811_aO().getOpposite();
    }
    
    public static boolean func_180696_a(final Block p_180696_0_, final World worldIn, final BlockPos p_180696_2_, final EnumFacing p_180696_3_, final boolean p_180696_4_) {
        if (p_180696_0_ == Blocks.obsidian) {
            return false;
        }
        if (!worldIn.getWorldBorder().contains(p_180696_2_)) {
            return false;
        }
        if (p_180696_2_.getY() < 0 || (p_180696_3_ == EnumFacing.DOWN && p_180696_2_.getY() == 0)) {
            return false;
        }
        if (p_180696_2_.getY() <= worldIn.getHeight() - 1 && (p_180696_3_ != EnumFacing.UP || p_180696_2_.getY() != worldIn.getHeight() - 1)) {
            if (p_180696_0_ != Blocks.piston && p_180696_0_ != Blocks.sticky_piston) {
                if (p_180696_0_.getBlockHardness(worldIn, p_180696_2_) == -1.0f) {
                    return false;
                }
                if (p_180696_0_.getMobilityFlag() == 2) {
                    return false;
                }
                if (p_180696_0_.getMobilityFlag() == 1) {
                    return p_180696_4_;
                }
            }
            else if (worldIn.getBlockState(p_180696_2_).getValue(BlockPistonBase.EXTENDED)) {
                return false;
            }
            return !(p_180696_0_ instanceof ITileEntityProvider);
        }
        return false;
    }
    
    private boolean func_176319_a(final World worldIn, final BlockPos p_176319_2_, final EnumFacing p_176319_3_, final boolean p_176319_4_) {
        if (!p_176319_4_) {
            worldIn.setBlockToAir(p_176319_2_.offset(p_176319_3_));
        }
        final BlockPistonStructureHelper var5 = new BlockPistonStructureHelper(worldIn, p_176319_2_, p_176319_3_, p_176319_4_);
        final List var6 = var5.func_177254_c();
        final List var7 = var5.func_177252_d();
        if (!var5.func_177253_a()) {
            return false;
        }
        int var8 = var6.size() + var7.size();
        final Block[] var9 = new Block[var8];
        final EnumFacing var10 = p_176319_4_ ? p_176319_3_ : p_176319_3_.getOpposite();
        for (int var11 = var7.size() - 1; var11 >= 0; --var11) {
            final BlockPos var12 = var7.get(var11);
            final Block var13 = worldIn.getBlockState(var12).getBlock();
            var13.dropBlockAsItem(worldIn, var12, worldIn.getBlockState(var12), 0);
            worldIn.setBlockToAir(var12);
            --var8;
            var9[var8] = var13;
        }
        for (int var11 = var6.size() - 1; var11 >= 0; --var11) {
            BlockPos var12 = var6.get(var11);
            final IBlockState var14 = worldIn.getBlockState(var12);
            final Block var15 = var14.getBlock();
            var15.getMetaFromState(var14);
            worldIn.setBlockToAir(var12);
            var12 = var12.offset(var10);
            worldIn.setBlockState(var12, Blocks.piston_extension.getDefaultState().withProperty(BlockPistonBase.FACING, p_176319_3_), 4);
            worldIn.setTileEntity(var12, BlockPistonMoving.func_176423_a(var14, p_176319_3_, p_176319_4_, false));
            --var8;
            var9[var8] = var15;
        }
        final BlockPos var16 = p_176319_2_.offset(p_176319_3_);
        if (p_176319_4_) {
            final BlockPistonExtension.EnumPistonType var17 = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
            final IBlockState var14 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.field_176326_a, p_176319_3_).withProperty(BlockPistonExtension.field_176325_b, var17);
            final IBlockState var18 = Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.field_176426_a, p_176319_3_).withProperty(BlockPistonMoving.field_176425_b, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
            worldIn.setBlockState(var16, var18, 4);
            worldIn.setTileEntity(var16, BlockPistonMoving.func_176423_a(var14, p_176319_3_, true, false));
        }
        for (int var19 = var7.size() - 1; var19 >= 0; --var19) {
            worldIn.notifyNeighborsOfStateChange(var7.get(var19), var9[var8++]);
        }
        for (int var19 = var6.size() - 1; var19 >= 0; --var19) {
            worldIn.notifyNeighborsOfStateChange(var6.get(var19), var9[var8++]);
        }
        if (p_176319_4_) {
            worldIn.notifyNeighborsOfStateChange(var16, Blocks.piston_head);
            worldIn.notifyNeighborsOfStateChange(p_176319_2_, this);
        }
        return true;
    }
    
    @Override
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return this.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.UP);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPistonBase.FACING, func_176317_b(meta)).withProperty(BlockPistonBase.EXTENDED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockPistonBase.FACING)).getIndex();
        if (state.getValue(BlockPistonBase.EXTENDED)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPistonBase.FACING, BlockPistonBase.EXTENDED });
    }
    
    static {
        FACING = PropertyDirection.create("facing");
        EXTENDED = PropertyBool.create("extended");
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177243_a;
        private static final String __OBFID = "CL_00002037";
        
        static {
            field_177243_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177243_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177243_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177243_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177243_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_177243_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_177243_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
