/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.ui.UI;

public class SLButton {
    public float x;
    public float y;
    public String name;
    public MainPanel panel;
    public boolean load;

    public SLButton(MainPanel panel, String name, float x, float y, boolean load) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.load = load;
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.slButtonDraw(this, x, y, this.panel);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.slButtonMouseClicked(this, x, y, button, this.panel);
        }
    }
}

