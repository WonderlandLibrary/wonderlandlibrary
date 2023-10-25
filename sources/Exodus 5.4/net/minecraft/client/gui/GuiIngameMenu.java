/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class GuiIngameMenu
extends GuiScreen {
    private int field_146444_f;
    private int field_146445_a;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), width / 2, 40, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        this.field_146445_a = 0;
        this.buttonList.clear();
        int n = -16;
        int n2 = 98;
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + n, I18n.format("menu.returnToMenu", new Object[0])));
        if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton)this.buttonList.get((int)0)).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + n, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + n, 98, 20, I18n.format("menu.options", new Object[0])));
        GuiButton guiButton = new GuiButton(7, width / 2 + 2, height / 4 + 96 + n, 98, 20, I18n.format("menu.shareToLan", new Object[0]));
        this.buttonList.add(guiButton);
        this.buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + n, 98, 20, I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + n, 98, 20, I18n.format("gui.stats", new Object[0])));
        guiButton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        switch (guiButton.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiOptions(this, Minecraft.gameSettings));
                break;
            }
            case 1: {
                boolean bl = this.mc.isIntegratedServerRunning();
                boolean bl2 = this.mc.func_181540_al();
                guiButton.enabled = false;
                Minecraft.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                if (bl) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                if (bl2) {
                    RealmsBridge realmsBridge = new RealmsBridge();
                    realmsBridge.switchToRealms(new GuiMainMenu());
                    break;
                }
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
            default: {
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            }
            case 5: {
                this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
                break;
            }
            case 7: {
                this.mc.displayGuiScreen(new GuiShareToLan(this));
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.field_146444_f;
    }
}

