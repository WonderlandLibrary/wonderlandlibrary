package koks.gui.clickgui.normal;

import koks.Koks;
import koks.gui.clickgui.normal.elements.DrawModule;
import koks.manager.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:01
 */
public class DrawCategory {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private final ArrayList<DrawModule> drawModules = new ArrayList<>();
    public final Module.Category category;
    public int x, y, width, height, dragX, dragY;
    public boolean extended, dragging;

    public DrawCategory(Module.Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            if (module.getCategory() == category) {
                drawModules.add(new DrawModule(module));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }

        Gui.drawRect(x - 3, y, x + width + 3, y + height, Koks.getKoks().clientColor.getRGB());
        String name = category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase();
        fr.drawStringWithShadow(name, x + width / 2 - fr.getStringWidth(name) / 2, y + height / 2 - fr.FONT_HEIGHT / 2, 0xFFFFFFFF);

        for (DrawModule drawModule : drawModules) {
            String string = drawModule.module.getName() + (drawModule.elements.isEmpty() ? "" : (drawModule.extended ? "-" : "+"));
            if (width < fr.getStringWidth(string) + 22) {
                width = fr.getStringWidth(string) + 22;
            }
        }

        if (extended) {
            int y = this.y + this.height;
            for (DrawModule drawModule : drawModules) {
                drawModule.updatePosition(x, y, width, height);
                drawModule.drawScreen(mouseX, mouseY, partialTicks);
                y += this.height;
            }
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x - 3 && mouseX <= x + width + 3 && mouseY > y && mouseY < y + height;
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (extended) {
            for (DrawModule drawModule : drawModules) {
                drawModule.keyTyped(typedChar, keyCode);
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragX = x - mouseX;
                dragY = y - mouseY;
                dragging = true;
            }
            if (mouseButton == 1) {
                extended = !extended;
            }
        }

        if (extended) {
            for (DrawModule drawModule : drawModules) {
                drawModule.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;

        if (extended) {
            for (DrawModule drawModule : drawModules) {
                drawModule.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

}