// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons.setting;

import java.io.IOException;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.augustus.settings.Setting;

public class CSSetting
{
    public int x;
    public int y;
    public int width;
    public int height;
    public Setting set;
    public String displayString;
    public Minecraft mc;
    public FontRenderer fr;
    
    public CSSetting(final int x, final int y, final int width, final int height, final Setting s) {
        this.mc = Minecraft.getMinecraft();
        this.fr = this.mc.fontRendererObj;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.set = s;
        this.displayString = s.getName();
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
