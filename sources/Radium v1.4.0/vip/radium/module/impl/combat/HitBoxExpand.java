package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import vip.radium.event.impl.entity.RayTraceEntityEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.DoubleProperty;

@ModuleInfo(label = "Hit Box Expand", category = ModuleCategory.COMBAT)
public final class HitBoxExpand extends Module {

    private final DoubleProperty expandMultiplier = new DoubleProperty("Expand Multiplier", 2, 1, 10, 0.1);

    @EventLink
    public final Listener<RayTraceEntityEvent> onRayTraceEntity = event -> event.setBorderMultiplier(expandMultiplier.getValue().floatValue());

}
