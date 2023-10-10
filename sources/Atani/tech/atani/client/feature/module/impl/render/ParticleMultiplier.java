package tech.atani.client.feature.module.impl.render;

import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.render.EmitParticleEvent;

@ModuleData(name = "ParticleMultiplier", description = "Multiplies particles", category = Category.RENDER)
public class ParticleMultiplier extends Module {

    private final SliderValue<Integer> multiplier = new SliderValue<>("Multiplier", "What will be the multiplier for particles?", this, 1, 1, 10, 0);

    @Listen
    public void onParticle(EmitParticleEvent e) {
        e.multiplier = multiplier.getValue();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}