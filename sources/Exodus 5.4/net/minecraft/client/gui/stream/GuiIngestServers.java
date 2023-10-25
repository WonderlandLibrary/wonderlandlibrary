/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  tv.twitch.broadcast.IngestServer
 */
package net.minecraft.client.gui.stream;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.util.EnumChatFormatting;
import tv.twitch.broadcast.IngestServer;

public class GuiIngestServers
extends GuiScreen {
    private final GuiScreen field_152309_a;
    private ServerList field_152311_g;
    private String field_152310_f;

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_152311_g.handleMouseInput();
    }

    public GuiIngestServers(GuiScreen guiScreen) {
        this.field_152309_a = guiScreen;
    }

    @Override
    public void initGui() {
        this.field_152310_f = I18n.format("options.stream.ingest.title", new Object[0]);
        this.field_152311_g = new ServerList(this.mc);
        if (!this.mc.getTwitchStream().func_152908_z()) {
            this.mc.getTwitchStream().func_152909_x();
        }
        this.buttonList.add(new GuiButton(1, width / 2 - 155, height - 24 - 6, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(2, width / 2 + 5, height - 24 - 6, 150, 20, I18n.format("options.stream.ingest.reset", new Object[0])));
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.field_152311_g.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, this.field_152310_f, width / 2, 20, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void onGuiClosed() {
        if (this.mc.getTwitchStream().func_152908_z()) {
            this.mc.getTwitchStream().func_152932_y().func_153039_l();
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
                this.mc.displayGuiScreen(this.field_152309_a);
            } else {
                Minecraft.gameSettings.streamPreferredServer = "";
                Minecraft.gameSettings.saveOptions();
            }
        }
    }

    class ServerList
    extends GuiSlot {
        @Override
        protected int getScrollBarX() {
            return super.getScrollBarX() + 15;
        }

        @Override
        protected int getSize() {
            return this.mc.getTwitchStream().func_152925_v().length;
        }

        @Override
        protected boolean isSelected(int n) {
            return this.mc.getTwitchStream().func_152925_v()[n].serverUrl.equals(Minecraft.gameSettings.streamPreferredServer);
        }

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            IngestServer ingestServer = this.mc.getTwitchStream().func_152925_v()[n];
            String string = ingestServer.serverUrl.replaceAll("\\{stream_key\\}", "");
            String string2 = String.valueOf((int)ingestServer.bitrateKbps) + " kbps";
            String string3 = null;
            IngestServerTester ingestServerTester = this.mc.getTwitchStream().func_152932_y();
            if (ingestServerTester != null) {
                if (ingestServer == ingestServerTester.func_153040_c()) {
                    string = (Object)((Object)EnumChatFormatting.GREEN) + string;
                    string2 = String.valueOf((int)(ingestServerTester.func_153030_h() * 100.0f)) + "%";
                } else if (n < ingestServerTester.func_153028_p()) {
                    if (ingestServer.bitrateKbps == 0.0f) {
                        string2 = (Object)((Object)EnumChatFormatting.RED) + "Down!";
                    }
                } else {
                    string2 = (Object)((Object)EnumChatFormatting.OBFUSCATED) + "1234" + (Object)((Object)EnumChatFormatting.RESET) + " kbps";
                }
            } else if (ingestServer.bitrateKbps == 0.0f) {
                string2 = (Object)((Object)EnumChatFormatting.RED) + "Down!";
            }
            n2 -= 15;
            if (this.isSelected(n)) {
                string3 = (Object)((Object)EnumChatFormatting.BLUE) + "(Preferred)";
            } else if (ingestServer.defaultServer) {
                string3 = (Object)((Object)EnumChatFormatting.GREEN) + "(Default)";
            }
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, ingestServer.serverName, n2 + 2, n3 + 5, 0xFFFFFF);
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, string, n2 + 2, n3 + ((GuiIngestServers)GuiIngestServers.this).fontRendererObj.FONT_HEIGHT + 5 + 3, 0x303030);
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, string2, this.getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(string2), n3 + 5, 0x808080);
            if (string3 != null) {
                GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, string3, this.getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(string3), n3 + 5 + 3 + ((GuiIngestServers)GuiIngestServers.this).fontRendererObj.FONT_HEIGHT, 0x808080);
            }
        }

        public ServerList(Minecraft minecraft) {
            super(minecraft, width, height, 32, height - 35, (int)((double)Minecraft.fontRendererObj.FONT_HEIGHT * 3.5));
            this.setShowSelectionBox(false);
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
            Minecraft.gameSettings.streamPreferredServer = this.mc.getTwitchStream().func_152925_v()[n].serverUrl;
            Minecraft.gameSettings.saveOptions();
        }

        @Override
        protected void drawBackground() {
        }
    }
}

