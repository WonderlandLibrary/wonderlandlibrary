// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.modules.Module;

public class RearView extends Module
{
    public static boolean enabled;
    
    public RearView() {
        super("RearView", 0, "", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        RearView.enabled = true;
    }
    
    @Override
    public void onDisable() {
        RearView.enabled = false;
    }
    
    static {
        RearView.enabled = false;
    }
}
