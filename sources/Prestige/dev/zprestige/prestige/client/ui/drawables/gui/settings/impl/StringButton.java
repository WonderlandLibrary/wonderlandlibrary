package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.setting.impl.StringSetting;
import dev.zprestige.prestige.client.ui.drawables.gui.field.CustomTextField;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;

public class StringButton extends SettingsDrawable {
    public StringSetting setting;
    public CustomTextField field;

    public StringButton(StringSetting stringSetting, float f, float f2, float f3, float f4) {
        super(stringSetting, f, f2, f3, f4);
        setting = stringSetting;
        field = new CustomTextField(setting.getValue());
        field.setValue(setting.getObject());
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        field.setPosition(getX(), getY());
        field.setDimension(getWidth(), getHeight());
        field.render(n, n2, f2);
        setting.invokeValue(field.getValue());
        setHeight(15);
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        field.mouseClicked(n, (int)d, (int)d2);
    }

    @Override
    public void charTyped(char c, int n) {
        field.charTyped(c);
    }

    @Override
    public void keyPressed(int n, int n2, int n3) {
        field.keyPressed(n);
    }

    public StringSetting getSetting() {
        return setting;
    }
}
