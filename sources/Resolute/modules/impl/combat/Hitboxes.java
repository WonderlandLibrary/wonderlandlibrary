// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Hitboxes extends Module
{
    public static NumberSetting size;
    public static boolean enabled;
    
    public Hitboxes() {
        super("Hitboxes", 0, "Expands entity hitboxes", Category.COMBAT);
        this.addSettings(Hitboxes.size);
    }
    
    @Override
    public void onEnable() {
        Hitboxes.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Hitboxes.enabled = false;
    }
    
    static {
        Hitboxes.size = new NumberSetting("Size", 0.3, 0.1, 1.0, 0.1);
        Hitboxes.enabled = false;
    }
}
