package tech.atani.client.feature.module.impl.combat;

import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

@ModuleData(name = "NoClickDelay", description = "Removes clicking delay", category = Category.MISCELLANEOUS)
public class NoClickDelay extends Module {

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        mc.leftClickCounter = 0;
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}