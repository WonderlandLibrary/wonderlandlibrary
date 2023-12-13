/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Collection;
import java.util.Random;

public class WeightedRandom {
    public static int getTotalWeight(Collection<? extends Item> collection) {
        int i2 = 0;
        for (Item item : collection) {
            i2 += item.itemWeight;
        }
        return i2;
    }

    public static <T extends Item> T getRandomItem(Random random, Collection<T> collection, int totalWeight) {
        if (totalWeight <= 0) {
            throw new IllegalArgumentException();
        }
        int i2 = random.nextInt(totalWeight);
        return WeightedRandom.getRandomItem(collection, i2);
    }

    public static <T extends Item> T getRandomItem(Collection<T> collection, int weight) {
        for (Item t2 : collection) {
            if ((weight -= t2.itemWeight) >= 0) continue;
            return (T)t2;
        }
        return (T)((Item)null);
    }

    public static <T extends Item> T getRandomItem(Random random, Collection<T> collection) {
        return WeightedRandom.getRandomItem(random, collection, WeightedRandom.getTotalWeight(collection));
    }

    public static class Item {
        protected int itemWeight;

        public Item(int itemWeightIn) {
            this.itemWeight = itemWeightIn;
        }
    }
}

