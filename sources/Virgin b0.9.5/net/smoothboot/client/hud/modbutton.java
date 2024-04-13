package net.smoothboot.client.hud;

import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.client.ClientSetting;
import net.smoothboot.client.module.settings.*;
import net.smoothboot.client.setting.Component;
import net.smoothboot.client.setting.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class modbutton {

    public Mod module;
    public frame parent;
    public Setting setting;
    public int offset;
    public List<net.smoothboot.client.setting.Component> components;
    public boolean extended;

    public modbutton(Mod module, frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
        this.extended = false;

        this.components = new ArrayList<>();
        int setOffset = parent.height;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new checkbox(setting,this, setOffset));
            } else if (setting instanceof ModeSetting) {
                components.add(new modebox(setting, this, setOffset));
            } else if (setting instanceof NumberSetting) {
                components.add(new slider(setting, this, setOffset));
            } else if (setting instanceof KeyBindSetting) {
                components.add(new KeyBind(setting, this, setOffset));
            }
            setOffset += parent.height;
        }
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(0, 0, 0, 160).getRGB());
        context.drawText(Mod.mc.textRenderer, module.getName(), parent.x + 2, parent.y + offset + 2, module.isEnabled() ? new Color(255, 255, 255, 220).getRGB() : new Color(160, 160, 160,220).getRGB(), true);
            if (extended) {
                for (net.smoothboot.client.setting.Component component : components) {
                    component.render(context, mouseX, mouseY, delta);
                    }
                }
        if (isHovered(mouseX, mouseY) && ClientSetting.EnabledTooltips == 1) {
            context.drawText(Mod.mc.textRenderer, module.getDescription(),mouseX + 5, mouseY + 5, new Color( 255, 255, 255, 220).getRGB(), true);
        }
        else if(isHovered(mouseX, mouseY) && ClientSetting.EnabledTooltips == 2) {
            context.drawText(Mod.mc.textRenderer, module.getDescription(), 575, 365, new Color( 255, 255, 255, 220).getRGB(), true);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                parent.updateButtons();
                extended = !extended;
                parent.updateButtons();
            }

        }
        for (net.smoothboot.client.setting.Component component : components) {
            component.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (net.smoothboot.client.setting.Component component : components) {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
    }

    public void keyPressed(int key) {
        for (Component component : components) {
            component.keyPressed(key);
        }
    }
}