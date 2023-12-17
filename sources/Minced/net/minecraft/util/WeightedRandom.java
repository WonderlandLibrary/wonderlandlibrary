// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Random;
import java.util.List;

public class WeightedRandom
{
    public static int getTotalWeight(final List<? extends Item> collection) {
        int i = 0;
        for (int j = 0, k = collection.size(); j < k; ++j) {
            final Item weightedrandom$item = (Item)collection.get(j);
            i += weightedrandom$item.itemWeight;
        }
        return i;
    }
    
    public static <T extends Item> T getRandomItem(final Random random, final List<T> collection, final int totalWeight) {
        if (totalWeight <= 0) {
            throw new IllegalArgumentException();
        }
        final int i = random.nextInt(totalWeight);
        return getRandomItem(collection, i);
    }
    
    public static <T extends Item> T getRandomItem(final List<T> collection, int weight) {
        for (int i = 0, j = collection.size(); i < j; ++i) {
            final T t = collection.get(i);
            weight -= t.itemWeight;
            if (weight < 0) {
                return t;
            }
        }
        return null;
    }
    
    public static <T extends Item> T getRandomItem(final Random random, final List<T> collection) {
        return getRandomItem(random, collection, getTotalWeight(collection));
    }
    
    public static class Item
    {
        protected int itemWeight;
        
        public Item(final int itemWeightIn) {
            this.itemWeight = itemWeightIn;
        }
    }
}
