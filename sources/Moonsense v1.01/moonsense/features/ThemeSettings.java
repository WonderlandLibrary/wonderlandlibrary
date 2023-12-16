// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;

public class ThemeSettings extends SCModule
{
    public static final ThemeSettings INSTANCE;
    public final Setting mainColor;
    public final Setting uiBackgroundMain;
    public final Setting uiBackgroundSecondary;
    public final Setting uiAccent;
    public final Setting brandingColor;
    public final Setting generalTextColor;
    public final Setting buttonFont;
    
    static {
        INSTANCE = new ThemeSettings();
    }
    
    public ThemeSettings() {
        super("Theme Settings", "Client Theme Settings", -1, false);
        new Setting(this, "Style");
        this.mainColor = new Setting(this, "UI Color").setDefault(new Color(23, 102, 181).getRGB(), 0);
        this.uiBackgroundMain = new Setting(this, "UI Background Main").setDefault(-1358954496, 0);
        this.uiBackgroundSecondary = new Setting(this, "UI Background Secondary").setDefault(1677721600, 0);
        this.uiAccent = new Setting(this, "UI Accent").setDefault(new Color(125, 136, 142, 150).getRGB(), 0);
        this.brandingColor = new Setting(this, "Branding Color").setDefault(Color.WHITE.getRGB(), 0);
        this.generalTextColor = new Setting(this, "General Text Color").setDefault(Color.WHITE.getRGB(), 0);
        this.buttonFont = new Setting(this, "Custom Button Font").setDefault(true);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
