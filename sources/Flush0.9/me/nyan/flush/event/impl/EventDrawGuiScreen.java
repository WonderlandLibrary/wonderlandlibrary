package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventDrawGuiScreen extends Event {
    private final GuiScreen guiScreen;
    private final int width;
    private final int height;
    private final int mouseX;
    private final int mouseY;

    public EventDrawGuiScreen(GuiScreen guiScreen, int width, int height, int mouseX, int mouseY) {
        this.guiScreen = guiScreen;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public GuiScreen getGuiScreen() {
        return guiScreen;
    }
}
