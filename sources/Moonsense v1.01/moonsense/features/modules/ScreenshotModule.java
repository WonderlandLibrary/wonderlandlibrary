// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules;

import moonsense.enums.ModuleCategory;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class ScreenshotModule extends SCModule
{
    public static ScreenshotModule INSTANCE;
    public final Setting customScreenshotMessage;
    public final Setting uploadToImgur;
    
    public ScreenshotModule() {
        super("Screenshot Module", "Advanced Minecraft screenshot system.");
        ScreenshotModule.INSTANCE = this;
        new Setting(this, "Screenshot Settings");
        this.customScreenshotMessage = new Setting(this, "Custom Screenshot Success Message").setDefault(true);
        this.uploadToImgur = new Setting(this, "Upload on Imgur").setDefault(true);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
