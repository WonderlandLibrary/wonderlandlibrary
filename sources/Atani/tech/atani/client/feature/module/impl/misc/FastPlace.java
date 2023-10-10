package tech.atani.client.feature.module.impl.misc;

import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

@ModuleData(name = "FastPlace", description = "Removes placing delay", category = Category.MISCELLANEOUS)
public class FastPlace extends Module {

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        mc.rightClickDelayTimer = 0;
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 6;
    }

}