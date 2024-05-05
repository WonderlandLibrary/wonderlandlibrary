// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.awt.Color;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class Ghost extends Module
{
    public static ColorSetting color;
    public static boolean enabled;
    
    public Ghost() {
        super("Ghost", 0, "Renders a ghost of the player", Category.RENDER);
        this.addSettings(Ghost.color);
    }
    
    @Override
    public void onEnable() {
        Ghost.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Ghost.enabled = false;
    }
    
    static {
        Ghost.color = new ColorSetting("Color", new Color(192, 0, 255));
        Ghost.enabled = false;
    }
}
