package com.ohare.client.gui.tab;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import com.ohare.client.gui.tab.component.Component;
import com.ohare.client.gui.tab.component.impl.CategoryComponent;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.visuals.hudcomps.comps.TabGUIComp;
import com.ohare.client.utils.RenderUtil;

import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 7/13/2019
 **/
public class TabGUI {
    private float x, y;
    private ArrayList<Component> components = new ArrayList();
    private Module.Category selectedCategory;
    private ArrayList<Module.Category> categories;
    private boolean extended,halfway;

    public TabGUI(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void init() {
        extended = false;
        categories = new ArrayList();
        selectedCategory = Module.Category.COMBAT;
        for (Module.Category category : Module.Category.values()) {
            categories.add(category);
        }
        categories.sort(Comparator.comparing(c -> c.name()));
        float incY = 0;
        for (Module.Category category : categories) {
            components.add(new CategoryComponent(this, category, x, incY, 100, 12));
            incY += 12;
        }
        components.forEach(component -> component.init());
    }

    public void updatePosition() {
        components.forEach(component -> {
            component.setX(getX());
            component.updatePosition();
        });
    }

    public void draw(TabGUIComp comp, ScaledResolution sr) {
        halfway = getX() + 50 > sr.getScaledWidth() / 2;
        if (comp.roundedTab.isEnabled()) {
            RenderUtil.drawRoundedRectWithShadow(getX(),getY(),100,components.size() * 12,15,new Color(38, 46, 52, 255).getRGB());
        }
        components.forEach(component -> component.draw(sr));
    }

    public void keypress(int key) {
        switch (key) {
            case Keyboard.KEY_DOWN:
                if (!extended) {
                    if (categories.indexOf(selectedCategory) + 1 >= categories.size()) {
                        selectedCategory = categories.get(0);
                        return;
                    }
                    selectedCategory = categories.get(categories.indexOf(selectedCategory) + 1);
                }
                break;
            case Keyboard.KEY_UP:
                if (!extended) {
                    if (categories.indexOf(selectedCategory) <= 0) {
                        selectedCategory = categories.get(categories.size() - 1);
                        return;
                    }
                    selectedCategory = categories.get(categories.indexOf(selectedCategory) - 1);
                }
                break;
            case Keyboard.KEY_RIGHT:
                if (halfway) {
                    if (extended) setExtended(false);
                } else {
                    if (!extended) setExtended(true);
                }
                break;
            case Keyboard.KEY_LEFT:
                if (halfway) {
                    if (!extended) setExtended(true);
                } else {
                    if (extended) setExtended(false);
                }
                break;
        }
        if (extended) components.forEach(component -> component.keypress(key));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Module.Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Module.Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isHalfway() {
        return halfway;
    }
}
