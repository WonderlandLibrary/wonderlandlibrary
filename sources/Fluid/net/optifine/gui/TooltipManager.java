// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.gui;

import net.minecraft.client.gui.FontRenderer;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import java.util.Arrays;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class TooltipManager
{
    private GuiScreen guiScreen;
    private TooltipProvider tooltipProvider;
    private int lastMouseX;
    private int lastMouseY;
    private long mouseStillTime;
    
    public TooltipManager(final GuiScreen guiScreen, final TooltipProvider tooltipProvider) {
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.mouseStillTime = 0L;
        this.guiScreen = guiScreen;
        this.tooltipProvider = tooltipProvider;
    }
    
    public void drawTooltips(final int x, final int y, final List buttonList) {
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
            final int i = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + i) {
                final GuiButton guibutton = GuiScreenOF.getSelectedButton(x, y, buttonList);
                if (guibutton != null) {
                    final Rectangle rectangle = this.tooltipProvider.getTooltipBounds(this.guiScreen, x, y);
                    String[] astring = this.tooltipProvider.getTooltipLines(guibutton, rectangle.width);
                    if (astring != null) {
                        if (astring.length > 8) {
                            astring = Arrays.copyOf(astring, 8);
                            astring[astring.length - 1] = String.valueOf(astring[astring.length - 1]) + " ...";
                        }
                        if (this.tooltipProvider.isRenderBorder()) {
                            final int j = -528449408;
                            this.drawRectBorder(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, j);
                        }
                        Gui.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, -536870912);
                        for (int l = 0; l < astring.length; ++l) {
                            final String s = astring[l];
                            int k = 14540253;
                            if (s.endsWith("!")) {
                                k = 16719904;
                            }
                            final FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
                            fontrenderer.drawStringWithShadow(s, (float)(rectangle.x + 5), (float)(rectangle.y + 5 + l * 11), k);
                        }
                    }
                }
            }
        }
        else {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }
    
    private void drawRectBorder(final int x1, final int y1, final int x2, final int y2, final int col) {
        Gui.drawRect(x1, y1 - 1, x2, y1, col);
        Gui.drawRect(x1, y2, x2, y2 + 1, col);
        Gui.drawRect(x1 - 1, y1, x1, y2, col);
        Gui.drawRect(x2, y1, x2 + 1, y2, col);
    }
}
