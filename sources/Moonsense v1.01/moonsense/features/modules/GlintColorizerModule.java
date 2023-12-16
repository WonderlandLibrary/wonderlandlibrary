// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class GlintColorizerModule extends SCModule
{
    public static GlintColorizerModule INSTANCE;
    public final Setting itemGlint;
    public final Setting itemGlintColor;
    public final Setting armorGlint;
    public final Setting armorGlintColor;
    
    public GlintColorizerModule() {
        super("Glint Colorizer", "Alters the color of the Minecraft enchantment item glint.");
        GlintColorizerModule.INSTANCE = this;
        new Setting(this, "Glint Options");
        this.itemGlint = new Setting(this, "Item Glint").setDefault(true);
        this.itemGlintColor = new Setting(this, "Item Glint Color").setDefault(new Color(-8372020).getRGB(), 0);
        this.armorGlint = new Setting(this, "Armor Glint").setDefault(true);
        this.armorGlintColor = new Setting(this, "Armor Glint Color").setDefault(new Color(-8372020).getRGB(), 0);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
