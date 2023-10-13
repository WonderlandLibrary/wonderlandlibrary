/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt
extends Block {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", DirtType.class);
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");
    private static final String __OBFID = "CL_00000228";

    protected BlockDirt() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)((Object)DirtType.DIRT)).withProperty(SNOWY, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(VARIANT) == DirtType.PODZOL) {
            Block var4 = worldIn.getBlockState(pos.offsetUp()).getBlock();
            state = state.withProperty(SNOWY, Boolean.valueOf(var4 == Blocks.snow || var4 == Blocks.snow_layer));
        }
        return state;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock() != this ? 0 : ((DirtType)((Object)var3.getValue(VARIANT))).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)((Object)DirtType.byMetadata(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((DirtType)((Object)state.getValue(VARIANT))).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT, SNOWY);
    }

    @Override
    public int damageDropped(IBlockState state) {
        DirtType var2 = (DirtType)((Object)state.getValue(VARIANT));
        if (var2 == DirtType.PODZOL) {
            var2 = DirtType.DIRT;
        }
        return var2.getMetadata();
    }

    public static enum DirtType implements IStringSerializable
    {
        DIRT("DIRT", 0, 0, "dirt", "default"),
        COARSE_DIRT("COARSE_DIRT", 1, 1, "coarse_dirt", "coarse"),
        PODZOL("PODZOL", 2, 2, "podzol");

        private static final DirtType[] METADATA_LOOKUP;
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private static final DirtType[] $VALUES;
        private static final String __OBFID = "CL_00002125";

        static {
            METADATA_LOOKUP = new DirtType[DirtType.values().length];
            $VALUES = new DirtType[]{DIRT, COARSE_DIRT, PODZOL};
            DirtType[] var0 = DirtType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                DirtType var3;
                DirtType.METADATA_LOOKUP[var3.getMetadata()] = var3 = var0[var2];
            }
        }

        private DirtType(String p_i45727_1_, int p_i45727_2_, int metadata, String name) {
            this(p_i45727_1_, p_i45727_2_, metadata, name, name);
        }

        private DirtType(String p_i45728_1_, int p_i45728_2_, int metadata, String name, String unlocalizedName) {
            this.metadata = metadata;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public String toString() {
            return this.name;
        }

        public static DirtType byMetadata(int metadata) {
            if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
                metadata = 0;
            }
            return METADATA_LOOKUP[metadata];
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

