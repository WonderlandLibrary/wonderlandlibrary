package wtf.diablo.gui.clickGui.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import wtf.diablo.Diablo;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Panel {

    public ArrayList<Button> buttons = new ArrayList<>();

    public double x, y, offsetX, offsetY;
    public boolean dragging, collapsed;
    public Category category;
    public int width = 100, barHeight = 15;
    private double totalHeight;
    private double scrolled = 0;
    private double fixedHeight = 150;
    public Color color;

    public Panel(int x, Category c) {
        this.x = x;
        this.y = 5;
        this.category = c;
        this.color = this.category.getColor();
        int count = 0;
        for (Module mod : Diablo.moduleManager.getModulesByCategory(category, Fonts.SFReg18)) {
            buttons.add(new Button(this, mod, count));
            count++;
        }
    }
    public double settingsHeight = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if (dragging) {
            x = mouseX + offsetX;
            y = mouseY + offsetY;
        }

        int mainColor = this.color.getRGB();

        Gui.drawRect(x, y, x + width,y + barHeight + fixedHeight,mainColor);
        if(fixedHeight > 1)
            RenderUtil.drawRoundedRect(x,y + barHeight,width,fixedHeight,5, 0xFF131313);

        drawIcon();
        double tempTotalHeight = 0;
        Fonts.SFReg18.drawStringWithShadow(category.getName(), x + 17, y + 0.5 + ((barHeight - Fonts.SFReg18.getHeight()) / 2), -1);
        for(Button b : buttons)
            tempTotalHeight += b.getHeight();
        settingsHeight = 0;

        fixedHeight = 0;
        if(!collapsed) {
            for (Button b : buttons) {
                settingsHeight += b.drawScreen(mouseX, mouseY, partialTicks, settingsHeight);
            }
            fixedHeight = totalHeight;
        }
        RenderUtil.drawOutlinedRoundedRect(x,y,width, fixedHeight + barHeight, 5,4,mainColor);
        GlStateManager.pushMatrix();
        if(fixedHeight > 1)
            RenderUtil.drawOutlinedRoundedRect(x + 1,y + barHeight - 0.5,width - 2, fixedHeight, 5, 3, 0xFF333333);
        GlStateManager.popMatrix();
        totalHeight = tempTotalHeight;
    }

    public void drawIcon() {
        switch (category) {
            case COMBAT:
                Fonts.IconFont.drawStringWithShadow("c", x + 3, y + 1 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case MOVEMENT:
                Fonts.IconFont.drawStringWithShadow("n", x + 4.5, y + 1 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case PLAYER:
                Fonts.IconFont.drawStringWithShadow("p", x + 5.5, y + 1 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case RENDER:
                Fonts.IconFont.drawStringWithShadow("i", x + 4, y + 1 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
            case EXPLOIT:
                Fonts.IconFont.drawStringWithShadow("e", x + 4.5, y + 1 + ((barHeight - Fonts.IconFont.getHeight()) / 2), -1);
                break;
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Button b : buttons) {
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
        for (Button b : buttons) {
            if (!collapsed) b.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        dragging = false;
        offsetY = 0;
        offsetX = 0;
        for(Button b : buttons){
            b.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void scroll(int i, int lastMouseX, int lastMouseY) {
        if(scrolled >= 0 && i >= 0)
            return;
        if(-scrolled >= totalHeight - fixedHeight / 2 && i <= 0)
            return;
        if(RenderUtil.isHovered(lastMouseX,lastMouseY, x,y,x + width,y + fixedHeight + barHeight)) {
            scrolled += i;
        }
    }

    public void onOpenGUI() {
        if(!collapsed) {
            fixedHeight = 0;
        }
        for(Button button : buttons){
            button.onOpenGUI();
        }
    }
}