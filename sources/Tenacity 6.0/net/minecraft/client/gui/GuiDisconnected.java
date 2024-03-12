// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import dev.tenacity.module.impl.render.NotificationsMod;
import dev.tenacity.module.impl.render.Statistics;
import net.minecraft.client.multiplayer.GuiConnecting;
import dev.tenacity.ui.mainmenu.CustomMainMenu;
import dev.tenacity.utils.server.ServerUtils;
import java.io.IOException;
import dev.tenacity.ui.altmanager.helpers.Alt;
import dev.tenacity.utils.server.ban.BanUtils;
import dev.tenacity.Tenacity;
import net.minecraft.client.resources.I18n;
import java.util.List;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private final String reason;
    private final IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    
    public GuiDisconnected(final GuiScreen screen, final String reasonLocalizationKey, final IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
        final Alt activeAlt = Tenacity.INSTANCE.getAltManager().currentSessionAlt;
        if (activeAlt != null) {
            BanUtils.processDisconnect(activeAlt, chatComp);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 25, 99, 20, "Alt Manager"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 1, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 25, 99, 20, "Reconnect"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc2.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 1) {
            this.mc2.displayGuiScreen(Tenacity.INSTANCE.getAltManager());
        }
        else if (button.id == 2 && ServerUtils.lastServer != null) {
            this.mc2.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new CustomMainMenu()), this.mc2, ServerUtils.lastServer));
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            for (final String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        int offset = 60;
        if (this.mc2.getSession() != null && this.mc2.getSession().getUsername() != null) {
            this.drawCenteredString(this.fontRendererObj, "Username: §7" + this.mc2.getSession().getUsername(), this.width / 2, i + offset, -1842205);
            offset += 10;
        }
        final String serverIP = (ServerUtils.lastServer != null && ServerUtils.lastServer.serverIP != null) ? ServerUtils.lastServer.serverIP : null;
        if (serverIP != null) {
            this.drawCenteredString(this.fontRendererObj, "Server: §7" + serverIP, this.width / 2, i + offset, -1842205);
            offset += 10;
        }
        if (Statistics.getTimeDiff() > 2000L) {
            final int[] playTime = Statistics.getPlayTime();
            String str = playTime[2] + "s";
            if (playTime[1] > 0) {
                str = playTime[1] + "m " + str;
            }
            if (playTime[0] > 0) {
                str = playTime[0] + "h " + str;
            }
            this.drawCenteredString(this.fontRendererObj, "Play time: §7" + str, this.width / 2, i + offset, -1842205);
        }
        Tenacity.INSTANCE.getModuleCollection().getModule(NotificationsMod.class).render();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
