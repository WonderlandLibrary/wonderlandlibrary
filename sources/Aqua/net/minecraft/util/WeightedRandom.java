package net.minecraft.util;

import java.util.Collection;
import java.util.Random;
import net.minecraft.util.WeightedRandom;

public class WeightedRandom {
    public static int getTotalWeight(Collection<? extends Item> collection) {
        int i = 0;
        for (Item weightedrandom$item : collection) {
            i += weightedrandom$item.itemWeight;
        }
        return i;
    }

    public static <T extends Item> T getRandomItem(Random random, Collection<T> collection, int totalWeight) {
        if (totalWeight <= 0) {
            throw new IllegalArgumentException();
        }
        int i = random.nextInt(totalWeight);
        return WeightedRandom.getRandomItem(collection, i);
    }

    public static <T extends Item> T getRandomItem(Collection<T> collection, int weight) {
        for (Item t : collection) {
            if ((weight -= t.itemWeight) >= 0) continue;
            return (T)t;
        }
        return (T)((Item)null);
    }

    public static <T extends Item> T getRandomItem(Random random, Collection<T> collection) {
        return WeightedRandom.getRandomItem(random, collection, WeightedRandom.getTotalWeight(collection));
    }
}
