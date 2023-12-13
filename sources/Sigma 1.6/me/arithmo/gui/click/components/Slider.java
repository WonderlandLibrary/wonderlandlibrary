/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.CategoryButton;
import me.arithmo.gui.click.components.CategoryPanel;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.module.Module;
import me.arithmo.module.data.Setting;

public class Slider {
    public Module module;
    public float x;
    public float y;
    public String name;
    public Setting setting;
    public CategoryPanel panel;
    public boolean dragging;
    public double dragX;
    public double lastDragX;

    public Slider(CategoryPanel panel, float x, float y, Setting setting) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.setting = setting;
        panel.categoryButton.panel.theme.SliderContructor(this, panel);
    }

    public Slider(CategoryPanel panel, float x, float y, Setting setting, Module module) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.setting = setting;
        this.module = module;
        panel.categoryButton.panel.theme.SliderContructor(this, panel);
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.SliderDraw(this, x, y, this.panel);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.SliderMouseClicked(this, x, y, button, this.panel);
        }
    }

    public void mouseReleased(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.SliderMouseMovedOrUp(this, x, y, button, this.panel);
        }
    }
}

