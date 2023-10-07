// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum TYPE_PROP;
    public static final PropertyInteger STAGE_PROP;
    private static final String __OBFID = "CL_00000305";
    
    protected BlockSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSapling.TYPE_PROP, BlockPlanks.EnumType.OAK).withProperty(BlockSapling.STAGE_PROP, 0));
        final float var1 = 0.4f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, var1 * 2.0f, 0.5f + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);
            if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9 && rand.nextInt(7) == 0) {
                this.func_176478_d(worldIn, pos, state, rand);
            }
        }
    }
    
    public void func_176478_d(final World worldIn, final BlockPos p_176478_2_, final IBlockState p_176478_3_, final Random p_176478_4_) {
        if ((int)p_176478_3_.getValue(BlockSapling.STAGE_PROP) == 0) {
            worldIn.setBlockState(p_176478_2_, p_176478_3_.cycleProperty(BlockSapling.STAGE_PROP), 4);
        }
        else {
            this.func_176476_e(worldIn, p_176478_2_, p_176478_3_, p_176478_4_);
        }
    }
    
    public void func_176476_e(final World worldIn, final BlockPos p_176476_2_, final IBlockState p_176476_3_, final Random p_176476_4_) {
        Object var5 = (p_176476_4_.nextInt(10) == 0) ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int var6 = 0;
        int var7 = 0;
        boolean var8 = false;
        switch (SwitchEnumType.field_177065_a[((BlockPlanks.EnumType)p_176476_3_.getValue(BlockSapling.TYPE_PROP)).ordinal()]) {
            case 1: {
            Label_0235:
                for (var6 = 0; var6 >= -1; --var6) {
                    for (var7 = 0; var7 >= -1; --var7) {
                        if (this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7), BlockPlanks.EnumType.SPRUCE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7), BlockPlanks.EnumType.SPRUCE) && this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7 + 1), BlockPlanks.EnumType.SPRUCE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.SPRUCE)) {
                            var5 = new WorldGenMegaPineTree(false, p_176476_4_.nextBoolean());
                            var8 = true;
                            break Label_0235;
                        }
                    }
                }
                if (!var8) {
                    var7 = 0;
                    var6 = 0;
                    var5 = new WorldGenTaiga2(true);
                    break;
                }
                break;
            }
            case 2: {
                var5 = new WorldGenForest(true, false);
                break;
            }
            case 3: {
            Label_0423:
                for (var6 = 0; var6 >= -1; --var6) {
                    for (var7 = 0; var7 >= -1; --var7) {
                        if (this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7), BlockPlanks.EnumType.JUNGLE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7), BlockPlanks.EnumType.JUNGLE) && this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7 + 1), BlockPlanks.EnumType.JUNGLE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.JUNGLE)) {
                            var5 = new WorldGenMegaJungle(true, 10, 20, BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.JUNGLE.func_176839_a());
                            var8 = true;
                            break Label_0423;
                        }
                    }
                }
                if (!var8) {
                    var7 = 0;
                    var6 = 0;
                    var5 = new WorldGenTrees(true, 4 + p_176476_4_.nextInt(7), BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.JUNGLE.func_176839_a(), false);
                    break;
                }
                break;
            }
            case 4: {
                var5 = new WorldGenSavannaTree(true);
                break;
            }
            case 5: {
            Label_0616:
                for (var6 = 0; var6 >= -1; --var6) {
                    for (var7 = 0; var7 >= -1; --var7) {
                        if (this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7), BlockPlanks.EnumType.DARK_OAK) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7), BlockPlanks.EnumType.DARK_OAK) && this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7 + 1), BlockPlanks.EnumType.DARK_OAK) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.DARK_OAK)) {
                            var5 = new WorldGenCanopyTree(true);
                            var8 = true;
                            break Label_0616;
                        }
                    }
                }
                if (!var8) {
                    return;
                }
                break;
            }
        }
        final IBlockState var9 = Blocks.air.getDefaultState();
        if (var8) {
            worldIn.setBlockState(p_176476_2_.add(var6, 0, var7), var9, 4);
            worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7), var9, 4);
            worldIn.setBlockState(p_176476_2_.add(var6, 0, var7 + 1), var9, 4);
            worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7 + 1), var9, 4);
        }
        else {
            worldIn.setBlockState(p_176476_2_, var9, 4);
        }
        if (!((WorldGenerator)var5).generate(worldIn, p_176476_4_, p_176476_2_.add(var6, 0, var7))) {
            if (var8) {
                worldIn.setBlockState(p_176476_2_.add(var6, 0, var7), p_176476_3_, 4);
                worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7), p_176476_3_, 4);
                worldIn.setBlockState(p_176476_2_.add(var6, 0, var7 + 1), p_176476_3_, 4);
                worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7 + 1), p_176476_3_, 4);
            }
            else {
                worldIn.setBlockState(p_176476_2_, p_176476_3_, 4);
            }
        }
    }
    
    public boolean func_176477_a(final World worldIn, final BlockPos p_176477_2_, final BlockPlanks.EnumType p_176477_3_) {
        final IBlockState var4 = worldIn.getBlockState(p_176477_2_);
        return var4.getBlock() == this && var4.getValue(BlockSapling.TYPE_PROP) == p_176477_3_;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(BlockSapling.TYPE_PROP)).func_176839_a();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final BlockPlanks.EnumType var7 : BlockPlanks.EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
        }
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return worldIn.rand.nextFloat() < 0.45;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        this.func_176478_d(worldIn, p_176474_3_, p_176474_4_, p_176474_2_);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSapling.TYPE_PROP, BlockPlanks.EnumType.func_176837_a(meta & 0x7)).withProperty(BlockSapling.STAGE_PROP, (meta & 0x8) >> 3);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(BlockSapling.TYPE_PROP)).func_176839_a();
        var3 |= (int)state.getValue(BlockSapling.STAGE_PROP) << 3;
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSapling.TYPE_PROP, BlockSapling.STAGE_PROP });
    }
    
    static {
        TYPE_PROP = PropertyEnum.create("type", BlockPlanks.EnumType.class);
        STAGE_PROP = PropertyInteger.create("stage", 0, 1);
    }
    
    static final class SwitchEnumType
    {
        static final int[] field_177065_a;
        private static final String __OBFID = "CL_00002067";
        
        static {
            field_177065_a = new int[BlockPlanks.EnumType.values().length];
            try {
                SwitchEnumType.field_177065_a[BlockPlanks.EnumType.SPRUCE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumType.field_177065_a[BlockPlanks.EnumType.BIRCH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumType.field_177065_a[BlockPlanks.EnumType.JUNGLE.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumType.field_177065_a[BlockPlanks.EnumType.ACACIA.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumType.field_177065_a[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumType.field_177065_a[BlockPlanks.EnumType.OAK.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
