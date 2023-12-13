package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import vip.radium.event.impl.entity.RayTraceEntityEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.DoubleProperty;

@ModuleInfo(label = "Reach", category = ModuleCategory.COMBAT)
public final class Reach extends Module {

    private final DoubleProperty reachProperty = new DoubleProperty("Reach", 3.5, 3.0, 6.0, 0.05);

    @EventLink
    public final Listener<RayTraceEntityEvent> onRayTraceEntity = event -> event.setReach(reachProperty.getValue().floatValue());
}
