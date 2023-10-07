/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.ArrayList;
import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.CategoryButton;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.components.RGBSlider;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.management.ColorObject;

public class ColorPreview {
    public String colorName;
    public float x;
    public float y;
    public CategoryButton categoryPanel;
    public ColorObject colorObject;
    public ArrayList<RGBSlider> sliders = new ArrayList();

    public ColorPreview(ColorObject colorObject, String colorName, float x, float y, CategoryButton categoryPanel) {
        this.colorObject = colorObject;
        this.categoryPanel = categoryPanel;
        this.colorName = colorName;
        this.x = x;
        this.y = y;
        categoryPanel.panel.theme.colorConstructor(this, x, y);
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.colorPrewviewDraw(this, x, y);
        }
    }
}

