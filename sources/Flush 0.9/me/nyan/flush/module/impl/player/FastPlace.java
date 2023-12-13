package me.nyan.flush.module.impl.player;

import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.NumberSetting;

public class FastPlace extends Module {
    public final NumberSetting delay = new NumberSetting("Placement Delay", this, 1, 0, 3);

    public FastPlace() {
        super("FastPlace", Category.PLAYER);
    }
}

