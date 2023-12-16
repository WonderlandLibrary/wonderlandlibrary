// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;

public class SettingsManager extends SCModule
{
    public static final SettingsManager INSTANCE;
    public final Setting showName;
    public final Setting fixNametagRot;
    public final Setting borderlessWindow;
    public final Setting previewBG;
    public final Setting hudEditorKeybind;
    public final Setting hideFoliage;
    public final Setting hideEndportals;
    public final Setting showEnchantmentGlint;
    public final Setting chunkUpdates;
    public final Setting transparentNametags;
    public final Setting renderEntityShadow;
    public final Setting showGroundArrows;
    public final Setting showStuckArrows;
    public final Setting hideSkulls;
    public final Setting noTCPDelay;
    public final Setting hitDelayFix;
    public final Setting redString;
    public final Setting clearGlass;
    public final Setting fireHeight;
    public final Setting cosmeticColor;
    
    static {
        INSTANCE = new SettingsManager();
    }
    
    public SettingsManager() {
        super("General Settings", "General Client Settings", -1, false);
        new Setting(this, "General Options");
        this.showName = new Setting(this, "Show name in F5").setDefault(false);
        this.fixNametagRot = new Setting(this, "Fix nametag rotation").setDefault(true);
        this.borderlessWindow = new Setting(this, "Borderless Window").setDefault(false);
        new Setting(this, "Module Rendering");
        this.previewBG = new Setting(this, "Preview Background").setDefault(0).setRange("Overworld Scene", "Overworld 2 Scene", "Nether Scene", "End Scene");
        new Setting(this, "Client Keybinds");
        this.hudEditorKeybind = new Setting(this, "HUD Editor Keybind").setDefault(new KeyBinding(54));
        new Setting(this, "General Performance Options");
        this.hideFoliage = new Setting(this, "Hide Foliage").setDefault(false).onValueChanged(setting -> this.mc.renderGlobal.loadRenderers());
        this.hideEndportals = new Setting(this, "Hide End Portals").setDefault(false).onValueChanged(setting -> this.mc.renderGlobal.loadRenderers());
        this.showEnchantmentGlint = new Setting(this, "Render Enchantment Glint").setDefault(0).setRange("All", "Only In Inventory", "Never");
        this.chunkUpdates = new Setting(this, "Lazy Chunk Loading").setDefault(5).setRange("Off (Vanilla)", "Lowest", "Low", "Medium", "High", "Highest");
        this.transparentNametags = new Setting(this, "Transparent Nametags").setDefault(false);
        new Setting(this, "Entity Performance Options");
        this.renderEntityShadow = new Setting(this, "Render Shadow").setDefault(true);
        this.showGroundArrows = new Setting(this, "Render Ground Arrows").setDefault(true);
        this.showStuckArrows = new Setting(this, "Show Arrows Stuck In Entities").setDefault(true);
        this.hideSkulls = new Setting(this, "Hide Skulls").setDefault(false).onValueChanged(setting -> this.mc.renderGlobal.loadRenderers());
        this.noTCPDelay = new Setting(this, "No TCP Delay").setDefault(true);
        this.hitDelayFix = new Setting(this, "Hit Delay Fix").setDefault(true);
        new Setting(this, "Resource Pack Performance Options");
        this.clearGlass = new Setting(this, "Clear Glass").setDefault(false).onValueChanged(setting -> this.mc.renderGlobal.loadRenderers());
        this.redString = new Setting(this, "Red String").setDefault(false);
        this.fireHeight = new Setting(this, "Fire Height").setDefault(0.0f).setRange(0.0f, 2.0f, 0.01f);
        new Setting(this, "Cosmetic Settings");
        this.cosmeticColor = new Setting(this, "Cosmetic Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
