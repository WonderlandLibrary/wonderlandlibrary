package net.caffeinemc.phosphor.module.modules.render;

import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;

public class NoShieldDelayModule extends Module {
    public final NumberSetting shieldDelay = new NumberSetting("Shield Delay", this, 0, 0, 5, 1);

    public NoShieldDelayModule() {
        super("NoShieldDelay", "Removes shield's visual delay", Category.RENDER);
    }
}
