// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import java.io.IOException;
import net.optifine.CustomLoadingScreens;
import net.optifine.CustomLoadingScreen;
import net.minecraft.client.network.NetHandlerPlayClient;

public class GuiDownloadTerrain extends GuiScreen
{
    private NetHandlerPlayClient netHandlerPlayClient;
    private int progress;
    private CustomLoadingScreen customLoadingScreen;
    
    public GuiDownloadTerrain(final NetHandlerPlayClient netHandler) {
        this.customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();
        this.netHandlerPlayClient = netHandler;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
    }
    
    @Override
    public void updateScreen() {
        ++this.progress;
        if (this.progress % 20 == 0) {
            this.netHandlerPlayClient.addToSendQueue(new C00PacketKeepAlive());
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.customLoadingScreen != null) {
            this.customLoadingScreen.drawBackground(this.width, this.height);
        }
        else {
            this.drawBackground(0);
        }
        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
