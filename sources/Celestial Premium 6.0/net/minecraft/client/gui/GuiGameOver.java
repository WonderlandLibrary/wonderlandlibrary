/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.helpers.misc.ChatHelper;

public class GuiGameOver
extends GuiScreen {
    private int enableButtonsTimer;
    private final ITextComponent causeOfDeath;
    public static boolean isGhost;

    public GuiGameOver(@Nullable ITextComponent p_i46598_1_) {
        this.causeOfDeath = p_i46598_1_;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.enableButtonsTimer = 0;
        if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen." + (this.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"), new Object[0])));
        } else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, "Bypass screen"));
            if (this.mc.getSession() == null) {
                ((GuiButton)this.buttonList.get((int)1)).enabled = false;
            }
        }
        for (GuiButton guibutton : this.buttonList) {
            guibutton.enabled = false;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.player.respawnPlayer();
                this.mc.displayGuiScreen(null);
                break;
            }
            case 1: {
                if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
                this.mc.displayGuiScreen(guiyesno);
                guiyesno.setButtonDelay(20);
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(null);
                this.mc.player.isDead = false;
                this.mc.player.setHealth(20.0f);
                this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ);
                this.mc.displayGuiScreen(null);
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Please write " + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "\".res\" " + (Object)((Object)ChatFormatting.WHITE) + "to respawn");
                isGhost = true;
            }
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (result) {
            if (this.mc.world != null) {
                this.mc.world.sendQuittingDisconnectingPacket();
            }
            this.mc.loadWorld(null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        } else {
            this.mc.player.respawnPlayer();
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ITextComponent itextcomponent;
        boolean flag = this.mc.world.getWorldInfo().isHardcoreModeEnabled();
        this.drawGradientRect(0.0f, 0.0f, this.width, this.height, 0x60500000, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.drawCenteredString(this.fontRendererObj, "WASTED", this.width / 2 / 2, 30, 13897216);
        GlStateManager.popMatrix();
        if (this.causeOfDeath != null) {
            this.drawCenteredString(this.fontRendererObj, this.causeOfDeath.getFormattedText(), this.width / 2, 85, 0xFFFFFF);
        }
        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + (Object)((Object)TextFormatting.YELLOW) + this.mc.player.getScore(), this.width / 2, 100, 0xFFFFFF);
        if (this.causeOfDeath != null && mouseY > 85 && mouseY < 85 + this.fontRendererObj.FONT_HEIGHT && (itextcomponent = this.getClickedComponentAt(mouseX)) != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Nullable
    public ITextComponent getClickedComponentAt(int p_184870_1_) {
        if (this.causeOfDeath == null) {
            return null;
        }
        int i = this.mc.fontRendererObj.getStringWidth(this.causeOfDeath.getFormattedText());
        int j = this.width / 2 - i / 2;
        int k = this.width / 2 + i / 2;
        int l = j;
        if (p_184870_1_ >= j && p_184870_1_ <= k) {
            for (ITextComponent itextcomponent : this.causeOfDeath) {
                if ((l += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(itextcomponent.getUnformattedComponentText(), false))) <= p_184870_1_) continue;
                return itextcomponent;
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
            for (GuiButton guibutton : this.buttonList) {
                guibutton.enabled = true;
            }
        }
    }
}

