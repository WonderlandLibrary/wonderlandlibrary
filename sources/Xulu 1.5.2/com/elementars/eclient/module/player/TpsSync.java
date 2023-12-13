// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class TpsSync extends Module
{
    private static TpsSync INSTANCE;
    
    public TpsSync() {
        super("TpsSync", "Syncs client with the tps", 0, Category.PLAYER, true);
        TpsSync.INSTANCE = this;
    }
    
    public static boolean isSync() {
        return TpsSync.INSTANCE.isToggled();
    }
}
