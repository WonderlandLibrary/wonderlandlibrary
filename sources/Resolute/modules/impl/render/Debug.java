// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.modules.Module;

public class Debug extends Module
{
    public static boolean enabled;
    
    public Debug() {
        super("Debug", 0, "", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        Debug.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Debug.enabled = false;
    }
    
    static {
        Debug.enabled = false;
    }
}
