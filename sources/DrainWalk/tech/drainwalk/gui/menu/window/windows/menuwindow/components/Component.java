package tech.drainwalk.gui.menu.window.windows.menuwindow.components;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.gui.menu.window.windows.menuwindow.MenuWindow;

public class Component {
    @Setter
    @Getter
    protected float x, y, width, height;
    protected MenuWindow parent;
    public static boolean canDragging = true;


    public Component(float x, float y, float width, float height, MenuWindow parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void updateScreen(int mouseX, int mouseY) {
    }
}
