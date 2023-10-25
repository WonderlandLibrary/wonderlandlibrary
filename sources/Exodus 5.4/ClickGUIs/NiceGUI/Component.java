/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.NiceGUI;

import ClickGUIs.NiceGUI.SettingsWindow;
import de.Hero.settings.Setting;
import me.Tengoku.Terror.util.font.FontUtil;

public class Component {
    int y;
    SettingsWindow parent;
    Setting set;
    int x;

    public int getX() {
        return this.x;
    }

    public void draw(FontUtil fontUtil, int n, int n2) {
    }

    public void setX(int n) {
        this.x = n;
    }

    public void setParent(SettingsWindow settingsWindow) {
        this.parent = settingsWindow;
    }

    public Component(int n, int n2, Setting setting, SettingsWindow settingsWindow) {
        this.x = n;
        this.y = n2;
        this.set = setting;
        this.parent = settingsWindow;
    }

    public void mouseClicked(int n, int n2) {
    }

    public Setting getSet() {
        return this.set;
    }

    public void setY(int n) {
        this.y = n;
    }

    public SettingsWindow getParent() {
        return this.parent;
    }

    public int getY() {
        return this.y;
    }

    public void mouseRelease() {
    }

    public void setSet(Setting setting) {
        this.set = setting;
    }
}

