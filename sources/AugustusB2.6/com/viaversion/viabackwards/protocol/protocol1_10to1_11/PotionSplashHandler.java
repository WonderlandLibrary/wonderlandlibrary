// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_10to1_11;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public class PotionSplashHandler
{
    private static final Int2IntMap DATA;
    
    public static int getOldData(final int data) {
        return PotionSplashHandler.DATA.get(data);
    }
    
    static {
        (DATA = new Int2IntOpenHashMap(14, 0.99f)).defaultReturnValue(-1);
        PotionSplashHandler.DATA.put(2039713, 5);
        PotionSplashHandler.DATA.put(8356754, 7);
        PotionSplashHandler.DATA.put(2293580, 9);
        PotionSplashHandler.DATA.put(14981690, 12);
        PotionSplashHandler.DATA.put(8171462, 14);
        PotionSplashHandler.DATA.put(5926017, 17);
        PotionSplashHandler.DATA.put(3035801, 19);
        PotionSplashHandler.DATA.put(16262179, 21);
        PotionSplashHandler.DATA.put(4393481, 23);
        PotionSplashHandler.DATA.put(5149489, 25);
        PotionSplashHandler.DATA.put(13458603, 28);
        PotionSplashHandler.DATA.put(9643043, 31);
        PotionSplashHandler.DATA.put(4738376, 34);
        PotionSplashHandler.DATA.put(3381504, 36);
    }
}
