// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import vip.Resolute.modules.Module;

public class DelayRemover extends Module
{
    public static boolean enabled;
    
    public DelayRemover() {
        super("DelayRemover", 0, "Allows for 1.7 hit reg", Category.COMBAT);
    }
    
    @Override
    public void onEnable() {
        DelayRemover.enabled = true;
    }
    
    @Override
    public void onDisable() {
        DelayRemover.enabled = false;
    }
    
    static {
        DelayRemover.enabled = false;
    }
}
