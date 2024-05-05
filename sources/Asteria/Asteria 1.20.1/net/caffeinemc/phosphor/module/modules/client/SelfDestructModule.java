package net.caffeinemc.phosphor.module.modules.client;

import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.module.Module;

public class SelfDestructModule extends Module {
    public SelfDestructModule() {
        super("SelfDestruct", "Removes traces", Category.CLIENT);
    }

    @Override
    public void enable() {
        AsteriaMenu.stopClient();
    }
}
