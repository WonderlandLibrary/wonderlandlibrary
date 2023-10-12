package wtf.diablo.gui.clickGuiMaterial.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainPanel {

    public double x, y, offsetX, offsetY;
    public boolean dragging;
    public int width = 100, height = 225;

    public Color main = new Color(38, 38, 38);
    public Color boarder = new Color(28, 28, 28);
    public Color backdrop = new Color(54, 54, 54);
    public int themeColor = ColorUtil.getColor(0);

    public ArrayList<Panel> panels = new ArrayList();

    public MainPanel(double x, double y) {
        this.x = x;
        this.y = y;

        double offset = 50;

        for(Category c : Category.values()){
            panels.add(new Panel(x + offset,y,50,40,c,offset,this));
            offset += 50;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int count = 0;
        for (Category c : Category.values()) {
            count++;
        }

        if (dragging) {
            y = mouseY + offsetY;
            x = mouseX + offsetX;
        }

        this.height = 225;
        this.width = 50 + (count * 50);

        float transparency = 1;

        if (!RenderUtil.isHovered(mouseX, mouseY, (float) x - 3, (float) y - 3, (float) (x + width) + 3, (float) (y + height) + 3)) {
            transparency = 0.6f;
        }

        //GL11.glPushMatrix();
        RenderUtil.drawRectAlpha((float) x - 3, (float) y - 3, (float) (x + width) + 3, (float) (y + height) + 3, backdrop.getRGB(), transparency);
        RenderUtil.drawRectAlpha((float) x - 1, (float) y - 1, (float) (x + width) + 1, (float) (y + height) + 1, boarder.getRGB(), transparency);
        RenderUtil.drawRectAlpha((float) x, (float) y, (float) (x + width), (float) (y + height), main.getRGB(), transparency);
        RenderUtil.drawRectAlpha((float) x, (float) y, (float) (x + width), (float) (y + 2), themeColor, transparency);

        //GL11.glPopMatrix();

        int imgSize = 34;

        GL11.glPushMatrix();
        GL11.glColor4f(1,1,1,1);
        RenderUtil.drawImage(x + 6, y  + 6,imgSize,imgSize,new ResourceLocation("diablo/icons/logo.png"),transparency);
        GL11.glPopMatrix();

        for(Panel p : panels){
            p.drawScreen(mouseX,mouseY,partialTicks);
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtil.isHovered(mouseX, mouseY, x, y, x + width, y + height)) {
            if (mouseButton == 0) {
                dragging = true;
                offsetX = x - mouseX;
                offsetY = y - mouseY;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        offsetY = 0;
        offsetX = 0;
    }

    public void drawIcon() {
        /*
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

         */
    }
}
