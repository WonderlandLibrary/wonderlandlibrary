// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class MenuBlurModule extends SCModule
{
    public static MenuBlurModule INSTANCE;
    public final Setting pauseMenuBlur;
    public final Setting pauseMenuBlurRadius;
    public final Setting pauseMenuBackgroundColor;
    public final Setting containerBlur;
    public final Setting containerBlurRadius;
    public final Setting containerBackgroundColor;
    public final Setting uiBlur;
    public final Setting uiBlurRadius;
    public final Setting uiBackgroundColor;
    
    public MenuBlurModule() {
        super("Menu Blur", "Blurs menus");
        MenuBlurModule.INSTANCE = this;
        this.pauseMenuBlur = new Setting(this, "Pause Menu Blur").setDefault(true);
        this.pauseMenuBlurRadius = new Setting(this, "Pause Menu Blur Radius").setDefault(5).setRange(0, 10, 1);
        this.pauseMenuBackgroundColor = new Setting(this, "Pause Menu Background Color").setDefault(new Color(0, 0, 0, 150).getRGB(), 0);
        this.containerBlur = new Setting(this, "Container (Inventory) Blur").setDefault(true);
        this.containerBlurRadius = new Setting(this, "Container (Inventory) Blur Radius").setDefault(5).setRange(0, 10, 1);
        this.containerBackgroundColor = new Setting(this, "Container (Inventory) Background Color").setDefault(new Color(0, 0, 0, 150).getRGB(), 0);
        this.uiBlur = new Setting(this, "Streamlined UI Blur").setDefault(true);
        this.uiBlurRadius = new Setting(this, "Streamlined UI Blur Radius").setDefault(5).setRange(0, 10, 1);
        this.uiBackgroundColor = new Setting(this, "Streamlined UI Background Color").setDefault(new Color(0, 0, 0, 150).getRGB(), 0);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
