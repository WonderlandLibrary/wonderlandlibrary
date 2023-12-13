/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom {
    private static final String __OBFID = "CL_00001503";

    public static int getTotalWeight(Collection p_76272_0_) {
        int var1 = 0;
        for (Item var3 : p_76272_0_) {
            var1 += var3.itemWeight;
        }
        return var1;
    }

    public static Item getRandomItem(Random p_76273_0_, Collection p_76273_1_, int p_76273_2_) {
        if (p_76273_2_ <= 0) {
            throw new IllegalArgumentException();
        }
        int var3 = p_76273_0_.nextInt(p_76273_2_);
        return WeightedRandom.func_180166_a(p_76273_1_, var3);
    }

    public static Item func_180166_a(Collection p_180166_0_, int p_180166_1_) {
        Item var3;
        Iterator var2 = p_180166_0_.iterator();
        do {
            if (!var2.hasNext()) {
                return null;
            }
            var3 = (Item)var2.next();
        } while ((p_180166_1_ -= var3.itemWeight) >= 0);
        return var3;
    }

    public static Item getRandomItem(Random p_76271_0_, Collection p_76271_1_) {
        return WeightedRandom.getRandomItem(p_76271_0_, p_76271_1_, WeightedRandom.getTotalWeight(p_76271_1_));
    }

    public static class Item {
        protected int itemWeight;
        private static final String __OBFID = "CL_00001504";

        public Item(int p_i1556_1_) {
            this.itemWeight = p_i1556_1_;
        }
    }
}

