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
import net.minecraft.world.World;

public class BlockNewLeaf
extends BlockLeaves {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>(){

        public boolean apply(BlockPlanks.EnumType enumType) {
            return enumType.getMetadata() >= 4;
        }
    });

    @Override
    public BlockPlanks.EnumType getWoodType(int n) {
        return BlockPlanks.EnumType.byMetadata((n & 3) + 4);
    }

    public BlockNewLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        return new ItemStack(Item.getItemFromBlock(this), 1, iBlockState.getValue(VARIANT).getMetadata() - 4);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    protected void dropApple(World world, BlockPos blockPos, IBlockState iBlockState, int n) {
        if (iBlockState.getValue(VARIANT) == BlockPlanks.EnumType.DARK_OAK && world.rand.nextInt(n) == 0) {
            BlockNewLeaf.spawnAsEntity(world, blockPos, new ItemStack(Items.apple, 1, 0));
        }
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).getMetadata();
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(VARIANT).getMetadata() - 4;
        if (!iBlockState.getValue(DECAYABLE).booleanValue()) {
            n |= 4;
        }
        if (iBlockState.getValue(CHECK_DECAY).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        return iBlockState.getBlock().getMetaFromState(iBlockState) & 3;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(VARIANT, this.getWoodType(n)).withProperty(DECAYABLE, (n & 4) == 0).withProperty(CHECK_DECAY, (n & 8) > 0);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity) {
        if (!world.isRemote && entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().getItem() == Items.shears) {
            entityPlayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            BlockNewLeaf.spawnAsEntity(world, blockPos, new ItemStack(Item.getItemFromBlock(this), 1, iBlockState.getValue(VARIANT).getMetadata() - 4));
        } else {
            super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
        }
    }
}

