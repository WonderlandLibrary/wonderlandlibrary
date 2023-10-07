package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

@Getter @Setter @AllArgsConstructor
public class DisplayGuiScreenEvent extends Event {
    GuiScreen screen;
}
