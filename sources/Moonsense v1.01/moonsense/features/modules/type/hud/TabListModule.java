// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class TabListModule extends SCModule
{
    public final Setting backgroundColor;
    public final Setting usePingColorForEntry;
    public final Setting entryColor;
    public final Setting disableHeader;
    public final Setting displayPlayerHead;
    public final Setting disableFooter;
    public final Setting showTabIcon;
    public final Setting highlightOwnName;
    public final Setting nameHighlightColor;
    public final Setting nameShadow;
    public final Setting hidePing;
    public final Setting displayPingAsNumber;
    public final Setting dynamicPingColor;
    public final Setting pingShadow;
    public final Setting defaultPingNumberColor;
    public static TabListModule INSTANCE;
    
    public TabListModule() {
        super("Tab List", "Customize the Minecraft tab list.");
        TabListModule.INSTANCE = this;
        new Setting(this, "Color Options");
        this.backgroundColor = new Setting(this, "Background Color").setDefault(new Color(0, 0, 0, 128).getRGB(), 0);
        this.usePingColorForEntry = new Setting(this, "Use Ping Color for Entry").setDefault(false);
        this.entryColor = new Setting(this, "Entry Color").setDefault(new Color(0, 0, 0, 32).getRGB(), 0);
        new Setting(this, "General Options");
        this.disableHeader = new Setting(this, "Disable Header").setDefault(false);
        this.disableFooter = new Setting(this, "Disable Footer").setDefault(false);
        this.displayPlayerHead = new Setting(this, "Show Player Head").setDefault(true);
        new Setting(this, "Name Options");
        this.showTabIcon = new Setting(this, "Show Client Icon").setDefault(true);
        this.highlightOwnName = new Setting(this, "Highlight Own Name").setDefault(false);
        this.nameHighlightColor = new Setting(this, "Name Highlight").setDefault(Color.yellow.getRGB(), 0);
        this.nameShadow = new Setting(this, "Name Shadow").setDefault(true);
        new Setting(this, "Latency Options");
        this.hidePing = new Setting(this, "Hide Ping").setDefault(false);
        this.displayPingAsNumber = new Setting(this, "Display Ping as a Number").setDefault(false);
        this.dynamicPingColor = new Setting(this, "Dynamic Ping Number Color").setDefault(false);
        this.pingShadow = new Setting(this, "Ping Shadow").setDefault(false);
        this.defaultPingNumberColor = new Setting(this, "Default Ping Number Color").setDefault(new Color(255, 255, 85, 255).getRGB(), 0);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
}
