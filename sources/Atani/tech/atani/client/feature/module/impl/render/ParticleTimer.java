package tech.atani.client.feature.module.impl.render;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;

@ModuleData(name = "ParticleTimer", description = "Increases particle speed", category = Category.RENDER)
public class ParticleTimer extends Module {
    private final SliderValue<Float> timerSpeed = new SliderValue<>("Time Speed", "How fast should the particle speed be?", this, 0.2f, 0.1f, 5f, 1);

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        Methods.mc.particleTimer.timerSpeed = timerSpeed.getValue();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {
        Methods.mc.particleTimer.timerSpeed = 1F;
    }

}