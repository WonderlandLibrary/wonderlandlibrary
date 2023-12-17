// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.optifine.CustomLoadingScreens;
import net.optifine.CustomLoadingScreen;
import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate
{
    private String title;
    private String stage;
    private int progress;
    private boolean doneWorking;
    private CustomLoadingScreen customLoadingScreen;
    
    public GuiScreenWorking() {
        this.title = "";
        this.stage = "";
        this.customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();
    }
    
    @Override
    public void displaySavingString(final String message) {
        this.resetProgressAndMessage(message);
    }
    
    @Override
    public void resetProgressAndMessage(final String message) {
        this.title = message;
        this.displayLoadingString("Working...");
    }
    
    @Override
    public void displayLoadingString(final String message) {
        this.stage = message;
        this.setLoadingProgress(0);
    }
    
    @Override
    public void setLoadingProgress(final int progress) {
        this.progress = progress;
    }
    
    @Override
    public void setDoneWorking() {
        this.doneWorking = true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.doneWorking) {
            if (!GuiScreenWorking.mc.isConnectedToRealms()) {
                GuiScreenWorking.mc.displayGuiScreen(null);
            }
        }
        else {
            if (this.customLoadingScreen != null && GuiScreenWorking.mc.world == null) {
                this.customLoadingScreen.drawBackground(this.width, this.height);
            }
            else {
                this.drawDefaultBackground();
            }
            if (this.progress > 0) {
                this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 70, 16777215);
                this.drawCenteredString(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 16777215);
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
