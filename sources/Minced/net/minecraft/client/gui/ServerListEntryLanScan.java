// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc;
    
    public ServerListEntryLanScan() {
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
        final int i = y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;
        this.mc.fontRenderer.drawString(I18n.format("lanServer.scanning", new Object[0]), this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, i, 16777215);
        String s = null;
        switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
            default: {
                s = "O o o";
                break;
            }
            case 1:
            case 3: {
                s = "o O o";
                break;
            }
            case 2: {
                s = "o o O";
                break;
            }
        }
        this.mc.fontRenderer.drawString(s, this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, i + this.mc.fontRenderer.FONT_HEIGHT, 8421504);
    }
    
    @Override
    public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
        return false;
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
}
