// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.lighting;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.config.ModuleConfig;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class LightingModule extends SCModule
{
    public static LightingModule INSTANCE;
    private final Setting fullBright;
    private final Setting boostFactor;
    public final Setting lightingHUDGroup;
    public final Setting lightingHUDShowWhileTyping;
    public final Setting lightingHUDEnabled;
    public final Setting lightingHUDBackground;
    public final Setting lightingHUDBackgroundRadius;
    public final Setting lightingHUDBackgroundColor;
    public final Setting lightingHUDBackgroundWidth;
    public final Setting lightingHUDBackgroundHeight;
    public final Setting lightingHUDTextShadow;
    public final Setting lightingHUDBrackets;
    public final Setting lightingHUDTextColor;
    public final Setting lightingHUDColorNotifier;
    public final Setting lightingHUDNotifierColor;
    public final Setting lightingHUDBorderColor;
    public final Setting lightingHUDBorderWidth;
    public final Setting lightingHUDBorder;
    
    public LightingModule() {
        super("Lighting", "Modify Minecraft's lighting effects. (Can increase FPS).");
        LightingModule.INSTANCE = this;
        this.lightingHUDGroup = new Setting(this, "Lighting HUD").setDefault(new Setting.CompoundSettingGroup("Lighting HUD", new Setting[] { this.lightingHUDEnabled = new Setting(null, "Enabled").setDefault(false).compound(true).onValueChanged(setting -> ModuleConfig.INSTANCE.setEnabled(LightingHUDChild.INSTANCE, setting.getBoolean())), new Setting(null, "General Options").compound(true), this.lightingHUDShowWhileTyping = new Setting(null, "Show While Typing").setDefault(true).compound(true).onValueChanged(setting -> LightingHUDChild.INSTANCE.showWhileTyping.setValue(setting.getBoolean())), this.lightingHUDBackground = new Setting(null, "Background").setDefault(true).compound(true), this.lightingHUDBackgroundRadius = new Setting(null, "Background Radius").setDefault(0.0f).setRange(0.0f, 5.0f, 0.01f).compound(true), this.lightingHUDTextShadow = new Setting(null, "Text Shadow").setDefault(false).compound(true), this.lightingHUDBackgroundWidth = new Setting(null, "Background Width").setDefault(5).setRange(2, 20, 1).compound(true), this.lightingHUDBackgroundHeight = new Setting(null, "Background Height").setDefault(5).setRange(2, 12, 1).compound(true), this.lightingHUDBorder = new Setting(null, "Border").setDefault(false).compound(true), this.lightingHUDBorderWidth = new Setting(null, "Border Thickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f).compound(true), this.lightingHUDBrackets = new Setting(null, "Brackets").setDefault(5).setRange("[]", "<>", "{}", "--", "||", "NONE").compound(true), new Setting(null, "Color Options").compound(true), this.lightingHUDTextColor = new Setting(null, "Text Color").setDefault(new Color(255, 255, 255).getRGB(), 0).compound(true), this.lightingHUDColorNotifier = new Setting(null, "Low Light Notifier").setDefault(true).compound(true), this.lightingHUDNotifierColor = new Setting(null, "Low Light Notifier Color").setDefault(Color.red.getRGB(), 0).compound(true), this.lightingHUDBackgroundColor = new Setting(null, "Background Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.lightingHUDBorderColor = new Setting(null, "Border Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true) }));
        new Setting(this, "Performance Options");
        this.fullBright = new Setting(this, "Full Bright").setDefault(true);
        new Setting(this, "Brightness Boost (1.0 = Vanilla Bright, 10.0 = 10.0x Brightness Boost)");
        this.boostFactor = new Setting(this, "Boost Factor").setDefault(10.0f).setRange(1.0f, 10.0f, 0.1f).onValueChanged(setting -> {
            if (ModuleConfig.INSTANCE.isEnabled(this)) {
                if (this.fullBright.getBoolean()) {
                    this.mc.gameSettings.gammaSetting = setting.getFloat();
                }
                else {
                    this.mc.gameSettings.gammaSetting = 1.0f;
                }
            }
            return;
        });
        this.fullBright.onValueChanged(setting -> {
            if (ModuleConfig.INSTANCE.isEnabled(this)) {
                if (setting.getBoolean()) {
                    this.mc.gameSettings.gammaSetting = this.boostFactor.getFloat();
                }
                else {
                    this.mc.gameSettings.gammaSetting = 1.0f;
                }
            }
        });
    }
    
    @Override
    public void onEnable() {
        this.mc.gameSettings.gammaSetting = this.boostFactor.getFloat();
    }
    
    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = 1.0f;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
