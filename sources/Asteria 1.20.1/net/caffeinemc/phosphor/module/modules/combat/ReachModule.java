package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;

public class ReachModule extends Module {
    public final NumberSetting entityReach = new NumberSetting("Entity Reach", this, 3, 3, 6, 0.1);
    public final BooleanSetting blockReachToggle = new BooleanSetting("Enable Block Reach", this, false);
    public final NumberSetting blockReach = new NumberSetting("Block Reach", this, 4.5, 4.5, 6, 0.1);

    public ReachModule() {
        super("Reach", "Extends player's reach", Category.COMBAT);
    }
}
