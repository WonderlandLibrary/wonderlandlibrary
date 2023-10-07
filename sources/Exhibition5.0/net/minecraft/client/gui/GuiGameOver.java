// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.gui.screen.impl.mainmenu.ClientMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.resources.I18n;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback
{
    private int field_146347_a;
    private boolean field_146346_f;
    private static final String __OBFID = "CL_00000690";
    
    public GuiGameOver() {
        this.field_146346_f = false;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (this.mc.isIntegratedServerRunning()) {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld", new Object[0])));
            }
            else {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer", new Object[0])));
            }
        }
        else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));
            if (this.mc.getSession() == null) {
                this.buttonList.get(1).enabled = false;
            }
        }
        for (final GuiButton var2 : this.buttonList) {
            var2.enabled = false;
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen(null);
                break;
            }
            case 1: {
                final GuiYesNo var2 = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
                this.mc.displayGuiScreen(var2);
                var2.setButtonDelay(20);
                break;
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result) {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            this.mc.displayGuiScreen(new ClientMainMenu());
        }
        else {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        final boolean var4 = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        final String var5 = var4 ? I18n.format("deathScreen.title.hardcore", new Object[0]) : I18n.format("deathScreen.title", new Object[0]);
        this.drawCenteredString(this.fontRendererObj, var5, this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();
        if (var4) {
            this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo", new Object[0]), this.width / 2, 144, 16777215);
        }
        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.field_146347_a;
        if (this.field_146347_a == 20) {
            for (final GuiButton var2 : this.buttonList) {
                var2.enabled = true;
            }
        }
    }
}
