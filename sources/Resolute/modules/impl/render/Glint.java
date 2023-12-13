// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.awt.Color;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class Glint extends Module
{
    public static ColorSetting color;
    public static boolean enabled;
    
    public Glint() {
        super("Glint", 0, "Changes enchantment color", Category.RENDER);
        this.addSettings(Glint.color);
    }
    
    @Override
    public void onEnable() {
        Glint.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Glint.enabled = false;
    }
    
    public static int getColor() {
        return new Color(Glint.color.getValue().getRed(), Glint.color.getValue().getGreen(), Glint.color.getValue().getBlue()).getRGB();
    }
    
    static {
        Glint.color = new ColorSetting("Color", new Color(255, 0, 0));
        Glint.enabled = false;
    }
}
