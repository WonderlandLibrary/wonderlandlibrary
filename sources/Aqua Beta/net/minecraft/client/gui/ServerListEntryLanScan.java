package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan
implements GuiListExtended.IGuiListEntry {
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        String s;
        FontRenderer cfr_ignored_0 = this.mc.fontRendererObj;
        int i = y + slotHeight / 2 - FontRenderer.FONT_HEIGHT / 2;
        GuiScreen cfr_ignored_1 = this.mc.currentScreen;
        this.mc.fontRendererObj.drawString(I18n.format((String)"lanServer.scanning", (Object[])new Object[0]), GuiScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(I18n.format((String)"lanServer.scanning", (Object[])new Object[0])) / 2, i, 0xFFFFFF);
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
            }
        }
        GuiScreen cfr_ignored_2 = this.mc.currentScreen;
        FontRenderer cfr_ignored_3 = this.mc.fontRendererObj;
        this.mc.fontRendererObj.drawString(s, GuiScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, i + FontRenderer.FONT_HEIGHT, 0x808080);
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        return false;
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }
}
