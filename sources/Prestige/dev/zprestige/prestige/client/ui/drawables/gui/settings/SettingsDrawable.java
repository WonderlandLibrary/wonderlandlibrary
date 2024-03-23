package dev.zprestige.prestige.client.ui.drawables.gui.settings;

import dev.zprestige.prestige.client.setting.Setting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.Drawable;

public class SettingsDrawable extends Drawable {

    public Setting setting;

    public SettingsDrawable(Setting<?> setting, float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4, setting.getDescription());
        this.setting = setting;
    }

    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        if (isInside(n, n2)) {
            Interface.setHover(this);
        }
    }

    public String getName() {
        return setting.getName();
    }

    public boolean isVisible() {
        return setting.visible();
    }

    boolean isInside(int n, int n2) {
        return n >= getX() && n <= getX() + getWidth() && n2 >= getY() && n2 <= getY() + getHeight();
    }
}
