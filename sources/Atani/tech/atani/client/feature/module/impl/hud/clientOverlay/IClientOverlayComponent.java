package tech.atani.client.feature.module.impl.hud.clientOverlay;

import tech.atani.client.listener.event.minecraft.render.Render2DEvent;
import tech.atani.client.utility.math.atomic.AtomicFloat;

public interface IClientOverlayComponent {

    void draw(Render2DEvent render2DEvent, AtomicFloat leftY, AtomicFloat rightY);

    int getPriority();
}
