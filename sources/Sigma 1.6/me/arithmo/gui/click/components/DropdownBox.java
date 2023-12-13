/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.ArrayList;
import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.CategoryButton;
import me.arithmo.gui.click.components.CategoryPanel;
import me.arithmo.gui.click.components.DropdownButton;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.module.data.Options;

public class DropdownBox {
    public Options option;
    public float x;
    public float y;
    public ArrayList<DropdownButton> buttons = new ArrayList();
    public CategoryPanel panel;
    public boolean active;

    public DropdownBox(Options option, float x, float y, CategoryPanel panel) {
        this.option = option;
        this.panel = panel;
        this.x = x;
        this.y = y;
        panel.categoryButton.panel.theme.dropDownContructor(this, x, y, this.panel);
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            if (!this.panel.visible) continue;
            theme.dropDownDraw(this, x, y, this.panel);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownMouseClicked(this, x, y, button, this.panel);
        }
    }
}

