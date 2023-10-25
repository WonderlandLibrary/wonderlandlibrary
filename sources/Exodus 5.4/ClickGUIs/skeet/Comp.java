/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.skeet;

import ClickGUIs.skeet.Skeet;
import de.Hero.settings.Setting;
import me.Tengoku.Terror.module.Module;

public class Comp {
    public double y2;
    public Skeet parent;
    public Setting setting;
    public Module module;
    public double x;
    public double x2;
    public double y;

    public void keyTyped(char c, int n) {
    }

    public void mouseReleased(int n, int n2, int n3) {
    }

    public void mouseClicked(int n, int n2, int n3) {
    }

    public void drawScreen(int n, int n2) {
    }

    public boolean isInside(int n, int n2, double d, double d2, double d3, double d4) {
        return (double)n > d && (double)n < d3 && (double)n2 > d2 && (double)n2 < d4;
    }
}

