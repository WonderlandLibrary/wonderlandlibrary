/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass
extends BlockBush
implements IGrowable {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE);
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        return iBlockState.getBlock().getMetaFromState(iBlockState);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        int n = 1;
        while (n < 3) {
            list.add(new ItemStack(item, 1, n));
            ++n;
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        return iBlockAccess.getBiomeGenForCoords(blockPos).getGrassColorAtPos(blockPos);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(TYPE, EnumType.byMetadata(n));
    }

    @Override
    public int quantityDroppedWithBonus(int n, Random random) {
        return 1 + random.nextInt(n * 2 + 1);
    }

    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    protected BlockTallGrass() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.DEAD_BUSH));
        float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.8f, 0.5f + f);
    }

    @Override
    public int getRenderColor(IBlockState iBlockState) {
        if (iBlockState.getBlock() != this) {
            return super.getRenderColor(iBlockState);
        }
        EnumType enumType = iBlockState.getValue(TYPE);
        return enumType == EnumType.DEAD_BUSH ? 0xFFFFFF : ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return random.nextInt(8) == 0 ? Items.wheat_seeds : null;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos blockPos, IBlockState iBlockState) {
        return this.canPlaceBlockOn(world.getBlockState(blockPos.down()).getBlock());
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity) {
        if (!world.isRemote && entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().getItem() == Items.shears) {
            entityPlayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            BlockTallGrass.spawnAsEntity(world, blockPos, new ItemStack(Blocks.tallgrass, 1, iBlockState.getValue(TYPE).getMeta()));
        } else {
            super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
        }
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        BlockDoublePlant.EnumPlantType enumPlantType = BlockDoublePlant.EnumPlantType.GRASS;
        if (iBlockState.getValue(TYPE) == EnumType.FERN) {
            enumPlantType = BlockDoublePlant.EnumPlantType.FERN;
        }
        if (Blocks.double_plant.canPlaceBlockAt(world, blockPos)) {
            Blocks.double_plant.placeAt(world, blockPos, enumPlantType, 2);
        }
    }

    @Override
    public boolean isReplaceable(World world, BlockPos blockPos) {
        return true;
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return iBlockState.getValue(TYPE) != EnumType.DEAD_BUSH;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(TYPE).getMeta();
    }

    public static enum EnumType implements IStringSerializable
    {
        DEAD_BUSH(0, "dead_bush"),
        GRASS(1, "tall_grass"),
        FERN(2, "fern");

        private static final EnumType[] META_LOOKUP = new EnumType[EnumType.values().length];
        private final int meta;
        private final String name;

        public String toString() {
            return this.name;
        }

        private EnumType(int n2, String string2) {
            this.meta = n2;
            this.name = string2;
        }

        static {
            EnumType[] enumTypeArray = EnumType.values();
            int n = enumTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumType enumType;
                EnumType.META_LOOKUP[enumType.getMeta()] = enumType = enumTypeArray[n2];
                ++n2;
            }
        }

        public static EnumType byMetadata(int n) {
            if (n < 0 || n >= META_LOOKUP.length) {
                n = 0;
            }
            return META_LOOKUP[n];
        }

        public int getMeta() {
            return this.meta;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

