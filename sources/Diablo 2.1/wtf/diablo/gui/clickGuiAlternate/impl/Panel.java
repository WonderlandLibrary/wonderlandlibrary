package wtf.diablo.gui.clickGuiAlternate.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import wtf.diablo.Diablo;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Panel {

    public ArrayList<wtf.diablo.gui.clickGuiAlternate.impl.Button> buttons = new ArrayList<>();

    public double x, y, offsetX, offsetY, barX;
    public boolean dragging, collapsed;
    public Category category;
    public int width = 100, barHeight = 16, barWidth = 104;
    private double totalHeight;
    private double scrolled = 0;
    private double fixedHeight = 150;
    public int color;

    public Panel(int x, Category c) {
        this.barX = x;
        this.x = barX + 2;
        this.y = 5;
        this.category = c;
        int count = 0;
        for (Module mod : Diablo.moduleManager.getModulesByCategory(category, Fonts.SFReg18)) {
            buttons.add(new wtf.diablo.gui.clickGuiAlternate.impl.Button(this, mod, count));
            count++;
        }
    }

    public double settingsHeight = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.color = ColorUtil.getColor(0);

        if (dragging) {
            barX = mouseX + offsetX;
            y = mouseY + offsetY;
            x = barX + 2;
        }
        int mainColor = this.color;


        Gui.drawRect(barX, y, barX + barWidth, y + barHeight, mainColor);

        drawIcon();
        double tempTotalHeight = 0;
        Fonts.SFReg18.drawStringWithShadow(category.getName(), x + 17, y + 1 + ((barHeight - Fonts.SFReg18.getHeight()) / 2f), -1);
        for (wtf.diablo.gui.clickGuiAlternate.impl.Button b : buttons)
            tempTotalHeight += b.getHeight();
        settingsHeight = 0;

        fixedHeight = 0;
        if (!collapsed) {
            for (wtf.diablo.gui.clickGuiAlternate.impl.Button b : buttons) {
                settingsHeight += b.drawScreen(mouseX, mouseY, partialTicks, settingsHeight);
            }
            fixedHeight = totalHeight;
        }
        totalHeight = settingsHeight;
    }

    public void drawIcon() {
        switch (category) {
            case COMBAT:
                Fonts.IconFont.drawStringWithShadow("c", x + 3, y + 2 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case MOVEMENT:
                Fonts.IconFont.drawStringWithShadow("n", x + 4.5, y + 2 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case PLAYER:
                Fonts.IconFont.drawStringWithShadow("p", x + 5.5, y + 2 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case RENDER:
                Fonts.IconFont.drawStringWithShadow("i", x + 4, y + 2 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case EXPLOIT:
                Fonts.IconFont.drawStringWithShadow("e", x + 4.5, y + 2 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (wtf.diablo.gui.clickGuiAlternate.impl.Button b : buttons) {
            b.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtil.isHovered(mouseX, mouseY, x, y, x + width, y + barHeight)) {
            if (mouseButton == 0) {
                dragging = true;
                offsetX = x - mouseX;
                offsetY = y - mouseY;
            } else if (mouseButton == 1) {
                collapsed = !collapsed;
            }
        }
        for (wtf.diablo.gui.clickGuiAlternate.impl.Button b : buttons) {
            if (!collapsed) b.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        offsetY = 0;
        offsetX = 0;
        for (wtf.diablo.gui.clickGuiAlternate.impl.Button b : buttons) {
            b.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void scroll(int i, int lastMouseX, int lastMouseY) {
        if (scrolled >= 0 && i >= 0)
            return;
        if (-scrolled >= totalHeight - fixedHeight / 2 && i <= 0)
            return;
        if (RenderUtil.isHovered(lastMouseX, lastMouseY, x, y, x + width, y + fixedHeight + barHeight)) {
            scrolled += i;
        }
    }

    public void onOpenGUI() {
        if (!collapsed) {
            fixedHeight = 0;
        }
        for (Button button : buttons) {
            button.onOpenGUI();
        }
    }
}