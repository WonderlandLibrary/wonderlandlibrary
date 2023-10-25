/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOldLeaf
extends BlockLeaves {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>(){

        public boolean apply(BlockPlanks.EnumType enumType) {
            return enumType.getMetadata() < 4;
        }
    });

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(VARIANT, this.getWoodType(n)).withProperty(DECAYABLE, (n & 4) == 0).withProperty(CHECK_DECAY, (n & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(VARIANT).getMetadata();
        if (!iBlockState.getValue(DECAYABLE).booleanValue()) {
            n |= 4;
        }
        if (iBlockState.getValue(CHECK_DECAY).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        return new ItemStack(Item.getItemFromBlock(this), 1, iBlockState.getValue(VARIANT).getMetadata());
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        list.add(new ItemStack(item, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        list.add(new ItemStack(item, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        list.add(new ItemStack(item, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).getMetadata();
    }

    @Override
    protected int getSaplingDropChance(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT) == BlockPlanks.EnumType.JUNGLE ? 40 : super.getSaplingDropChance(iBlockState);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity) {
        if (!world.isRemote && entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().getItem() == Items.shears) {
            entityPlayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            BlockOldLeaf.spawnAsEntity(world, blockPos, new ItemStack(Item.getItemFromBlock(this), 1, iBlockState.getValue(VARIANT).getMetadata()));
        } else {
            super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
        }
    }

    public BlockOldLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK).withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int n) {
        return BlockPlanks.EnumType.byMetadata((n & 3) % 4);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT, CHECK_DECAY, DECAYABLE);
    }

    @Override
    protected void dropApple(World world, BlockPos blockPos, IBlockState iBlockState, int n) {
        if (iBlockState.getValue(VARIANT) == BlockPlanks.EnumType.OAK && world.rand.nextInt(n) == 0) {
            BlockOldLeaf.spawnAsEntity(world, blockPos, new ItemStack(Items.apple, 1, 0));
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        if (iBlockState.getBlock() == this) {
            BlockPlanks.EnumType enumType = iBlockState.getValue(VARIANT);
            if (enumType == BlockPlanks.EnumType.SPRUCE) {
                return ColorizerFoliage.getFoliageColorPine();
            }
            if (enumType == BlockPlanks.EnumType.BIRCH) {
                return ColorizerFoliage.getFoliageColorBirch();
            }
        }
        return super.colorMultiplier(iBlockAccess, blockPos, n);
    }

    @Override
    public int getRenderColor(IBlockState iBlockState) {
        if (iBlockState.getBlock() != this) {
            return super.getRenderColor(iBlockState);
        }
        BlockPlanks.EnumType enumType = iBlockState.getValue(VARIANT);
        return enumType == BlockPlanks.EnumType.SPRUCE ? ColorizerFoliage.getFoliageColorPine() : (enumType == BlockPlanks.EnumType.BIRCH ? ColorizerFoliage.getFoliageColorBirch() : super.getRenderColor(iBlockState));
    }
}

