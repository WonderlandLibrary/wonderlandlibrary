package dev.tenacity.module;

import dev.tenacity.ui.clickgui.dropdown.component.CategoryPanelComponent;

public enum ModuleCategory {

    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc"),
    WORLD("World"),
    EXPLOIT("Exploit"),
    FUNNY("Funny");

    private final String name;
    private int posX, posY;

    ModuleCategory(final String name) {
        this.name = name;

        posX = (int) (20 + (CategoryPanelComponent.offsetX += 110) - 110);
        posY = 20;
    }

    public final String getName() {return name;}

    public final int getPosX() {return posX;}

    public final int getPosY() {
        return posY;
    }

    public final void setPosX(final int posX) {
        this.posX = posX;
    }

    public final void setPosY(final int posY) {
        this.posY = posY;
    }
}
