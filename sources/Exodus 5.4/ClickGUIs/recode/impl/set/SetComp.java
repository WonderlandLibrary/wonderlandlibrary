/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode.impl.set;

import ClickGUIs.recode.impl.Button;
import de.Hero.settings.Setting;

public class SetComp {
    public Setting setting;
    protected Button parent;
    private double height;

    public double drawScreen(int n, int n2, double d, double d2) {
        return 0.0;
    }

    public void mouseClicked(int n, int n2, int n3) {
    }

    public SetComp(Setting setting, Button button, double d) {
        this.parent = button;
        this.setting = setting;
        this.height = d;
    }

    public void keyTyped(char c, int n) {
    }

    public Setting getSetting() {
        return this.setting;
    }

    public void mouseReleased(int n, int n2, int n3) {
    }

    public double getHeight() {
        return this.height;
    }
}

