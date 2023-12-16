// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world;

import moonsense.enums.ModuleCategory;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class TimeChangerModule extends SCModule
{
    public static TimeChangerModule INSTANCE;
    public final Setting time;
    
    public TimeChangerModule() {
        super("Time Changer", "Alter Minecraft's time client-side.");
        new Setting(this, "Time Options");
        this.time = new Setting(this, "Time").setDefault(0).setRange("Vanilla", "Day", "Sunset", "Night");
        TimeChangerModule.INSTANCE = this;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
