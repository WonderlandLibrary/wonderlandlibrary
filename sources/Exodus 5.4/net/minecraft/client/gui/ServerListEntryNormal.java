/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal
implements GuiListExtended.IGuiListEntry {
    private static final Logger logger = LogManager.getLogger();
    private final ServerData field_148301_e;
    private final ResourceLocation field_148306_i;
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private final Minecraft mc;
    private final GuiMultiplayer field_148303_c;
    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
    private String field_148299_g;
    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    private long field_148298_f;
    private DynamicTexture field_148305_h;

    @Override
    public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
        if (n5 <= 32) {
            if (n5 < 32 && n5 > 16 && this.func_178013_b()) {
                this.field_148303_c.selectServer(n);
                this.field_148303_c.connectToSelected();
                return true;
            }
            if (n5 < 16 && n6 < 16 && this.field_148303_c.func_175392_a(this, n)) {
                this.field_148303_c.func_175391_a(this, n, GuiScreen.isShiftKeyDown());
                return true;
            }
            if (n5 < 16 && n6 > 16 && this.field_148303_c.func_175394_b(this, n)) {
                this.field_148303_c.func_175393_b(this, n, GuiScreen.isShiftKeyDown());
                return true;
            }
        }
        this.field_148303_c.selectServer(n);
        if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
            this.field_148303_c.connectToSelected();
        }
        this.field_148298_f = Minecraft.getSystemTime();
        return false;
    }

    private void prepareServerIcon() {
        block4: {
            BufferedImage bufferedImage;
            block3: {
                if (this.field_148301_e.getBase64EncodedIconData() != null) break block3;
                this.mc.getTextureManager().deleteTexture(this.field_148306_i);
                this.field_148305_h = null;
                break block4;
            }
            ByteBuf byteBuf = Unpooled.copiedBuffer(this.field_148301_e.getBase64EncodedIconData(), Charsets.UTF_8);
            ByteBuf byteBuf2 = Base64.decode(byteBuf);
            try {
                bufferedImage = TextureUtil.readBufferedImage(new ByteBufInputStream(byteBuf2));
                Validate.validState((bufferedImage.getWidth() == 64 ? 1 : 0) != 0, (String)"Must be 64 pixels wide", (Object[])new Object[0]);
                Validate.validState((bufferedImage.getHeight() == 64 ? 1 : 0) != 0, (String)"Must be 64 pixels high", (Object[])new Object[0]);
            }
            catch (Throwable throwable) {
                logger.error("Invalid icon for server " + this.field_148301_e.serverName + " (" + this.field_148301_e.serverIP + ")", throwable);
                this.field_148301_e.setBase64EncodedIconData(null);
                byteBuf.release();
                byteBuf2.release();
                return;
            }
            byteBuf.release();
            byteBuf2.release();
            if (this.field_148305_h == null) {
                this.field_148305_h = new DynamicTexture(bufferedImage.getWidth(), bufferedImage.getHeight());
                this.mc.getTextureManager().loadTexture(this.field_148306_i, this.field_148305_h);
            }
            bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.field_148305_h.getTextureData(), 0, bufferedImage.getWidth());
            this.field_148305_h.updateDynamicTexture();
        }
    }

    @Override
    public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        String string;
        int n8;
        if (!this.field_148301_e.field_78841_f) {
            this.field_148301_e.field_78841_f = true;
            this.field_148301_e.pingToServer = -2L;
            this.field_148301_e.serverMOTD = "";
            this.field_148301_e.populationInfo = "";
            field_148302_b.submit(new Runnable(){

                @Override
                public void run() {
                    try {
                        ServerListEntryNormal.this.field_148303_c.getOldServerPinger().ping(ServerListEntryNormal.this.field_148301_e);
                    }
                    catch (UnknownHostException unknownHostException) {
                        ((ServerListEntryNormal)ServerListEntryNormal.this).field_148301_e.pingToServer = -1L;
                        ((ServerListEntryNormal)ServerListEntryNormal.this).field_148301_e.serverMOTD = (Object)((Object)EnumChatFormatting.DARK_RED) + "Can't resolve hostname";
                    }
                    catch (Exception exception) {
                        ((ServerListEntryNormal)ServerListEntryNormal.this).field_148301_e.pingToServer = -1L;
                        ((ServerListEntryNormal)ServerListEntryNormal.this).field_148301_e.serverMOTD = (Object)((Object)EnumChatFormatting.DARK_RED) + "Can't connect to server.";
                    }
                }
            });
        }
        boolean bl2 = this.field_148301_e.version > 47;
        boolean bl3 = this.field_148301_e.version < 47;
        boolean bl4 = bl2 || bl3;
        Minecraft.fontRendererObj.drawString(this.field_148301_e.serverName, n2 + 32 + 3, n3 + 1, 0xFFFFFF);
        List<String> list = Minecraft.fontRendererObj.listFormattedStringToWidth(this.field_148301_e.serverMOTD, n4 - 32 - 2);
        int n9 = 0;
        while (n9 < Math.min(list.size(), 2)) {
            Minecraft.fontRendererObj.drawString(list.get(n9), n2 + 32 + 3, n3 + 12 + Minecraft.fontRendererObj.FONT_HEIGHT * n9, 0x808080);
            ++n9;
        }
        String string2 = bl4 ? (Object)((Object)EnumChatFormatting.DARK_RED) + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo;
        int n10 = Minecraft.fontRendererObj.getStringWidth(string2);
        Minecraft.fontRendererObj.drawString(string2, n2 + n4 - n10 - 15 - 2, n3 + 1, 0x808080);
        int n11 = 0;
        String string3 = null;
        if (bl4) {
            n8 = 5;
            string = bl2 ? "Client out of date!" : "Server out of date!";
            string3 = this.field_148301_e.playerList;
        } else if (this.field_148301_e.field_78841_f && this.field_148301_e.pingToServer != -2L) {
            n8 = this.field_148301_e.pingToServer < 0L ? 5 : (this.field_148301_e.pingToServer < 150L ? 0 : (this.field_148301_e.pingToServer < 300L ? 1 : (this.field_148301_e.pingToServer < 600L ? 2 : (this.field_148301_e.pingToServer < 1000L ? 3 : 4))));
            if (this.field_148301_e.pingToServer < 0L) {
                string = "(no connection)";
            } else {
                string = String.valueOf(this.field_148301_e.pingToServer) + "ms";
                string3 = this.field_148301_e.playerList;
            }
        } else {
            n11 = 1;
            n8 = (int)(Minecraft.getSystemTime() / 100L + (long)(n * 2) & 7L);
            if (n8 > 4) {
                n8 = 8 - n8;
            }
            string = "Pinging...";
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture(n2 + n4 - 15, n3, n11 * 10, 176 + n8 * 8, 10, 8, 256.0f, 256.0f);
        if (this.field_148301_e.getBase64EncodedIconData() != null && !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g)) {
            this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.field_148303_c.getServerList().saveServerList();
        }
        if (this.field_148305_h != null) {
            this.func_178012_a(n2, n3, this.field_148306_i);
        } else {
            this.func_178012_a(n2, n3, UNKNOWN_SERVER);
        }
        int n12 = n6 - n2;
        int n13 = n7 - n3;
        if (n12 >= n4 - 15 && n12 <= n4 - 5 && n13 >= 0 && n13 <= 8) {
            this.field_148303_c.setHoveringText(string);
        } else if (n12 >= n4 - n10 - 15 - 2 && n12 <= n4 - 15 - 2 && n13 >= 0 && n13 <= 8) {
            this.field_148303_c.setHoveringText(string3);
        }
        if (Minecraft.gameSettings.touchscreen || bl) {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(n2, n3, n2 + 32, n3 + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            int n14 = n6 - n2;
            int n15 = n7 - n3;
            if (this.func_178013_b()) {
                if (n14 < 32 && n14 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            if (this.field_148303_c.func_175392_a(this, n)) {
                if (n14 < 16 && n15 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 96.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 96.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            if (this.field_148303_c.func_175394_b(this, n)) {
                if (n14 < 16 && n15 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    private boolean func_178013_b() {
        return true;
    }

    public ServerData getServerData() {
        return this.field_148301_e;
    }

    protected ServerListEntryNormal(GuiMultiplayer guiMultiplayer, ServerData serverData) {
        this.field_148303_c = guiMultiplayer;
        this.field_148301_e = serverData;
        this.mc = Minecraft.getMinecraft();
        this.field_148306_i = new ResourceLocation("servers/" + serverData.serverIP + "/icon");
        this.field_148305_h = (DynamicTexture)this.mc.getTextureManager().getTexture(this.field_148306_i);
    }

    protected void func_178012_a(int n, int n2, ResourceLocation resourceLocation) {
        this.mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(n, n2, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        GlStateManager.disableBlend();
    }

    @Override
    public void setSelected(int n, int n2, int n3) {
    }
}

