// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenu extends GuiScreen
{
    private int saveStep;
    private int visibleTime;
    
    @Override
    public void initGui() {
        this.saveStep = 0;
        this.buttonList.clear();
        final int i = -16;
        final int j = 98;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 - 16, I18n.format("menu.returnToMenu", new Object[0])));
        if (!GuiIngameMenu.mc.isIntegratedServerRunning()) {
            this.buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 - 16, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 - 16, 98, 20, I18n.format("menu.options", new Object[0])));
        final GuiButton guibutton = this.addButton(new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 - 16, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
        guibutton.enabled = (GuiIngameMenu.mc.isSingleplayer() && !GuiIngameMenu.mc.getIntegratedServer().getPublic());
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 - 16, 98, 20, I18n.format("gui.advancements", new Object[0])));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 - 16, 98, 20, I18n.format("gui.stats", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                GuiIngameMenu.mc.displayGuiScreen(new GuiOptions(this, GuiIngameMenu.mc.gameSettings));
                break;
            }
            case 1: {
                final boolean flag = GuiIngameMenu.mc.isIntegratedServerRunning();
                final boolean flag2 = GuiIngameMenu.mc.isConnectedToRealms();
                button.enabled = false;
                GuiIngameMenu.mc.world.sendQuittingDisconnectingPacket();
                GuiIngameMenu.mc.loadWorld(null);
                if (flag) {
                    GuiIngameMenu.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                if (flag2) {
                    final RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                    break;
                }
                GuiIngameMenu.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                break;
            }
            case 4: {
                GuiIngameMenu.mc.displayGuiScreen(null);
                GuiIngameMenu.mc.setIngameFocus();
                break;
            }
            case 5: {
                final Minecraft mc = GuiIngameMenu.mc;
                final Minecraft mc2 = GuiIngameMenu.mc;
                mc.displayGuiScreen(new GuiScreenAdvancements(Minecraft.player.connection.getAdvancementManager()));
                break;
            }
            case 6: {
                final Minecraft mc3 = GuiIngameMenu.mc;
                final Minecraft mc4 = GuiIngameMenu.mc;
                mc3.displayGuiScreen(new GuiStats(this, Minecraft.player.getStatFileWriter()));
                break;
            }
            case 7: {
                GuiIngameMenu.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.visibleTime;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
