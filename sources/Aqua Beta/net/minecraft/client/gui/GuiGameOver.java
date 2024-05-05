package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiGameOver
extends GuiScreen
implements GuiYesNoCallback {
    private int enableButtonsTimer;
    private boolean field_146346_f = false;

    public void initGui() {
        this.buttonList.clear();
        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (this.mc.isIntegratedServerRunning()) {
                this.buttonList.add((Object)new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format((String)"deathScreen.deleteWorld", (Object[])new Object[0])));
            } else {
                this.buttonList.add((Object)new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format((String)"deathScreen.leaveServer", (Object[])new Object[0])));
            }
        } else {
            this.buttonList.add((Object)new GuiButton(0, width / 2 - 100, height / 4 + 72, I18n.format((String)"deathScreen.respawn", (Object[])new Object[0])));
            this.buttonList.add((Object)new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.format((String)"deathScreen.titleScreen", (Object[])new Object[0])));
            if (this.mc.getSession() == null) {
                ((GuiButton)this.buttonList.get((int)1)).enabled = false;
            }
        }
        for (GuiButton guibutton : this.buttonList) {
            guibutton.enabled = false;
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen)null);
                break;
            }
            case 1: {
                if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                    this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
                    break;
                }
                GuiYesNo guiyesno = new GuiYesNo((GuiYesNoCallback)this, I18n.format((String)"deathScreen.quit.confirm", (Object[])new Object[0]), "", I18n.format((String)"deathScreen.titleScreen", (Object[])new Object[0]), I18n.format((String)"deathScreen.respawn", (Object[])new Object[0]), 0);
                this.mc.displayGuiScreen((GuiScreen)guiyesno);
                guiyesno.setButtonDelay(20);
            }
        }
    }

    public void confirmClicked(boolean result, int id) {
        if (result) {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
        } else {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, width, height, 0x60500000, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        boolean flag = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        String s = flag ? I18n.format((String)"deathScreen.title.hardcore", (Object[])new Object[0]) : I18n.format((String)"deathScreen.title", (Object[])new Object[0]);
        this.drawCenteredString(this.fontRendererObj, s, width / 2 / 2, 30, 0xFFFFFF);
        GlStateManager.popMatrix();
        if (flag) {
            this.drawCenteredString(this.fontRendererObj, I18n.format((String)"deathScreen.hardcoreInfo", (Object[])new Object[0]), width / 2, 144, 0xFFFFFF);
        }
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"deathScreen.score", (Object[])new Object[0]) + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), width / 2, 100, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

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
