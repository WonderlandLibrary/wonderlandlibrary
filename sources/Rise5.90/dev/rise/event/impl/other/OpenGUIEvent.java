package dev.rise.event.impl.other;

import dev.rise.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

@Getter
@Setter
@AllArgsConstructor
public class OpenGUIEvent extends Event {
    private final GuiScreen newScreen, oldScreen;
}