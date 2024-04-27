package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class ScreenEvent extends Event {
    private GuiScreen guiScreen;

    public ScreenEvent(GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }

    public GuiScreen getGuiScreen() {
        return this.guiScreen;
    }
}
