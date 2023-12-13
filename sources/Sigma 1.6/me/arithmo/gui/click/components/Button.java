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
import me.arithmo.management.animate.Rotate;
import me.arithmo.management.animate.Translate;
import me.arithmo.module.Module;

public class Button {
    public Rotate rotate = new Rotate(0.0f);
    public Translate translate = new Translate(0.0f, 0.0f);
    public float x;
    public float y;
    public String name;
    public CategoryPanel panel;
    public boolean enabled;
    public Module module;
    public boolean isBinding;

    public Button(CategoryPanel panel, String name, float x, float y, Module module) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.module = module;
        this.enabled = module.isEnabled();
        panel.categoryButton.panel.theme.buttonContructor(this, this.panel);
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            if (!this.panel.visible) continue;
            theme.buttonDraw(this, x, y, this.panel);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.buttonMouseClicked(this, x, y, button, this.panel);
        }
    }

    public void keyPressed(int key) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.buttonKeyPressed(this, key);
        }
    }
}

