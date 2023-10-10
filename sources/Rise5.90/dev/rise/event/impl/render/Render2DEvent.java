package dev.rise.event.impl.render;

import dev.rise.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;

@Getter
@Setter
@AllArgsConstructor
public final class Render2DEvent extends Event {
    private float partialTicks;
    private ScaledResolution scaledResolution;
}
