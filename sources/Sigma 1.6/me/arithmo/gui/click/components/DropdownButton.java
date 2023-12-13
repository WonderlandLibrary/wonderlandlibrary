/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.DropdownBox;
import me.arithmo.gui.click.ui.UI;

public class DropdownButton {
    public String name;
    public float x;
    public float y;
    public DropdownBox box;

    public DropdownButton(String name, float x, float y, DropdownBox box) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.box = box;
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownButtonDraw(this, this.box, x, y);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownButtonMouseClicked(this, this.box, x, y, button);
        }
    }
}

