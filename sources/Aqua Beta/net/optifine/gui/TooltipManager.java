package net.optifine.gui;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.gui.GuiScreenOF;
import net.optifine.gui.TooltipProvider;

public class TooltipManager {
    private GuiScreen guiScreen;
    private TooltipProvider tooltipProvider;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public TooltipManager(GuiScreen guiScreen, TooltipProvider tooltipProvider) {
        this.guiScreen = guiScreen;
        this.tooltipProvider = tooltipProvider;
    }

    public void drawTooltips(int x, int y, List buttonList) {
        if (Math.abs((int)(x - this.lastMouseX)) <= 5 && Math.abs((int)(y - this.lastMouseY)) <= 5) {
            GuiButton guibutton;
            int i = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + (long)i && (guibutton = GuiScreenOF.getSelectedButton((int)x, (int)y, (List)buttonList)) != null) {
                Rectangle rectangle = this.tooltipProvider.getTooltipBounds(this.guiScreen, x, y);
                Object[] astring = this.tooltipProvider.getTooltipLines(guibutton, rectangle.width);
                if (astring != null) {
                    if (astring.length > 8) {
                        astring = (String[])Arrays.copyOf((Object[])astring, (int)8);
                        astring[astring.length - 1] = (String)astring[astring.length - 1] + " ...";
                    }
                    if (this.tooltipProvider.isRenderBorder()) {
                        int j = -528449408;
                        this.drawRectBorder(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, j);
                    }
                    Gui.drawRect((int)rectangle.x, (int)rectangle.y, (int)(rectangle.x + rectangle.width), (int)(rectangle.y + rectangle.height), (int)-536870912);
                    for (int l = 0; l < astring.length; ++l) {
                        Object s = astring[l];
                        int k = 0xDDDDDD;
                        if (s.endsWith("!")) {
                            k = 0xFF2020;
                        }
                        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
                        fontrenderer.drawStringWithShadow((String)s, (float)(rectangle.x + 5), (float)(rectangle.y + 5 + l * 11), k);
                    }
                }
            }
        } else {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private void drawRectBorder(int x1, int y1, int x2, int y2, int col) {
        Gui.drawRect((int)x1, (int)(y1 - 1), (int)x2, (int)y1, (int)col);
        Gui.drawRect((int)x1, (int)y2, (int)x2, (int)(y2 + 1), (int)col);
        Gui.drawRect((int)(x1 - 1), (int)y1, (int)x1, (int)y2, (int)col);
        Gui.drawRect((int)x2, (int)y1, (int)(x2 + 1), (int)y2, (int)col);
    }
}
