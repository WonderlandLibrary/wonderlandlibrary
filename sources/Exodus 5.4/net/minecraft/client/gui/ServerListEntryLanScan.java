/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan
implements GuiListExtended.IGuiListEntry {
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void setSelected(int n, int n2, int n3) {
    }

    @Override
    public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
        return false;
    }

    @Override
    public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    @Override
    public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        String string;
        int n8 = n3 + n5 / 2 - Minecraft.fontRendererObj.FONT_HEIGHT / 2;
        GuiScreen cfr_ignored_0 = this.mc.currentScreen;
        Minecraft.fontRendererObj.drawString(I18n.format("lanServer.scanning", new Object[0]), GuiScreen.width / 2 - Minecraft.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, n8, 0xFFFFFF);
        switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
            default: {
                string = "O o o";
                break;
            }
            case 1: 
            case 3: {
                string = "o O o";
                break;
            }
            case 2: {
                string = "o o O";
            }
        }
        GuiScreen cfr_ignored_1 = this.mc.currentScreen;
        Minecraft.fontRendererObj.drawString(string, GuiScreen.width / 2 - Minecraft.fontRendererObj.getStringWidth(string) / 2, n8 + Minecraft.fontRendererObj.FONT_HEIGHT, 0x808080);
    }
}

