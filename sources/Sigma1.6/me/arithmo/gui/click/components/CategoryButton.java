/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.CategoryPanel;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.ui.UI;

public class CategoryButton {
    public float x;
    public float y;
    public String name;
    public MainPanel panel;
    public boolean enabled;
    public CategoryPanel categoryPanel;

    public CategoryButton(MainPanel panel, String name, float x, float y) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        panel.theme.categoryButtonConstructor(this, this.panel);
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonDraw(this, x, y);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonMouseClicked(this, this.panel, x, y, button);
        }
    }

    public void mouseReleased(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonMouseReleased(this, x, y, button);
        }
    }
}

