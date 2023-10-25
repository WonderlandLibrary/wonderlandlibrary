/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected
implements GuiListExtended.IGuiListEntry {
    protected final Minecraft mc;
    private final GuiMultiplayer field_148292_c;
    protected final LanServerDetector.LanServer field_148291_b;
    private long field_148290_d = 0L;

    protected ServerListEntryLanDetected(GuiMultiplayer guiMultiplayer, LanServerDetector.LanServer lanServer) {
        this.field_148292_c = guiMultiplayer;
        this.field_148291_b = lanServer;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
        this.field_148292_c.selectServer(n);
        if (Minecraft.getSystemTime() - this.field_148290_d < 250L) {
            this.field_148292_c.connectToSelected();
        }
        this.field_148290_d = Minecraft.getSystemTime();
        return false;
    }

    @Override
    public void setSelected(int n, int n2, int n3) {
    }

    @Override
    public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    @Override
    public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        Minecraft.fontRendererObj.drawString(I18n.format("lanServer.title", new Object[0]), n2 + 32 + 3, n3 + 1, 0xFFFFFF);
        Minecraft.fontRendererObj.drawString(this.field_148291_b.getServerMotd(), n2 + 32 + 3, n3 + 12, 0x808080);
        if (Minecraft.gameSettings.hideServerAddress) {
            Minecraft.fontRendererObj.drawString(I18n.format("selectServer.hiddenAddress", new Object[0]), n2 + 32 + 3, n3 + 12 + 11, 0x303030);
        } else {
            Minecraft.fontRendererObj.drawString(this.field_148291_b.getServerIpPort(), n2 + 32 + 3, n3 + 12 + 11, 0x303030);
        }
    }

    public LanServerDetector.LanServer getLanServer() {
        return this.field_148291_b;
    }
}

