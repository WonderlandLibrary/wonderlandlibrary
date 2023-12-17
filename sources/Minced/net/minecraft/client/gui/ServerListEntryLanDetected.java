// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.Minecraft;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final GuiMultiplayer screen;
    protected final Minecraft mc;
    protected final LanServerInfo serverData;
    private long lastClickTime;
    
    protected ServerListEntryLanDetected(final GuiMultiplayer p_i47141_1_, final LanServerInfo p_i47141_2_) {
        this.screen = p_i47141_1_;
        this.serverData = p_i47141_2_;
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
        this.mc.fontRenderer.drawString(I18n.format("lanServer.title", new Object[0]), x + 32 + 3, y + 1, 16777215);
        this.mc.fontRenderer.drawString(this.serverData.getServerMotd(), x + 32 + 3, y + 12, 8421504);
        if (this.mc.gameSettings.hideServerAddress) {
            this.mc.fontRenderer.drawString(I18n.format("selectServer.hiddenAddress", new Object[0]), x + 32 + 3, y + 12 + 11, 3158064);
        }
        else {
            this.mc.fontRenderer.drawString(this.serverData.getServerIpPort(), x + 32 + 3, y + 12 + 11, 3158064);
        }
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
        this.screen.selectServer(slotIndex);
        if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.screen.connectToSelected();
        }
        this.lastClickTime = Minecraft.getSystemTime();
        return false;
    }
    
    @Override
    public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public LanServerInfo getServerData() {
        return this.serverData;
    }
}
