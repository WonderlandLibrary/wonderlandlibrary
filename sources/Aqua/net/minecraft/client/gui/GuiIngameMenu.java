package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class GuiIngameMenu
extends GuiScreen {
    private int field_146445_a;
    private int field_146444_f;

    public void initGui() {
        this.field_146445_a = 0;
        this.buttonList.clear();
        int i = -16;
        int j = 98;
        this.buttonList.add((Object)new GuiButton(1, width / 2 - 100, height / 4 + 120 + i, I18n.format((String)"menu.returnToMenu", (Object[])new Object[0])));
        if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton)this.buttonList.get((int)0)).displayString = I18n.format((String)"menu.disconnect", (Object[])new Object[0]);
        }
        this.buttonList.add((Object)new GuiButton(4, width / 2 - 100, height / 4 + 24 + i, I18n.format((String)"menu.returnToGame", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(0, width / 2 - 100, height / 4 + 96 + i, 98, 20, I18n.format((String)"menu.options", (Object[])new Object[0])));
        GuiButton guibutton = new GuiButton(7, width / 2 + 2, height / 4 + 96 + i, 98, 20, I18n.format((String)"menu.shareToLan", (Object[])new Object[0]));
        this.buttonList.add((Object)guibutton);
        this.buttonList.add((Object)new GuiButton(5, width / 2 - 100, height / 4 + 48 + i, 98, 20, I18n.format((String)"gui.achievements", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(6, width / 2 + 2, height / 4 + 48 + i, 98, 20, I18n.format((String)"gui.stats", (Object[])new Object[0])));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
                break;
            }
            case 1: {
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                if (flag) {
                    this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
                    break;
                }
                if (flag1) {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms((GuiScreen)new GuiMainMenu());
                    break;
                }
                this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
            }
            default: {
                break;
            }
            case 4: {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                break;
            }
            case 5: {
                this.mc.displayGuiScreen((GuiScreen)new GuiAchievements((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen((GuiScreen)new GuiStats((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
                break;
            }
            case 7: {
                this.mc.displayGuiScreen((GuiScreen)new GuiShareToLan((GuiScreen)this));
            }
        }
    }

    public void updateScreen() {
        super.updateScreen();
        ++this.field_146444_f;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"menu.game", (Object[])new Object[0]), width / 2, 40, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
