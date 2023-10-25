/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Collection;
import java.util.Random;

public class WeightedRandom {
    public static int getTotalWeight(Collection<? extends Item> collection) {
        int n = 0;
        for (Item item : collection) {
            n += item.itemWeight;
        }
        return n;
    }

    public static <T extends Item> T getRandomItem(Collection<T> collection, int n) {
        for (Item item : collection) {
            if ((n -= item.itemWeight) >= 0) continue;
            return (T)item;
        }
        return null;
    }

    public static <T extends Item> T getRandomItem(Random random, Collection<T> collection, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        int n2 = random.nextInt(n);
        return WeightedRandom.getRandomItem(collection, n2);
    }

    public static <T extends Item> T getRandomItem(Random random, Collection<T> collection) {
        return WeightedRandom.getRandomItem(random, collection, WeightedRandom.getTotalWeight(collection));
    }

    public static class Item {
        protected int itemWeight;

        public Item(int n) {
            this.itemWeight = n;
        }
    }
}

