// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class TNTTimerModule extends SCModule
{
    public final Setting textShadow;
    public final Setting staticColor;
    public final Setting timerColor;
    public static TNTTimerModule INSTANCE;
    
    public TNTTimerModule() {
        super("TNT Timer", "Displays the fuse countdown above a piece of lit TNT.");
        TNTTimerModule.INSTANCE = this;
        this.textShadow = new Setting(this, "Text Shadow").setDefault(true);
        this.staticColor = new Setting(this, "Static Color").setDefault(false);
        this.timerColor = new Setting(this, "Timer Color").setDefault(new Color(0, 255, 0, 255).getRGB(), 0);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
