package vip.radium.module.impl.visuals;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.event.impl.entity.SpawnParticleEntityEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.DoubleProperty;

@ModuleInfo(label = "More Particles", category = ModuleCategory.VISUALS)
public final class MoreParticles extends Module {

    private final DoubleProperty multiplierProperty = new DoubleProperty("Multiplier", 2, 0, 10, 1);

    @EventLink
    public final Listener<SpawnParticleEntityEvent> onSpawnEntityEvent = event -> {
        event.setMultiplier(multiplierProperty.getValue().intValue());
    };

}
