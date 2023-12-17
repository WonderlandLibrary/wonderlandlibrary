// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.resources.I18n;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;

public class GuiGameOver extends GuiScreen
{
    private int enableButtonsTimer;
    private final ITextComponent causeOfDeath;
    
    public GuiGameOver(@Nullable final ITextComponent causeOfDeathIn) {
        this.causeOfDeath = causeOfDeathIn;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.enableButtonsTimer = 0;
        if (GuiGameOver.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen." + (GuiGameOver.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"), new Object[0])));
        }
        else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));
            if (GuiGameOver.mc.getSession() == null) {
                this.buttonList.get(1).enabled = false;
            }
        }
        for (final GuiButton guibutton : this.buttonList) {
            guibutton.enabled = false;
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                final Minecraft mc = GuiGameOver.mc;
                Minecraft.player.respawnPlayer();
                GuiGameOver.mc.displayGuiScreen(null);
                break;
            }
            case 1: {
                if (GuiGameOver.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
                    GuiGameOver.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                final GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
                GuiGameOver.mc.displayGuiScreen(guiyesno);
                guiyesno.setButtonDelay(20);
                break;
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result) {
            if (GuiGameOver.mc.world != null) {
                GuiGameOver.mc.world.sendQuittingDisconnectingPacket();
            }
            GuiGameOver.mc.loadWorld(null);
            GuiGameOver.mc.displayGuiScreen(new GuiMainMenu());
        }
        else {
            final Minecraft mc = GuiGameOver.mc;
            Minecraft.player.respawnPlayer();
            GuiGameOver.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final boolean flag = GuiGameOver.mc.world.getWorldInfo().isHardcoreModeEnabled();
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.drawCenteredString(this.fontRenderer, I18n.format(flag ? "deathScreen.title.hardcore" : "deathScreen.title", new Object[0]), this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();
        if (this.causeOfDeath != null) {
            this.drawCenteredString(this.fontRenderer, this.causeOfDeath.getFormattedText(), this.width / 2, 85, 16777215);
        }
        final FontRenderer fontRenderer = this.fontRenderer;
        final StringBuilder append = new StringBuilder().append(I18n.format("deathScreen.score", new Object[0])).append(": ").append(TextFormatting.YELLOW);
        final Minecraft mc = GuiGameOver.mc;
        this.drawCenteredString(fontRenderer, append.append(Minecraft.player.getScore()).toString(), this.width / 2, 100, 16777215);
        if (this.causeOfDeath != null && mouseY > 85 && mouseY < 85 + this.fontRenderer.FONT_HEIGHT) {
            final ITextComponent itextcomponent = this.getClickedComponentAt(mouseX);
            if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
                this.handleComponentHover(itextcomponent, mouseX, mouseY);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Nullable
    public ITextComponent getClickedComponentAt(final int p_184870_1_) {
        if (this.causeOfDeath == null) {
            return null;
        }
        final int i = GuiGameOver.mc.fontRenderer.getStringWidth(this.causeOfDeath.getFormattedText());
        final int j = this.width / 2 - i / 2;
        final int k = this.width / 2 + i / 2;
        int l = j;
        if (p_184870_1_ >= j && p_184870_1_ <= k) {
            for (final ITextComponent itextcomponent : this.causeOfDeath) {
                l += GuiGameOver.mc.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(itextcomponent.getUnformattedComponentText(), false));
                if (l > p_184870_1_) {
                    return itextcomponent;
                }
            }
            return null;
        }
        return null;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.enableButtonsTimer;
        if (this.enableButtonsTimer == 20) {
            for (final GuiButton guibutton : this.buttonList) {
                guibutton.enabled = true;
            }
        }
    }
}
