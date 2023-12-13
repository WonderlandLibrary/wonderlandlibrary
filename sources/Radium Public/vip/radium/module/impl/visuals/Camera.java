package vip.radium.module.impl.visuals;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.event.impl.render.HurtShakeEvent;
import vip.radium.event.impl.render.ViewClipEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;

@ModuleInfo(label = "Camera", category = ModuleCategory.VISUALS)
public final class Camera extends Module {

    private final Property<Boolean> noHurtShakeProperty = new Property<>("Hurt Shake", true);
    private final Property<Boolean> viewClipProperty = new Property<>("View Clip", true);

    @EventLink
    public final Listener<ViewClipEvent> onViewClipEvent = event -> {
        if (viewClipProperty.getValue())
            event.setCancelled();
    };

    @EventLink
    public final Listener<HurtShakeEvent> onHurtShakeEvent = event -> {
        if (!noHurtShakeProperty.getValue())
            event.setCancelled();
    };


}
