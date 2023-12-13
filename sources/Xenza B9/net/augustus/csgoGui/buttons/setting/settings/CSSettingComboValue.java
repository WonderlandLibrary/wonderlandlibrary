// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons.setting.settings;

import net.augustus.Augustus;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.augustus.settings.StringValue;
import net.augustus.settings.Setting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class CSSettingComboValue
{
    public int x;
    public int y;
    public int width;
    public int height;
    public String displayString;
    public Minecraft mc;
    public FontRenderer fr;
    public String[] values;
    public Setting set;
    
    public CSSettingComboValue(final int x, final int y, final int width, final int height, final Setting s, final String displayString) {
        this.mc = Minecraft.getMinecraft();
        this.fr = this.mc.fontRendererObj;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayString = displayString;
        this.set = s;
        this.values = ((StringValue)s).getStringList();
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.isHovered(mouseX, mouseY)) {
            ((StringValue)this.set).setString(this.displayString);
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
        final int color = ((StringValue)this.set).getSelected().equalsIgnoreCase(this.displayString) ? Augustus.getInstance().getClientColor().getRGB() : Integer.MAX_VALUE;
        this.fr.drawString(this.displayString, this.x + this.width / 2 - this.fr.getStringWidth(this.displayString) / 2, this.y + 1, color);
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        final boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
        final boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
        return hoveredx && hoveredy;
    }
}
