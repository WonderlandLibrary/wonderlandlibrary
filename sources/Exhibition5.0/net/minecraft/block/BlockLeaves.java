// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public abstract class BlockLeaves extends BlockLeavesBase
{
    public static final PropertyBool field_176237_a;
    public static final PropertyBool field_176236_b;
    int[] field_150128_a;
    protected int field_150127_b;
    protected boolean field_176238_O;
    private static final String __OBFID = "CL_00000263";
    
    public BlockLeaves() {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2f);
        this.setLightOpacity(1);
        this.setStepSound(Block.soundTypeGrass);
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColor(0.5, 1.0);
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return BiomeColorHelper.func_180287_b(worldIn, pos);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final byte var4 = 1;
        final int var5 = var4 + 1;
        final int var6 = pos.getX();
        final int var7 = pos.getY();
        final int var8 = pos.getZ();
        if (worldIn.isAreaLoaded(new BlockPos(var6 - var5, var7 - var5, var8 - var5), new BlockPos(var6 + var5, var7 + var5, var8 + var5))) {
            for (int var9 = -var4; var9 <= var4; ++var9) {
                for (int var10 = -var4; var10 <= var4; ++var10) {
                    for (int var11 = -var4; var11 <= var4; ++var11) {
                        final BlockPos var12 = pos.add(var9, var10, var11);
                        final IBlockState var13 = worldIn.getBlockState(var12);
                        if (var13.getBlock().getMaterial() == Material.leaves && !(boolean)var13.getValue(BlockLeaves.field_176236_b)) {
                            worldIn.setBlockState(var12, var13.withProperty(BlockLeaves.field_176236_b, true), 4);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && (boolean)state.getValue(BlockLeaves.field_176236_b) && (boolean)state.getValue(BlockLeaves.field_176237_a)) {
            final byte var5 = 4;
            final int var6 = var5 + 1;
            final int var7 = pos.getX();
            final int var8 = pos.getY();
            final int var9 = pos.getZ();
            final byte var10 = 32;
            final int var11 = var10 * var10;
            final int var12 = var10 / 2;
            if (this.field_150128_a == null) {
                this.field_150128_a = new int[var10 * var10 * var10];
            }
            if (worldIn.isAreaLoaded(new BlockPos(var7 - var6, var8 - var6, var9 - var6), new BlockPos(var7 + var6, var8 + var6, var9 + var6))) {
                for (int var13 = -var5; var13 <= var5; ++var13) {
                    for (int var14 = -var5; var14 <= var5; ++var14) {
                        for (int var15 = -var5; var15 <= var5; ++var15) {
                            final Block var16 = worldIn.getBlockState(new BlockPos(var7 + var13, var8 + var14, var9 + var15)).getBlock();
                            if (var16 != Blocks.log && var16 != Blocks.log2) {
                                if (var16.getMaterial() == Material.leaves) {
                                    this.field_150128_a[(var13 + var12) * var11 + (var14 + var12) * var10 + var15 + var12] = -2;
                                }
                                else {
                                    this.field_150128_a[(var13 + var12) * var11 + (var14 + var12) * var10 + var15 + var12] = -1;
                                }
                            }
                            else {
                                this.field_150128_a[(var13 + var12) * var11 + (var14 + var12) * var10 + var15 + var12] = 0;
                            }
                        }
                    }
                }
                for (int var13 = 1; var13 <= 4; ++var13) {
                    for (int var14 = -var5; var14 <= var5; ++var14) {
                        for (int var15 = -var5; var15 <= var5; ++var15) {
                            for (int var17 = -var5; var17 <= var5; ++var17) {
                                if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + var17 + var12] == var13 - 1) {
                                    if (this.field_150128_a[(var14 + var12 - 1) * var11 + (var15 + var12) * var10 + var17 + var12] == -2) {
                                        this.field_150128_a[(var14 + var12 - 1) * var11 + (var15 + var12) * var10 + var17 + var12] = var13;
                                    }
                                    if (this.field_150128_a[(var14 + var12 + 1) * var11 + (var15 + var12) * var10 + var17 + var12] == -2) {
                                        this.field_150128_a[(var14 + var12 + 1) * var11 + (var15 + var12) * var10 + var17 + var12] = var13;
                                    }
                                    if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 - 1) * var10 + var17 + var12] == -2) {
                                        this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 - 1) * var10 + var17 + var12] = var13;
                                    }
                                    if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 + 1) * var10 + var17 + var12] == -2) {
                                        this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 + 1) * var10 + var17 + var12] = var13;
                                    }
                                    if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + (var17 + var12 - 1)] == -2) {
                                        this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + (var17 + var12 - 1)] = var13;
                                    }
                                    if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + var17 + var12 + 1] == -2) {
                                        this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + var17 + var12 + 1] = var13;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int var13 = this.field_150128_a[var12 * var11 + var12 * var10 + var12];
            if (var13 >= 0) {
                worldIn.setBlockState(pos, state.withProperty(BlockLeaves.field_176236_b, false), 4);
            }
            else {
                this.func_176235_d(worldIn, pos);
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (worldIn.func_175727_C(pos.offsetUp()) && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && rand.nextInt(15) == 1) {
            final double var5 = pos.getX() + rand.nextFloat();
            final double var6 = pos.getY() - 0.05;
            final double var7 = pos.getZ() + rand.nextFloat();
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var5, var6, var7, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    private void func_176235_d(final World worldIn, final BlockPos p_176235_2_) {
        this.dropBlockAsItem(worldIn, p_176235_2_, worldIn.getBlockState(p_176235_2_), 0);
        worldIn.setBlockToAir(p_176235_2_);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return (random.nextInt(20) == 0) ? 1 : 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.sapling);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            int var6 = this.func_176232_d(state);
            if (fortune > 0) {
                var6 -= 2 << fortune;
                if (var6 < 10) {
                    var6 = 10;
                }
            }
            if (worldIn.rand.nextInt(var6) == 0) {
                final Item var7 = this.getItemDropped(state, worldIn.rand, fortune);
                Block.spawnAsEntity(worldIn, pos, new ItemStack(var7, 1, this.damageDropped(state)));
            }
            var6 = 200;
            if (fortune > 0) {
                var6 -= 10 << fortune;
                if (var6 < 40) {
                    var6 = 40;
                }
            }
            this.func_176234_a(worldIn, pos, state, var6);
        }
    }
    
    protected void func_176234_a(final World worldIn, final BlockPos p_176234_2_, final IBlockState p_176234_3_, final int p_176234_4_) {
    }
    
    protected int func_176232_d(final IBlockState p_176232_1_) {
        return 20;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return !this.field_150121_P;
    }
    
    public void setGraphicsLevel(final boolean p_150122_1_) {
        this.field_176238_O = p_150122_1_;
        this.field_150121_P = p_150122_1_;
        this.field_150127_b = (p_150122_1_ ? 0 : 1);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.field_176238_O ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
    }
    
    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }
    
    public abstract BlockPlanks.EnumType func_176233_b(final int p0);
    
    static {
        field_176237_a = PropertyBool.create("decayable");
        field_176236_b = PropertyBool.create("check_decay");
    }
}
