package client.event.impl.other;

import client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;

@Getter
@Setter
@AllArgsConstructor
public final class Render2DEvent implements Event {
    private final ScaledResolution scaledResolution;
    private final float partialTicks;
}
