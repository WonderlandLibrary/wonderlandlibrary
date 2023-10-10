/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockSilverfish
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    public BlockSilverfish() {
        super(Material.clay);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.STONE));
        this.setHardness(0.0f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    public static boolean canContainSilverfish(IBlockState blockState) {
        Block block = blockState.getBlock();
        if (blockState == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)) return true;
        if (block == Blocks.cobblestone) return true;
        if (block == Blocks.stonebrick) return true;
        return false;
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        switch (1.$SwitchMap$net$minecraft$block$BlockSilverfish$EnumType[state.getValue(VARIANT).ordinal()]) {
            case 1: {
                return new ItemStack(Blocks.cobblestone);
            }
            case 2: {
                return new ItemStack(Blocks.stonebrick);
            }
            case 3: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
            }
            case 4: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
            }
            case 5: {
                return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
            }
        }
        return new ItemStack(Blocks.stone);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (worldIn.isRemote) return;
        if (!worldIn.getGameRules().getBoolean("doTileDrops")) return;
        EntitySilverfish entitysilverfish = new EntitySilverfish(worldIn);
        entitysilverfish.setLocationAndAngles((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, 0.0f, 0.0f);
        worldIn.spawnEntityInWorld(entitysilverfish);
        entitysilverfish.spawnExplosionParticle();
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        EnumType[] enumTypeArray = EnumType.values();
        int n = enumTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumType blocksilverfish$enumtype = enumTypeArray[n2];
            list.add(new ItemStack(itemIn, 1, blocksilverfish$enumtype.getMetadata()));
            ++n2;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT);
    }

    public static enum EnumType implements IStringSerializable
    {
        STONE(0, "stone"){

            @Override
            public IBlockState getModelBlock() {
                return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
            }
        }
        ,
        COBBLESTONE(1, "cobblestone", "cobble"){

            @Override
            public IBlockState getModelBlock() {
                return Blocks.cobblestone.getDefaultState();
            }
        }
        ,
        STONEBRICK(2, "stone_brick", "brick"){

            @Override
            public IBlockState getModelBlock() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
            }
        }
        ,
        MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick"){

            @Override
            public IBlockState getModelBlock() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
            }
        }
        ,
        CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick"){

            @Override
            public IBlockState getModelBlock() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
            }
        }
        ,
        CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick"){

            @Override
            public IBlockState getModelBlock() {
                return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
            }
        };

        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int meta, String name) {
            this(meta, name, name);
        }

        private EnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static EnumType byMetadata(int meta) {
            if (meta >= 0) {
                if (meta < META_LOOKUP.length) return META_LOOKUP[meta];
            }
            meta = 0;
            return META_LOOKUP[meta];
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public abstract IBlockState getModelBlock();

        public static EnumType forModelBlock(IBlockState model) {
            EnumType[] enumTypeArray = EnumType.values();
            int n = enumTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumType blocksilverfish$enumtype = enumTypeArray[n2];
                if (model == blocksilverfish$enumtype.getModelBlock()) {
                    return blocksilverfish$enumtype;
                }
                ++n2;
            }
            return STONE;
        }

        static {
            META_LOOKUP = new EnumType[EnumType.values().length];
            EnumType[] enumTypeArray = EnumType.values();
            int n = enumTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumType blocksilverfish$enumtype;
                EnumType.META_LOOKUP[blocksilverfish$enumtype.getMetadata()] = blocksilverfish$enumtype = enumTypeArray[n2];
                ++n2;
            }
        }
    }
}

