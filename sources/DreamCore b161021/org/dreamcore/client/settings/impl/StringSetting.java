package org.dreamcore.client.settings.impl;

import org.dreamcore.client.settings.Setting;

import java.util.function.Supplier;

public class StringSetting extends Setting {

    public String defaultText;
    public String currentText;

    public StringSetting(String name, String defaultText, String currentText, Supplier<Boolean> visible) {
        this.name = name;
        this.defaultText = defaultText;
        this.currentText = currentText;
        setVisible(visible);
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }
}
