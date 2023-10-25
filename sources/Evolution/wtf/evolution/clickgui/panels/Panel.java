package wtf.evolution.clickgui.panels;

import net.minecraft.client.gui.Gui;
import wtf.evolution.Main;
import wtf.evolution.clickgui.panels.components.BooleanComponent;
import wtf.evolution.clickgui.panels.components.Component;
import wtf.evolution.clickgui.panels.components.ModuleComponent;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel {

    public Category category;
    public float x, y;
    public ArrayList<Component> components = new ArrayList<>();
    public float height;

    public Panel(Category category, float x, float y) {
        this.category = category;
        this.x = x;
        this.y = y;
        int yOffset = 20;
        for (Module module : Main.m.getModulesFromCategory(category)) {
            components.add(new ModuleComponent(module, this, x, y + yOffset, 125, 16));
            yOffset+=16;
        }

    }




    public void render(int mouseX, int mouseY) {
        for (Component component : components) {
            if (component instanceof ModuleComponent) {
                ModuleComponent moduleComponent = (ModuleComponent) component;
                if (moduleComponent.opened) {
                    height = moduleComponent.offset + 20;
                }
            }
        }
        RenderUtil.blur(() -> {
            RoundedUtil.drawRound(x, y, 125, getHeight(), 5, new Color(20, 20, 20, 150));
        }, 10);
        RenderUtil.bloom(() -> {
            RoundedUtil.drawRound(x, y, 125, getHeight(), 5, new Color(20, 20, 20, 150));
        }, 5, 2, 1);
        RoundedUtil.drawRound(x, y, 125, getHeight(), 5, new Color(10, 10, 10, 220));
        Fonts.RUB14.drawCenteredString(category.name(), x + 125 / 2f, y + 7, -1);
        Gui.drawGradientRect(x, y + 17, x + 125, y + 25, new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 0).getRGB());
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, 125,getHeight(), 5, new Color(10, 10, 10, 180));
        StencilUtil.readStencilBuffer(1);
        components.forEach(component -> {
            component.render(mouseX, mouseY);
        });
        StencilUtil.uninitStencilBuffer();

    }

    public float getHeight() {
        float height = 20;
        for (Component component : components) {
            if (component instanceof ModuleComponent) {
                ModuleComponent moduleComponent = (ModuleComponent) component;
                if (moduleComponent.opened) {
                    height = moduleComponent.offset + 20;
                }
            }
        }
        return height;
    }

    public void click(int mouseX, int mouseY, int button) {
        components.forEach(component -> {
                if (RenderUtil.isHovered(mouseX, mouseY, component.x, component.y, component.width, component.height)) {
                    component.click(mouseX, mouseY, button);
                }
        });
    }

}
