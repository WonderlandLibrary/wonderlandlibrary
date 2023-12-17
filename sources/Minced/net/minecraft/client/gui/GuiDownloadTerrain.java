// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.optifine.CustomLoadingScreens;
import net.optifine.CustomLoadingScreen;

public class GuiDownloadTerrain extends GuiScreen
{
    private CustomLoadingScreen customLoadingScreen;
    
    public GuiDownloadTerrain() {
        this.customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.customLoadingScreen != null) {
            this.customLoadingScreen.drawBackground(this.width, this.height);
        }
        else {
            this.drawBackground(0);
        }
        this.drawCenteredString(this.fontRenderer, I18n.format("multiplayer.downloadingTerrain", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
