// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Emote extends Module
{
    public static boolean enabled;
    public static ModeSetting mode;
    
    public Emote() {
        super("Emote", 0, "Haha funny emote", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        Emote.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Emote.enabled = false;
        super.onDisable();
    }
    
    static {
        Emote.enabled = false;
        Emote.mode = new ModeSetting("Mode", "Dab", new String[] { "Dab" });
    }
}
