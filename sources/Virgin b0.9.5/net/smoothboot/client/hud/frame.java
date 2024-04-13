package net.smoothboot.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.Virginclient;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.modmanager;
import net.smoothboot.client.setting.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class frame {
    public int x, y, width, height, dragX, dragY;
    public Mod.Category category;
    public boolean dragging, extended;
    protected List<modbutton> buttons;
    protected MinecraftClient mc = MinecraftClient.getInstance();

    public frame(Mod.Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dragging = false;
        this.extended = true;
        buttons = new ArrayList<>();
        int offset = height;
        for (Mod mod : modmanager.INSTANCE.getModulesInCategory(category)) {
            buttons.add(new modbutton(mod, this, offset));
            offset += height;
        }
    }

    public static int menured = 190;
    public static int menugreen = 130;
    public static int menublue = 255;
    public static  int menured2 = 40;
    public static int menugreen2 = 40;
    public static int menublue2 = 40;

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(x, y, x + width, y + height, new Color(menured, menugreen, menublue, 220).getRGB(), new Color(menured2, menugreen2, menublue2, 220).getRGB());
        context.drawText(mc.textRenderer, category.name(), x + 2, y + 3, new Color(255, 255, 255, 220).getRGB(), true);
        context.drawText(mc.textRenderer, extended ? "-" : "+", x + width - 10, y + 2, new Color(255, 255, 255, 220).getRGB(), false);
        context.drawText(mc.textRenderer,"Virgin Client " + Virginclient.clientVersion, 2, 2, new Color(menured, menugreen, menublue, 220).getRGB(), true);
        if (extended) {
            for (modbutton button : buttons) {
                button.render(context, mouseX, mouseY, delta);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                dragging = true;
                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
            }
            else if (button == 1) {
                extended = !extended;
            }

        }
        if (extended) {
            for (modbutton mb : buttons) {
                mb.mouseClicked(mouseX, mouseY, button);
            }
        }
        if (button == 2) {
           extended = !extended;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
        }
        for (modbutton mb : buttons) {
            mb.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }

    public void updateButtons() {
        int offset = height;
        for (modbutton button : buttons) {
            button.offset = offset;
            offset += height;

            if (button.extended) {
                for (Component component : button.components) {
                    if (component.setting.isVisible()) offset += height;
                }
            }
        }
    }

    public void keyPressed(int key) {
        for (modbutton mb : buttons) {
            mb.keyPressed(key);
        }
    }
}
