/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class ItemEmptyMap
extends ItemMapBase {
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ItemStack itemStack2 = new ItemStack(Items.filled_map, 1, world.getUniqueDataId("map"));
        String string = "map_" + itemStack2.getMetadata();
        MapData mapData = new MapData(string);
        world.setItemData(string, mapData);
        mapData.scale = 0;
        mapData.calculateMapCenter(entityPlayer.posX, entityPlayer.posZ, mapData.scale);
        mapData.dimension = (byte)world.provider.getDimensionId();
        mapData.markDirty();
        --itemStack.stackSize;
        if (itemStack.stackSize <= 0) {
            return itemStack2;
        }
        if (!entityPlayer.inventory.addItemStackToInventory(itemStack2.copy())) {
            entityPlayer.dropPlayerItemWithRandomChoice(itemStack2, false);
        }
        entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStack;
    }

    protected ItemEmptyMap() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
}

