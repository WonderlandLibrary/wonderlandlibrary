// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft field_148288_a;
    private static final String __OBFID = "CL_00000815";
    
    public ServerListEntryLanScan() {
        this.field_148288_a = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        final int var9 = y + slotHeight / 2 - this.field_148288_a.fontRendererObj.FONT_HEIGHT / 2;
        this.field_148288_a.fontRendererObj.drawString(I18n.format("lanServer.scanning", new Object[0]), this.field_148288_a.currentScreen.width / 2 - this.field_148288_a.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, var9, 16777215);
        String var10 = null;
        switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
            default: {
                var10 = "O o o";
                break;
            }
            case 1:
            case 3: {
                var10 = "o O o";
                break;
            }
            case 2: {
                var10 = "o o O";
                break;
            }
        }
        this.field_148288_a.fontRendererObj.drawString(var10, this.field_148288_a.currentScreen.width / 2 - this.field_148288_a.fontRendererObj.getStringWidth(var10) / 2, var9 + this.field_148288_a.fontRendererObj.FONT_HEIGHT, 8421504);
    }
    
    @Override
    public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
    }
    
    @Override
    public boolean mousePressed(final int p_148278_1_, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        return false;
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
}
