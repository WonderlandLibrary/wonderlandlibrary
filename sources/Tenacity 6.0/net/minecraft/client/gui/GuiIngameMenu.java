// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenu extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;
    
    @Override
    public void initGui() {
        this.field_146445_a = 0;
        this.buttonList.clear();
        final int i = -16;
        final int j = 98;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));
        if (!this.mc2.isIntegratedServerRunning()) {
            this.buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
        final GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats", new Object[0])));
        guibutton.enabled = (this.mc2.isSingleplayer() && !this.mc2.getIntegratedServer().getPublic());
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc2.displayGuiScreen(new GuiOptions(this, this.mc2.gameSettings));
                break;
            }
            case 1: {
                final boolean flag = this.mc2.isIntegratedServerRunning();
                button.enabled = false;
                this.mc2.theWorld.sendQuittingDisconnectingPacket();
                this.mc2.loadWorld(null);
                if (flag) {
                    this.mc2.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                this.mc2.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                break;
            }
            case 4: {
                this.mc2.displayGuiScreen(null);
                this.mc2.setIngameFocus();
                break;
            }
            case 5: {
                this.mc2.displayGuiScreen(new GuiAchievements(this, this.mc2.thePlayer.getStatFileWriter()));
                break;
            }
            case 6: {
                this.mc2.displayGuiScreen(new GuiStats(this, this.mc2.thePlayer.getStatFileWriter()));
                break;
            }
            case 7: {
                this.mc2.displayGuiScreen(new GuiShareToLan(this));
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.field_146444_f;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
