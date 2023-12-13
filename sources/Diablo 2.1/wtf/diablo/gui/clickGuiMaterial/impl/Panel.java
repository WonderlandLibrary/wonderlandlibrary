package wtf.diablo.gui.clickGuiMaterial.impl;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

import java.io.IOException;

public class Panel {
    public double x, y, offsetX, offsetY, width, height,offset;
    public Category category;

    public MainPanel parent;

    public Panel(double x, double y, double width, double height, Category cat,double offset, MainPanel parent) {
        this.x = x;
        this.y = y;
        this.offset = offset;
        this.width = width;
        this.height = height;
        this.category = cat;
        this.parent = parent;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int count = 0;
        for (Category c : Category.values()) {
            count++;
        }

        this.x = parent.x + offset;
        this.y = parent.y;

        float transparency = 1;

        if (!RenderUtil.isHovered(mouseX, mouseY, (float) x - 3, (float) y - 3, (float) (x + width) + 3, (float) (y + height) + 3)) {
            transparency = 0.6f;
        }

        //RenderUtil.drawRect(x,y,x+width,y+height,-1);
        Fonts.IconFontBig.drawStringWithShadow(drawIcon(), x + ( width / 2) - (Fonts.IconFontBig.getStringWidth(drawIcon()) / 2f), y + (height / 2) - (Fonts.IconFontBig.getHeight() / 2f), -1);
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public String drawIcon() {
        switch (category) {
            case COMBAT:
                return "c";
            case MOVEMENT:
                return "n";
            case PLAYER:
                return "p";
            case RENDER:
                return "i";
            case EXPLOIT:
                return "e";
        }
        return "c";
    }
}
