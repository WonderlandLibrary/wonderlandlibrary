package dev.tenacity.ui.clickgui.dropdown.component.setting;

import dev.tenacity.setting.Setting;
import dev.tenacity.ui.IScreen;

public abstract class SettingPanelComponent<T extends Setting<?>> implements IScreen {

    private final T setting;

    private float posX, posY;

    private final float width = 100,
            height = 15;

    private float addedHeight = 1;

    public SettingPanelComponent(final T setting) {
        this.setting = setting;
    }

    public final T getSetting() {
        return setting;
    }

    public final float getPosX() {
        return posX;
    }

    public final float getPosY() {
        return posY;
    }

    public final float getHeight() {
        return height;
    }

    public final float getWidth() {
        return width;
    }

    public final void setPosX(final float posX) {
        this.posX = posX;
    }

    public final void setPosY(final float posY) {
        this.posY = posY;
    }

    public final float getAddedHeight() {
        return addedHeight;
    }

    public final void setAddedHeight(final float addedHeight) {
        this.addedHeight = addedHeight;
    }
}
