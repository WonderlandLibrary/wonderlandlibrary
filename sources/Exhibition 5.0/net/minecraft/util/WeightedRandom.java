// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Random;
import java.util.Iterator;
import java.util.Collection;

public class WeightedRandom
{
    private static final String __OBFID = "CL_00001503";
    
    public static int getTotalWeight(final Collection p_76272_0_) {
        int var1 = 0;
        for (final Item var3 : p_76272_0_) {
            var1 += var3.itemWeight;
        }
        return var1;
    }
    
    public static Item getRandomItem(final Random p_76273_0_, final Collection p_76273_1_, final int p_76273_2_) {
        if (p_76273_2_ <= 0) {
            throw new IllegalArgumentException();
        }
        final int var3 = p_76273_0_.nextInt(p_76273_2_);
        return func_180166_a(p_76273_1_, var3);
    }
    
    public static Item func_180166_a(final Collection p_180166_0_, int p_180166_1_) {
        for (final Item var3 : p_180166_0_) {
            p_180166_1_ -= var3.itemWeight;
            if (p_180166_1_ < 0) {
                return var3;
            }
        }
        return null;
    }
    
    public static Item getRandomItem(final Random p_76271_0_, final Collection p_76271_1_) {
        return getRandomItem(p_76271_0_, p_76271_1_, getTotalWeight(p_76271_1_));
    }
    
    public static class Item
    {
        protected int itemWeight;
        private static final String __OBFID = "CL_00001504";
        
        public Item(final int p_i1556_1_) {
            this.itemWeight = p_i1556_1_;
        }
    }
}
