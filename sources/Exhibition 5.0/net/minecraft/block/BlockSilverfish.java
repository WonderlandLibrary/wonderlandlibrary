// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSilverfish extends Block
{
    public static final PropertyEnum VARIANT_PROP;
    private static final String __OBFID = "CL_00000271";
    
    public BlockSilverfish() {
        super(Material.clay);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSilverfish.VARIANT_PROP, EnumType.STONE));
        this.setHardness(0.0f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    public static boolean func_176377_d(final IBlockState p_176377_0_) {
        final Block var1 = p_176377_0_.getBlock();
        return p_176377_0_ == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.STONE) || var1 == Blocks.cobblestone || var1 == Blocks.stonebrick;
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        switch (SwitchEnumType.field_180178_a[((EnumType)state.getValue(BlockSilverfish.VARIANT_PROP)).ordinal()]) {
            case 1: {
                return new ItemStack(Blocks.cobblestone);
            }
            case 2: {
                return new ItemStack(Blocks.stonebrick);
            }
            case 3: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetaFromState());
            }
            case 4: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetaFromState());
            }
            case 5: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetaFromState());
            }
            default: {
                return new ItemStack(Blocks.stone);
            }
        }
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            final EntitySilverfish var6 = new EntitySilverfish(worldIn);
            var6.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntityInWorld(var6);
            var6.spawnExplosionParticle();
        }
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3);
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176881_a()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSilverfish.VARIANT_PROP, EnumType.func_176879_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockSilverfish.VARIANT_PROP)).func_176881_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSilverfish.VARIANT_PROP });
    }
    
    static {
        VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone", (SwitchEnumType)null) {
            private static final String __OBFID = "CL_00002097";
            
            @Override
            public IBlockState func_176883_d() {
                return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.STONE);
            }
        }, 
        COBBLESTONE("COBBLESTONE", 1, 1, "cobblestone", "cobble", (SwitchEnumType)null) {
            private static final String __OBFID = "CL_00002096";
            
            @Override
            public IBlockState func_176883_d() {
                return Blocks.cobblestone.getDefaultState();
            }
        }, 
        STONEBRICK("STONEBRICK", 2, 2, "stone_brick", "brick", (SwitchEnumType)null) {
            private static final String __OBFID = "CL_00002095";
            
            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.DEFAULT);
            }
        }, 
        MOSSY_STONEBRICK("MOSSY_STONEBRICK", 3, 3, "mossy_brick", "mossybrick", (SwitchEnumType)null) {
            private static final String __OBFID = "CL_00002094";
            
            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.MOSSY);
            }
        }, 
        CRACKED_STONEBRICK("CRACKED_STONEBRICK", 4, 4, "cracked_brick", "crackedbrick", (SwitchEnumType)null) {
            private static final String __OBFID = "CL_00002093";
            
            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.CRACKED);
            }
        }, 
        CHISELED_STONEBRICK("CHISELED_STONEBRICK", 5, 5, "chiseled_brick", "chiseledbrick", (SwitchEnumType)null) {
            private static final String __OBFID = "CL_00002092";
            
            @Override
            public IBlockState func_176883_d() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.CHISELED);
            }
        };
        
        private static final EnumType[] field_176885_g;
        private final int field_176893_h;
        private final String field_176894_i;
        private final String field_176891_j;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002098";
        
        private EnumType(final String p_i45704_1_, final int p_i45704_2_, final int p_i45704_3_, final String p_i45704_4_) {
            this(p_i45704_1_, p_i45704_2_, p_i45704_3_, p_i45704_4_, p_i45704_4_);
        }
        
        private EnumType(final String p_i45705_1_, final int p_i45705_2_, final int p_i45705_3_, final String p_i45705_4_, final String p_i45705_5_) {
            this.field_176893_h = p_i45705_3_;
            this.field_176894_i = p_i45705_4_;
            this.field_176891_j = p_i45705_5_;
        }
        
        public int func_176881_a() {
            return this.field_176893_h;
        }
        
        @Override
        public String toString() {
            return this.field_176894_i;
        }
        
        public static EnumType func_176879_a(int p_176879_0_) {
            if (p_176879_0_ < 0 || p_176879_0_ >= EnumType.field_176885_g.length) {
                p_176879_0_ = 0;
            }
            return EnumType.field_176885_g[p_176879_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176894_i;
        }
        
        public String func_176882_c() {
            return this.field_176891_j;
        }
        
        public abstract IBlockState func_176883_d();
        
        public static EnumType func_176878_a(final IBlockState p_176878_0_) {
            for (final EnumType var4 : values()) {
                if (p_176878_0_ == var4.func_176883_d()) {
                    return var4;
                }
            }
            return EnumType.STONE;
        }
        
        private EnumType(final String p_i45706_1_, final int p_i45706_2_, final int p_i45706_3_, final String p_i45706_4_, final SwitchEnumType p_i45706_5_) {
            this(p_i45706_1_, p_i45706_2_, p_i45706_3_, p_i45706_4_);
        }
        
        private EnumType(final String p_i45707_1_, final int p_i45707_2_, final int p_i45707_3_, final String p_i45707_4_, final String p_i45707_5_, final SwitchEnumType p_i45707_6_) {
            this(p_i45707_1_, p_i45707_2_, p_i45707_3_, p_i45707_4_, p_i45707_5_);
        }
        
        static {
            field_176885_g = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.STONE, EnumType.COBBLESTONE, EnumType.STONEBRICK, EnumType.MOSSY_STONEBRICK, EnumType.CRACKED_STONEBRICK, EnumType.CHISELED_STONEBRICK };
            for (final EnumType var4 : values()) {
                EnumType.field_176885_g[var4.func_176881_a()] = var4;
            }
        }
    }
    
    static final class SwitchEnumType
    {
        static final int[] field_180178_a;
        private static final String __OBFID = "CL_00002099";
        
        static {
            field_180178_a = new int[EnumType.values().length];
            try {
                SwitchEnumType.field_180178_a[EnumType.COBBLESTONE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumType.field_180178_a[EnumType.STONEBRICK.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumType.field_180178_a[EnumType.MOSSY_STONEBRICK.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumType.field_180178_a[EnumType.CRACKED_STONEBRICK.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumType.field_180178_a[EnumType.CHISELED_STONEBRICK.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
        }
    }
}
