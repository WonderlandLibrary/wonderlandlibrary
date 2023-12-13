// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons;

import java.io.IOException;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class CSButton extends Gui
{
    public int x;
    public int y;
    public int width;
    public int height;
    public int color;
    public String displayString;
    public Minecraft mc;
    public FontRenderer fr;
    
    public CSButton(final int x, final int y, final int width, final int height, final int color, final String displayString) {
        this.mc = Minecraft.getMinecraft();
        this.fr = this.mc.fontRendererObj;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.displayString = displayString;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void initButton() {
    }
}
