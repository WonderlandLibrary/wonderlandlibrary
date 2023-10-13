/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine
extends Block {
    public static final PropertyBool field_176277_a = PropertyBool.create("up");
    public static final PropertyBool field_176273_b = PropertyBool.create("north");
    public static final PropertyBool field_176278_M = PropertyBool.create("east");
    public static final PropertyBool field_176279_N = PropertyBool.create("south");
    public static final PropertyBool field_176280_O = PropertyBool.create("west");
    public static final PropertyBool[] field_176274_P = new PropertyBool[]{field_176277_a, field_176273_b, field_176279_N, field_176280_O, field_176278_M};
    public static final int field_176272_Q = BlockVine.func_176270_b(EnumFacing.SOUTH);
    public static final int field_176276_R = BlockVine.func_176270_b(EnumFacing.NORTH);
    public static final int field_176275_S = BlockVine.func_176270_b(EnumFacing.EAST);
    public static final int field_176271_T = BlockVine.func_176270_b(EnumFacing.WEST);
    private static final String __OBFID = "CL_00000330";

    public BlockVine() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176277_a, Boolean.valueOf(false)).withProperty(field_176273_b, Boolean.valueOf(false)).withProperty(field_176278_M, Boolean.valueOf(false)).withProperty(field_176279_N, Boolean.valueOf(false)).withProperty(field_176280_O, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(field_176277_a, Boolean.valueOf(worldIn.getBlockState(pos.offsetUp()).getBlock().isSolidFullCube()));
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
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.0625f;
        float var4 = 1.0f;
        float var5 = 1.0f;
        float var6 = 1.0f;
        float var7 = 0.0f;
        float var8 = 0.0f;
        float var9 = 0.0f;
        boolean var10 = false;
        if (((Boolean)access.getBlockState(pos).getValue(field_176280_O)).booleanValue()) {
            var7 = Math.max(var7, 0.0625f);
            var4 = 0.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var6 = 0.0f;
            var9 = 1.0f;
            var10 = true;
        }
        if (((Boolean)access.getBlockState(pos).getValue(field_176278_M)).booleanValue()) {
            var4 = Math.min(var4, 0.9375f);
            var7 = 1.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var6 = 0.0f;
            var9 = 1.0f;
            var10 = true;
        }
        if (((Boolean)access.getBlockState(pos).getValue(field_176273_b)).booleanValue()) {
            var9 = Math.max(var9, 0.0625f);
            var6 = 0.0f;
            var4 = 0.0f;
            var7 = 1.0f;
            var5 = 0.0f;
            var8 = 1.0f;
            var10 = true;
        }
        if (((Boolean)access.getBlockState(pos).getValue(field_176279_N)).booleanValue()) {
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
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
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
        }
        return false;
    }

    private boolean func_150093_a(Block p_150093_1_) {
        return p_150093_1_.isFullCube() && p_150093_1_.blockMaterial.blocksMovement();
    }

    private boolean func_176269_e(World worldIn, BlockPos p_176269_2_, IBlockState p_176269_3_) {
        IBlockState var4 = p_176269_3_;
        for (EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState var8;
            PropertyBool var7 = BlockVine.func_176267_a(var6);
            if (!((Boolean)p_176269_3_.getValue(var7)).booleanValue() || this.func_150093_a(worldIn.getBlockState(p_176269_2_.offset(var6)).getBlock()) || (var8 = worldIn.getBlockState(p_176269_2_.offsetUp())).getBlock() == this && ((Boolean)var8.getValue(var7)).booleanValue()) continue;
            p_176269_3_ = p_176269_3_.withProperty(var7, Boolean.valueOf(false));
        }
        if (BlockVine.func_176268_d(p_176269_3_) == 0) {
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
    public int getRenderColor(IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180625_c(pos);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote && !this.func_176269_e(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
            int var5 = 4;
            int var6 = 5;
            boolean var7 = false;
            block0: for (int var8 = -var5; var8 <= var5; ++var8) {
                for (int var9 = -var5; var9 <= var5; ++var9) {
                    for (int var10 = -1; var10 <= 1; ++var10) {
                        if (worldIn.getBlockState(pos.add(var8, var10, var9)).getBlock() != this || --var6 > 0) continue;
                        var7 = true;
                        break block0;
                    }
                }
            }
            EnumFacing var17 = EnumFacing.random(rand);
            if (var17 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(pos.offsetUp())) {
                if (!var7) {
                    IBlockState var19 = state;
                    for (EnumFacing var23 : EnumFacing.Plane.HORIZONTAL) {
                        if (!rand.nextBoolean() && this.func_150093_a(worldIn.getBlockState(pos.offset(var23).offsetUp()).getBlock())) continue;
                        var19 = var19.withProperty(BlockVine.func_176267_a(var23), Boolean.valueOf(false));
                    }
                    if (((Boolean)var19.getValue(field_176273_b)).booleanValue() || ((Boolean)var19.getValue(field_176278_M)).booleanValue() || ((Boolean)var19.getValue(field_176279_N)).booleanValue() || ((Boolean)var19.getValue(field_176280_O)).booleanValue()) {
                        worldIn.setBlockState(pos.offsetUp(), var19, 2);
                    }
                }
            } else if (var17.getAxis().isHorizontal() && !((Boolean)state.getValue(BlockVine.func_176267_a(var17))).booleanValue()) {
                if (!var7) {
                    BlockPos var18 = pos.offset(var17);
                    Block var21 = worldIn.getBlockState(var18).getBlock();
                    if (var21.blockMaterial == Material.air) {
                        EnumFacing var23 = var17.rotateY();
                        EnumFacing var24 = var17.rotateYCCW();
                        boolean var25 = (Boolean)state.getValue(BlockVine.func_176267_a(var23));
                        boolean var26 = (Boolean)state.getValue(BlockVine.func_176267_a(var24));
                        BlockPos var27 = var18.offset(var23);
                        BlockPos var16 = var18.offset(var24);
                        if (var25 && this.func_150093_a(worldIn.getBlockState(var27).getBlock())) {
                            worldIn.setBlockState(var18, this.getDefaultState().withProperty(BlockVine.func_176267_a(var23), Boolean.valueOf(true)), 2);
                        } else if (var26 && this.func_150093_a(worldIn.getBlockState(var16).getBlock())) {
                            worldIn.setBlockState(var18, this.getDefaultState().withProperty(BlockVine.func_176267_a(var24), Boolean.valueOf(true)), 2);
                        } else if (var25 && worldIn.isAirBlock(var27) && this.func_150093_a(worldIn.getBlockState(pos.offset(var23)).getBlock())) {
                            worldIn.setBlockState(var27, this.getDefaultState().withProperty(BlockVine.func_176267_a(var17.getOpposite()), Boolean.valueOf(true)), 2);
                        } else if (var26 && worldIn.isAirBlock(var16) && this.func_150093_a(worldIn.getBlockState(pos.offset(var24)).getBlock())) {
                            worldIn.setBlockState(var16, this.getDefaultState().withProperty(BlockVine.func_176267_a(var17.getOpposite()), Boolean.valueOf(true)), 2);
                        } else if (this.func_150093_a(worldIn.getBlockState(var18.offsetUp()).getBlock())) {
                            worldIn.setBlockState(var18, this.getDefaultState(), 2);
                        }
                    } else if (var21.blockMaterial.isOpaque() && var21.isFullCube()) {
                        worldIn.setBlockState(pos, state.withProperty(BlockVine.func_176267_a(var17), Boolean.valueOf(true)), 2);
                    }
                }
            } else if (pos.getY() > 1) {
                BlockPos var18 = pos.offsetDown();
                IBlockState var20 = worldIn.getBlockState(var18);
                Block var11 = var20.getBlock();
                if (var11.blockMaterial == Material.air) {
                    IBlockState var12 = state;
                    for (EnumFacing var14 : EnumFacing.Plane.HORIZONTAL) {
                        if (!rand.nextBoolean()) continue;
                        var12 = var12.withProperty(BlockVine.func_176267_a(var14), Boolean.valueOf(false));
                    }
                    if (((Boolean)var12.getValue(field_176273_b)).booleanValue() || ((Boolean)var12.getValue(field_176278_M)).booleanValue() || ((Boolean)var12.getValue(field_176279_N)).booleanValue() || ((Boolean)var12.getValue(field_176280_O)).booleanValue()) {
                        worldIn.setBlockState(var18, var12, 2);
                    }
                } else if (var11 == this) {
                    IBlockState var12 = var20;
                    for (EnumFacing var14 : EnumFacing.Plane.HORIZONTAL) {
                        PropertyBool var15 = BlockVine.func_176267_a(var14);
                        if (!rand.nextBoolean() && ((Boolean)state.getValue(var15)).booleanValue()) continue;
                        var12 = var12.withProperty(var15, Boolean.valueOf(false));
                    }
                    if (((Boolean)var12.getValue(field_176273_b)).booleanValue() || ((Boolean)var12.getValue(field_176278_M)).booleanValue() || ((Boolean)var12.getValue(field_176279_N)).booleanValue() || ((Boolean)var12.getValue(field_176280_O)).booleanValue()) {
                        worldIn.setBlockState(var18, var12, 2);
                    }
                }
            }
        }
    }

    private static int func_176270_b(EnumFacing p_176270_0_) {
        return 1 << p_176270_0_.getHorizontalIndex();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = this.getDefaultState().withProperty(field_176277_a, Boolean.valueOf(false)).withProperty(field_176273_b, Boolean.valueOf(false)).withProperty(field_176278_M, Boolean.valueOf(false)).withProperty(field_176279_N, Boolean.valueOf(false)).withProperty(field_176280_O, Boolean.valueOf(false));
        return facing.getAxis().isHorizontal() ? var9.withProperty(BlockVine.func_176267_a(facing.getOpposite()), Boolean.valueOf(true)) : var9;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            BlockVine.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
        } else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176273_b, Boolean.valueOf((meta & field_176276_R) > 0)).withProperty(field_176278_M, Boolean.valueOf((meta & field_176275_S) > 0)).withProperty(field_176279_N, Boolean.valueOf((meta & field_176272_Q) > 0)).withProperty(field_176280_O, Boolean.valueOf((meta & field_176271_T) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        if (((Boolean)state.getValue(field_176273_b)).booleanValue()) {
            var2 |= field_176276_R;
        }
        if (((Boolean)state.getValue(field_176278_M)).booleanValue()) {
            var2 |= field_176275_S;
        }
        if (((Boolean)state.getValue(field_176279_N)).booleanValue()) {
            var2 |= field_176272_Q;
        }
        if (((Boolean)state.getValue(field_176280_O)).booleanValue()) {
            var2 |= field_176271_T;
        }
        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176277_a, field_176273_b, field_176278_M, field_176279_N, field_176280_O);
    }

    public static PropertyBool func_176267_a(EnumFacing p_176267_0_) {
        switch (SwitchEnumFacing.field_177057_a[p_176267_0_.ordinal()]) {
            case 1: {
                return field_176277_a;
            }
            case 2: {
                return field_176273_b;
            }
            case 3: {
                return field_176279_N;
            }
            case 4: {
                return field_176278_M;
            }
            case 5: {
                return field_176280_O;
            }
        }
        throw new IllegalArgumentException(p_176267_0_ + " is an invalid choice");
    }

    public static int func_176268_d(IBlockState p_176268_0_) {
        int var1 = 0;
        for (PropertyBool var5 : field_176274_P) {
            if (!((Boolean)p_176268_0_.getValue(var5)).booleanValue()) continue;
            ++var1;
        }
        return var1;
    }

    static final class SwitchEnumFacing {
        static final int[] field_177057_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002049";

        static {
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177057_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

