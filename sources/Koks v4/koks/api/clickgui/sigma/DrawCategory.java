package koks.api.clickgui.sigma;

import koks.Koks;
import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author kroko
 * @created on 19.02.2021 : 03:16
 */
public class DrawCategory {

    final Minecraft mc = Minecraft.getMinecraft();
    DirtyFontRenderer fontRenderer;
    Module.Category category;
    int x, y, dragX, dragY, width, height, currentScroll;

    boolean drag, expanded = true;

    final int moduleSize = 7;
    final int moduleHeight = 20, maxPanelHeight = (moduleSize + 1) * moduleHeight;

    public final ArrayList<DrawModule> drawModules = new ArrayList<>();

    public DrawCategory(Module.Category category, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;

        int setY = y + height;

        for (Module module : ModuleRegistry.getModules()) {
            if (module.getCategory() == category) {
                drawModules.add(new DrawModule(x, setY, width, moduleHeight, module));
                setY += moduleHeight;
            }
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        this.fontRenderer = Fonts.arial25;
        if (drag) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }

        GuiScreen.drawRect(x, y, x + width, y + height, new Color(21, 23, 24, 255).getRGB());
        fontRenderer.drawString(category.name().charAt(0) + category.name().substring(1).toLowerCase(), x + 5, y + height / 2F - fontRenderer.getStringHeight(category.name()) / 2, Color.white, true);
        int y = this.y + this.height;
        if (expanded)
            for (int i = 0; i < drawModules.size(); i++) {
                final DrawModule drawModule = drawModules.get(i);
                if (i <= moduleSize + currentScroll && i >= currentScroll) {
                    drawModule.updateValues(x, y);
                    drawModule.draw(mouseX, mouseY, partialTicks);
                    y += this.moduleHeight;
                }
            }
        GlStateManager.resetColor();
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (!Koks.getKoks().sigmaClickGUI.displaySetting) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height)
                if (mouseButton == 0) {
                    dragX = x - mouseX;
                    dragY = y - mouseY;
                    drag = true;
                } else if (mouseButton == 1) {
                    expanded = !expanded;
                }

            if (expanded)
                for (int i = 0; i < drawModules.size(); i++) {
                    final DrawModule drawModule = drawModules.get(i);
                    if (i <= moduleSize + currentScroll && i >= currentScroll) {
                        if (mouseX >= drawModule.x && mouseX <= drawModule.x + drawModule.width && mouseY >= drawModule.y && mouseY <= drawModule.y + drawModule.height) {
                            drawModule.onClick(mouseX, mouseY, mouseButton);
                        }
                    }
                }
        }
    }

    public void onRelease() {
        drag = false;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height + maxPanelHeight;
    }

    public void handleMouseInput() {
        if (expanded) {
            final int wheel = Mouse.getEventDWheel();
            if (wheel < 0)
                currentScroll++;
            if (wheel > 0)
                currentScroll--;

            if (currentScroll < 0)
                currentScroll = 0;

            if (currentScroll + moduleSize >= drawModules.size() - 1)
                currentScroll = drawModules.size() - moduleSize - 1;

            if (moduleSize >= drawModules.size())
                currentScroll = 0;
        }
    }
}
