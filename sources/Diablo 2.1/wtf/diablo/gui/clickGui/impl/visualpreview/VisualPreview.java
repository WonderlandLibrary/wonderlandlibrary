package wtf.diablo.gui.clickGui.impl.visualpreview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

public class VisualPreview {
    public double x, y, x2, y2, offsetX, offsetY;
    public boolean dragging, collapsed;
    public Category category;
    public double width, height, barOffset;

    public VisualPreview(double x, double y) {
        this.width = 100;
        this.height = 200;
        this.barOffset = 13;
        this.x = x;
        this.y = y;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();

        if (dragging) {
            x = mouseX + offsetX;
            y = mouseY + offsetY;
        }

        this.x2 = x + width;
        this.y2 = y + height;

        Gui.drawRect(x, y, x2, y + barOffset, 0xFF131313);

        Fonts.IconFont.drawStringWithShadow("i", x + 4.5, y + 1 + (Fonts.IconFont.getHeight() / 2), -1);
        Fonts.SFReg18.drawStringWithShadow("Visual Preview", x + 19, y + 0.5 + (Fonts.SFReg18.getHeight() / 2), -1);

        if (!collapsed) {
            Gui.drawRect(x, y + barOffset, x2, y2, 0x80000000);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiInventory.drawEntityOnScreen((int) (x + (width / 2)), (int) (y2 - 7), 90, 40, 6, mc.thePlayer);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {

    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovered(mouseX, mouseY, x, y, x2, y + barOffset)) {
            switch (mouseButton) {
                case 0:
                    dragging = true;
                    offsetX = x - mouseX;
                    offsetY = y - mouseY;
                    break;
                case 1:
                    dragging = false;
                    collapsed = !collapsed;
                    break;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        switch (state) {
            case 0:
                dragging = false;
                offsetY = 0;
                offsetX = 0;
                break;
            case 1:
                break;
        }
    }

}
