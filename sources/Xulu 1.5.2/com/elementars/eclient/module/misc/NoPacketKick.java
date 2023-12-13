// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class NoPacketKick extends Module
{
    private static NoPacketKick INSTANCE;
    
    public NoPacketKick() {
        super("NoPacketKick", "Prevents packet kicks", 0, Category.MISC, true);
        NoPacketKick.INSTANCE = this;
    }
    
    public static boolean isEnabled() {
        return NoPacketKick.INSTANCE.isToggled();
    }
}
