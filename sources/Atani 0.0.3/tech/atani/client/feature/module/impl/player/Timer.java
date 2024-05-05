package tech.atani.client.feature.module.impl.player;

import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "Timer", description = "Increases game speed", category = Category.MISCELLANEOUS)
public class Timer extends Module {
    private final SliderValue<Float> timerSpeed = new SliderValue<Float>("Time Speed", "How fast should the game speed be?", this, 1f, 0.1f, 5f, 1);

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        mc.timer.timerSpeed = timerSpeed.getValue();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
    }

}