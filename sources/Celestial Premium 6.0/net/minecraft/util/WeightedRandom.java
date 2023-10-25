/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import java.util.List;
import java.util.Random;

public class WeightedRandom {
    public static int getTotalWeight(List<? extends Item> collection) {
        int i = 0;
        int k = collection.size();
        for (int j = 0; j < k; ++j) {
            Item weightedrandom$item = collection.get(j);
            i += weightedrandom$item.itemWeight;
        }
        return i;
    }

    public static <T extends Item> T getRandomItem(Random random, List<T> collection, int totalWeight) {
        if (totalWeight <= 0) {
            throw new IllegalArgumentException();
        }
        int i = random.nextInt(totalWeight);
        return WeightedRandom.getRandomItem(collection, i);
    }

    public static <T extends Item> T getRandomItem(List<T> collection, int weight) {
        int j = collection.size();
        for (int i = 0; i < j; ++i) {
            Item t = (Item)collection.get(i);
            if ((weight -= t.itemWeight) >= 0) continue;
            return (T)t;
        }
        return (T)((Item)null);
    }

    public static <T extends Item> T getRandomItem(Random random, List<T> collection) {
        return WeightedRandom.getRandomItem(random, collection, WeightedRandom.getTotalWeight(collection));
    }

    public static class Item {
        protected int itemWeight;

        public Item(int itemWeightIn) {
            this.itemWeight = itemWeightIn;
        }
    }
}

