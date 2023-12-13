// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import java.util.Random;
import net.minecraft.world.ColorizerFoliage;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockVine extends Block
{
    public static final PropertyBool field_176277_a;
    public static final PropertyBool field_176273_b;
    public static final PropertyBool field_176278_M;
    public static final PropertyBool field_176279_N;
    public static final PropertyBool field_176280_O;
    public static final PropertyBool[] field_176274_P;
    public static final int field_176272_Q;
    public static final int field_176276_R;
    public static final int field_176275_S;
    public static final int field_176271_T;
    private static final String __OBFID = "CL_00000330";
    
    public BlockVine() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockVine.field_176277_a, false).withProperty(BlockVine.field_176273_b, false).withProperty(BlockVine.field_176278_M, false).withProperty(BlockVine.field_176279_N, false).withProperty(BlockVine.field_176280_O, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockVine.field_176277_a, worldIn.getBlockState(pos.offsetUp()).getBlock().isSolidFullCube());
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
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
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final float var3 = 0.0625f;
        float var4 = 1.0f;
        float var5 = 1.0f;
        float var6 = 1.0f;
        float var7 = 0.0f;
        float var8 = 0.0f;
        float var9 = 0.0f;
        boolean var10 = false;
        if (access.getBlockState(pos).getValue(BlockVine.field_176280_O)) {
            var7 = Math.max(var7, 0.0625f);
            var4 = 0.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var6 = 0.0f;
            var9 = 1.0f;
            var10 = true;
        }
        if (access.getBlockState(pos).getValue(BlockVine.field_176278_M)) {
            var4 = Math.min(var4, 0.9375f);
            var7 = 1.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var6 = 0.0f;
            var9 = 1.0f;
            var10 = true;
        }
        if (access.getBlockState(pos).getValue(BlockVine.field_176273_b)) {
            var9 = Math.max(var9, 0.0625f);
            var6 = 0.0f;
            var4 = 0.0f;
            var7 = 1.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var10 = true;
        }
        if (access.getBlockState(pos).getValue(BlockVine.field_176279_N)) {
            var6 = Math.min(var6, 0.9375f);
            var9 = 1.0f;
            var4 = 0.0f;
            var7 = 1.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var10 = true;
        }
        if (!var10 && this.func_150093_a(access.getBlockState(pos.offsetUp()).getBlock())) {
            var5 = Math.min(var5, 0.9375f);
            var8 = 1.0f;
            var4 = 0.0f;
            var7 = 1.0f;
            var6 = 0.0f;
            var9 = 1.0f;
        }
        this.setBlockBounds(var4, var5, var6, var7, var8, var9);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        switch (SwitchEnumFacing.field_177057_a[side.ordinal()]) {
            case 1: {
                return this.func_150093_a(worldIn.getBlockState(pos.offsetUp()).getBlock());
            }
            case 2:
            case 3:
            case 4:
            case 5: {
                return this.func_150093_a(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean func_150093_a(final Block p_150093_1_) {
        return p_150093_1_.isFullCube() && p_150093_1_.blockMaterial.blocksMovement();
    }
    
    private boolean func_176269_e(final World worldIn, final BlockPos p_176269_2_, IBlockState p_176269_3_) {
        final IBlockState var4 = p_176269_3_;
        for (final EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            final PropertyBool var7 = func_176267_a(var6);
            if ((boolean)p_176269_3_.getValue(var7) && !this.func_150093_a(worldIn.getBlockState(p_176269_2_.offset(var6)).getBlock())) {
                final IBlockState var8 = worldIn.getBlockState(p_176269_2_.offsetUp());
                if (var8.getBlock() == this && (boolean)var8.getValue(var7)) {
                    continue;
                }
                p_176269_3_ = p_176269_3_.withProperty(var7, false);
            }
        }
        if (func_176268_d(p_176269_3_) == 0) {
            return false;
        }
        if (var4 != p_176269_3_) {
            worldIn.setBlockState(p_176269_2_, p_176269_3_, 2);
        }
        return true;
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180625_c(pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote && !this.func_176269_e(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
            final byte var5 = 4;
            int var6 = 5;
            boolean var7 = false;
        Label_0115:
            for (int var8 = -var5; var8 <= var5; ++var8) {
                for (int var9 = -var5; var9 <= var5; ++var9) {
                    for (int var10 = -1; var10 <= 1; ++var10) {
                        if (worldIn.getBlockState(pos.add(var8, var10, var9)).getBlock() == this && --var6 <= 0) {
                            var7 = true;
                            break Label_0115;
                        }
                    }
                }
            }
            final EnumFacing var11 = EnumFacing.random(rand);
            if (var11 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(pos.offsetUp())) {
                if (!var7) {
                    IBlockState var12 = state;
                    for (final EnumFacing var14 : EnumFacing.Plane.HORIZONTAL) {
                        if (rand.nextBoolean() || !this.func_150093_a(worldIn.getBlockState(pos.offset(var14).offsetUp()).getBlock())) {
                            var12 = var12.withProperty(func_176267_a(var14), false);
                        }
                    }
                    if ((boolean)var12.getValue(BlockVine.field_176273_b) || (boolean)var12.getValue(BlockVine.field_176278_M) || (boolean)var12.getValue(BlockVine.field_176279_N) || (boolean)var12.getValue(BlockVine.field_176280_O)) {
                        worldIn.setBlockState(pos.offsetUp(), var12, 2);
                    }
                }
            }
            else if (var11.getAxis().isHorizontal() && !(boolean)state.getValue(func_176267_a(var11))) {
                if (!var7) {
                    final BlockPos var15 = pos.offset(var11);
                    final Block var16 = worldIn.getBlockState(var15).getBlock();
                    if (var16.blockMaterial == Material.air) {
                        final EnumFacing var14 = var11.rotateY();
                        final EnumFacing var17 = var11.rotateYCCW();
                        final boolean var18 = (boolean)state.getValue(func_176267_a(var14));
                        final boolean var19 = (boolean)state.getValue(func_176267_a(var17));
                        final BlockPos var20 = var15.offset(var14);
                        final BlockPos var21 = var15.offset(var17);
                        if (var18 && this.func_150093_a(worldIn.getBlockState(var20).getBlock())) {
                            worldIn.setBlockState(var15, this.getDefaultState().withProperty(func_176267_a(var14), true), 2);
                        }
                        else if (var19 && this.func_150093_a(worldIn.getBlockState(var21).getBlock())) {
                            worldIn.setBlockState(var15, this.getDefaultState().withProperty(func_176267_a(var17), true), 2);
                        }
                        else if (var18 && worldIn.isAirBlock(var20) && this.func_150093_a(worldIn.getBlockState(pos.offset(var14)).getBlock())) {
                            worldIn.setBlockState(var20, this.getDefaultState().withProperty(func_176267_a(var11.getOpposite()), true), 2);
                        }
                        else if (var19 && worldIn.isAirBlock(var21) && this.func_150093_a(worldIn.getBlockState(pos.offset(var17)).getBlock())) {
                            worldIn.setBlockState(var21, this.getDefaultState().withProperty(func_176267_a(var11.getOpposite()), true), 2);
                        }
                        else if (this.func_150093_a(worldIn.getBlockState(var15.offsetUp()).getBlock())) {
                            worldIn.setBlockState(var15, this.getDefaultState(), 2);
                        }
                    }
                    else if (var16.blockMaterial.isOpaque() && var16.isFullCube()) {
                        worldIn.setBlockState(pos, state.withProperty(func_176267_a(var11), true), 2);
                    }
                }
            }
            else if (pos.getY() > 1) {
                final BlockPos var15 = pos.offsetDown();
                final IBlockState var22 = worldIn.getBlockState(var15);
                final Block var23 = var22.getBlock();
                if (var23.blockMaterial == Material.air) {
                    IBlockState var24 = state;
                    for (final EnumFacing var26 : EnumFacing.Plane.HORIZONTAL) {
                        if (rand.nextBoolean()) {
                            var24 = var24.withProperty(func_176267_a(var26), false);
                        }
                    }
                    if ((boolean)var24.getValue(BlockVine.field_176273_b) || (boolean)var24.getValue(BlockVine.field_176278_M) || (boolean)var24.getValue(BlockVine.field_176279_N) || (boolean)var24.getValue(BlockVine.field_176280_O)) {
                        worldIn.setBlockState(var15, var24, 2);
                    }
                }
                else if (var23 == this) {
                    IBlockState var24 = var22;
                    for (final EnumFacing var26 : EnumFacing.Plane.HORIZONTAL) {
                        final PropertyBool var27 = func_176267_a(var26);
                        if (rand.nextBoolean() || !(boolean)state.getValue(var27)) {
                            var24 = var24.withProperty(var27, false);
                        }
                    }
                    if ((boolean)var24.getValue(BlockVine.field_176273_b) || (boolean)var24.getValue(BlockVine.field_176278_M) || (boolean)var24.getValue(BlockVine.field_176279_N) || (boolean)var24.getValue(BlockVine.field_176280_O)) {
                        worldIn.setBlockState(var15, var24, 2);
                    }
                }
            }
        }
    }
    
    private static int func_176270_b(final EnumFacing p_176270_0_) {
        return 1 << p_176270_0_.getHorizontalIndex();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState var9 = this.getDefaultState().withProperty(BlockVine.field_176277_a, false).withProperty(BlockVine.field_176273_b, false).withProperty(BlockVine.field_176278_M, false).withProperty(BlockVine.field_176279_N, false).withProperty(BlockVine.field_176280_O, false);
        return facing.getAxis().isHorizontal() ? var9.withProperty(func_176267_a(facing.getOpposite()), true) : var9;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
        }
        else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockVine.field_176273_b, (meta & BlockVine.field_176276_R) > 0).withProperty(BlockVine.field_176278_M, (meta & BlockVine.field_176275_S) > 0).withProperty(BlockVine.field_176279_N, (meta & BlockVine.field_176272_Q) > 0).withProperty(BlockVine.field_176280_O, (meta & BlockVine.field_176271_T) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        if (state.getValue(BlockVine.field_176273_b)) {
            var2 |= BlockVine.field_176276_R;
        }
        if (state.getValue(BlockVine.field_176278_M)) {
            var2 |= BlockVine.field_176275_S;
        }
        if (state.getValue(BlockVine.field_176279_N)) {
            var2 |= BlockVine.field_176272_Q;
        }
        if (state.getValue(BlockVine.field_176280_O)) {
            var2 |= BlockVine.field_176271_T;
        }
        return var2;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockVine.field_176277_a, BlockVine.field_176273_b, BlockVine.field_176278_M, BlockVine.field_176279_N, BlockVine.field_176280_O });
    }
    
    public static PropertyBool func_176267_a(final EnumFacing p_176267_0_) {
        switch (SwitchEnumFacing.field_177057_a[p_176267_0_.ordinal()]) {
            case 1: {
                return BlockVine.field_176277_a;
            }
            case 2: {
                return BlockVine.field_176273_b;
            }
            case 3: {
                return BlockVine.field_176279_N;
            }
            case 4: {
                return BlockVine.field_176278_M;
            }
            case 5: {
                return BlockVine.field_176280_O;
            }
            default: {
                throw new IllegalArgumentException(p_176267_0_ + " is an invalid choice");
            }
        }
    }
    
    public static int func_176268_d(final IBlockState p_176268_0_) {
        int var1 = 0;
        for (final PropertyBool var5 : BlockVine.field_176274_P) {
            if (p_176268_0_.getValue(var5)) {
                ++var1;
            }
        }
        return var1;
    }
    
    static {
        field_176277_a = PropertyBool.create("up");
        field_176273_b = PropertyBool.create("north");
        field_176278_M = PropertyBool.create("east");
        field_176279_N = PropertyBool.create("south");
        field_176280_O = PropertyBool.create("west");
        field_176274_P = new PropertyBool[] { BlockVine.field_176277_a, BlockVine.field_176273_b, BlockVine.field_176279_N, BlockVine.field_176280_O, BlockVine.field_176278_M };
        field_176272_Q = func_176270_b(EnumFacing.SOUTH);
        field_176276_R = func_176270_b(EnumFacing.NORTH);
        field_176275_S = func_176270_b(EnumFacing.EAST);
        field_176271_T = func_176270_b(EnumFacing.WEST);
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177057_a;
        private static final String __OBFID = "CL_00002049";
        
        static {
            field_177057_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
        }
    }
}
