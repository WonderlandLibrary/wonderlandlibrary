// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class MemoryUsageModule extends SCDefaultRenderModule
{
    private final Setting textFormatMode;
    private final Runtime runtime;
    
    public MemoryUsageModule() {
        super("Memory Usage", "Displays your memory usage on the HUD.");
        new Setting(this, "Memory Usage Display Options");
        this.textFormatMode = new Setting(this, "Text Formatting").setDefault(0).setRange("%used used/total", "%used", "used/total", "%used used", "%used total", "used");
        this.runtime = Runtime.getRuntime();
    }
    
    @Override
    public Object getValue() {
        final long i = this.runtime.maxMemory();
        final long j = this.runtime.totalMemory();
        final long k = this.runtime.freeMemory();
        final long l = j - k;
        final String tfm = this.textFormatMode.getValue().get(this.textFormatMode.getInt() + 1);
        if (tfm.equalsIgnoreCase("%used used/total")) {
            return String.format("%2d%% %03d/%03dMB", l * 100L / i, l / 1024L / 1024L, i / 1024L / 1024L);
        }
        if (tfm.equalsIgnoreCase("%used")) {
            return String.format("%2d%%", l * 100L / i);
        }
        if (tfm.equalsIgnoreCase("used/total")) {
            return String.format("%03d/%03dMB", l / 1024L / 1024L, i / 1024L / 1024L);
        }
        if (tfm.equalsIgnoreCase("%used used")) {
            return String.format("%2d%% %03dMB", l * 100L / i, l / 1024L / 1024L);
        }
        if (tfm.equalsIgnoreCase("%used total")) {
            return String.format("%2d%% %03dMB", l * 100L / i, i / 1024L / 1024L);
        }
        if (tfm.equalsIgnoreCase("used")) {
            return String.format("%03dMB", l / 1024L / 1024L);
        }
        return "";
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_CENTER;
    }
}
