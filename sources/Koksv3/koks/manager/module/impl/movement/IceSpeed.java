package koks.manager.module.impl.movement;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.init.Blocks;

/**
 * @author kroko
 * @created on 22.11.2020 : 00:31
 */

@ModuleInfo(name = "IceSpeed", category = Module.Category.MOVEMENT, description = "You are fast on ice.")
public class IceSpeed extends Module {

    float defaultSpeed;

    public Setting slipperiness = new Setting("Slipperiness", 0.1F, 0.1F, 1, false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            Blocks.ice.slipperiness = slipperiness.getCurrentValue();
            Blocks.packed_ice.slipperiness = slipperiness.getCurrentValue();
        }
    }

    @Override
    public void onEnable() {
        Blocks.ice.slipperiness = slipperiness.getCurrentValue();
        Blocks.packed_ice.slipperiness = slipperiness.getCurrentValue();
    }

    @Override
    public void onDisable() {
        Blocks.ice.slipperiness = 0.98F;
        Blocks.packed_ice.slipperiness = 0.98F;
    }
}
