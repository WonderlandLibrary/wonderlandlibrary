/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.WeightedRandom;

public class WeightedRandomChestContent
extends WeightedRandom.Item {
    private ItemStack theItemId;
    private int maxStackSize;
    private int minStackSize;

    public static void generateDispenserContents(Random random, List<WeightedRandomChestContent> list, TileEntityDispenser tileEntityDispenser, int n) {
        int n2 = 0;
        while (n2 < n) {
            WeightedRandomChestContent weightedRandomChestContent = WeightedRandom.getRandomItem(random, list);
            int n3 = weightedRandomChestContent.minStackSize + random.nextInt(weightedRandomChestContent.maxStackSize - weightedRandomChestContent.minStackSize + 1);
            if (weightedRandomChestContent.theItemId.getMaxStackSize() >= n3) {
                ItemStack itemStack = weightedRandomChestContent.theItemId.copy();
                itemStack.stackSize = n3;
                tileEntityDispenser.setInventorySlotContents(random.nextInt(tileEntityDispenser.getSizeInventory()), itemStack);
            } else {
                int n4 = 0;
                while (n4 < n3) {
                    ItemStack itemStack = weightedRandomChestContent.theItemId.copy();
                    itemStack.stackSize = 1;
                    tileEntityDispenser.setInventorySlotContents(random.nextInt(tileEntityDispenser.getSizeInventory()), itemStack);
                    ++n4;
                }
            }
            ++n2;
        }
    }

    public WeightedRandomChestContent(Item item, int n, int n2, int n3, int n4) {
        super(n4);
        this.theItemId = new ItemStack(item, 1, n);
        this.minStackSize = n2;
        this.maxStackSize = n3;
    }

    public static void generateChestContents(Random random, List<WeightedRandomChestContent> list, IInventory iInventory, int n) {
        int n2 = 0;
        while (n2 < n) {
            WeightedRandomChestContent weightedRandomChestContent = WeightedRandom.getRandomItem(random, list);
            int n3 = weightedRandomChestContent.minStackSize + random.nextInt(weightedRandomChestContent.maxStackSize - weightedRandomChestContent.minStackSize + 1);
            if (weightedRandomChestContent.theItemId.getMaxStackSize() >= n3) {
                ItemStack itemStack = weightedRandomChestContent.theItemId.copy();
                itemStack.stackSize = n3;
                iInventory.setInventorySlotContents(random.nextInt(iInventory.getSizeInventory()), itemStack);
            } else {
                int n4 = 0;
                while (n4 < n3) {
                    ItemStack itemStack = weightedRandomChestContent.theItemId.copy();
                    itemStack.stackSize = 1;
                    iInventory.setInventorySlotContents(random.nextInt(iInventory.getSizeInventory()), itemStack);
                    ++n4;
                }
            }
            ++n2;
        }
    }

    public WeightedRandomChestContent(ItemStack itemStack, int n, int n2, int n3) {
        super(n3);
        this.theItemId = itemStack;
        this.minStackSize = n;
        this.maxStackSize = n2;
    }

    public static List<WeightedRandomChestContent> func_177629_a(List<WeightedRandomChestContent> list, WeightedRandomChestContent ... weightedRandomChestContentArray) {
        ArrayList arrayList = Lists.newArrayList(list);
        Collections.addAll(arrayList, weightedRandomChestContentArray);
        return arrayList;
    }
}

