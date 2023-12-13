// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.awt.Color;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class WorldColor extends Module
{
    public static ColorSetting lightMapColorProperty;
    public static boolean enabled;
    
    public WorldColor() {
        super("WorldColor", 0, "", Category.RENDER);
        this.addSettings(WorldColor.lightMapColorProperty);
    }
    
    @Override
    public void onEnable() {
        WorldColor.enabled = true;
    }
    
    @Override
    public void onDisable() {
        WorldColor.enabled = false;
    }
    
    static {
        WorldColor.lightMapColorProperty = new ColorSetting("Color", new Color(-32640));
        WorldColor.enabled = false;
    }
}
