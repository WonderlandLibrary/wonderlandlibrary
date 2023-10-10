package client.event.impl.input;

import client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

@Getter
@AllArgsConstructor
public final class KeyboardInputEvent implements Event {
    private final GuiScreen guiScreen;
    private final int keyCode;
}
