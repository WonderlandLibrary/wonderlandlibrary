/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemRecord
extends Item {
    private static final Map<String, ItemRecord> RECORDS = Maps.newHashMap();
    public final String recordName;

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() == Blocks.jukebox && !iBlockState.getValue(BlockJukebox.HAS_RECORD).booleanValue()) {
            if (world.isRemote) {
                return true;
            }
            ((BlockJukebox)Blocks.jukebox).insertRecord(world, blockPos, iBlockState, itemStack);
            world.playAuxSFXAtEntity(null, 1005, blockPos, Item.getIdFromItem(this));
            --itemStack.stackSize;
            entityPlayer.triggerAchievement(StatList.field_181740_X);
            return true;
        }
        return false;
    }

    public String getRecordNameLocal() {
        return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
    }

    protected ItemRecord(String string) {
        this.recordName = string;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        RECORDS.put("records." + string, this);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        list.add(this.getRecordNameLocal());
    }

    public static ItemRecord getRecord(String string) {
        return RECORDS.get(string);
    }
}

