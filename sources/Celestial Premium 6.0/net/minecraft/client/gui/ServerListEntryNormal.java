/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.ByteBufInputStream
 *  io.netty.buffer.Unpooled
 *  io.netty.handler.codec.base64.Base64
 */
package net.minecraft.client.gui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal
implements GuiListExtended.IGuiListEntry {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    private final GuiMultiplayer owner;
    private final Minecraft mc;
    private final ServerData server;
    private final ResourceLocation serverIcon;
    private String lastIconB64;
    private DynamicTexture icon;
    private long lastClickTime;

    protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData serverIn) {
        this.owner = p_i45048_1_;
        this.server = serverIn;
        this.mc = Minecraft.getMinecraft();
        this.serverIcon = new ResourceLocation("servers/" + serverIn.serverIP + "/icon");
        this.icon = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
    }

    @Override
    public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
        String s1;
        int l;
        if (!this.server.pinged) {
            this.server.pinged = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            EXECUTOR.submit(new Runnable(){

                @Override
                public void run() {
                    try {
                        ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
                    }
                    catch (UnknownHostException var2) {
                        ((ServerListEntryNormal)ServerListEntryNormal.this).server.pingToServer = -1L;
                        ((ServerListEntryNormal)ServerListEntryNormal.this).server.serverMOTD = (Object)((Object)TextFormatting.DARK_RED) + I18n.format("multiplayer.status.cannot_resolve", new Object[0]);
                    }
                    catch (Exception var3) {
                        ((ServerListEntryNormal)ServerListEntryNormal.this).server.pingToServer = -1L;
                        ((ServerListEntryNormal)ServerListEntryNormal.this).server.serverMOTD = (Object)((Object)TextFormatting.DARK_RED) + I18n.format("multiplayer.status.cannot_connect", new Object[0]);
                    }
                }
            });
        }
        boolean flag = this.server.version > 340;
        boolean flag1 = this.server.version < 340;
        boolean flag2 = flag || flag1;
        this.mc.fontRendererObj.drawString(this.server.serverName, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 0xFFFFFF);
        List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(this.server.serverMOTD, p_192634_4_ - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            this.mc.fontRendererObj.drawString(list.get(i), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 0x808080);
        }
        String s2 = flag2 ? (Object)((Object)TextFormatting.DARK_RED) + this.server.gameVersion : this.server.populationInfo;
        int j = this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, p_192634_2_ + p_192634_4_ - j - 15 - 2, p_192634_3_ + 1, 0x808080);
        int k = 0;
        String s = null;
        if (flag2) {
            l = 5;
            s1 = I18n.format(flag ? "multiplayer.status.client_out_of_date" : "multiplayer.status.server_out_of_date", new Object[0]);
            s = this.server.playerList;
        } else if (this.server.pinged && this.server.pingToServer != -2L) {
            l = this.server.pingToServer < 0L ? 5 : (this.server.pingToServer < 150L ? 0 : (this.server.pingToServer < 300L ? 1 : (this.server.pingToServer < 600L ? 2 : (this.server.pingToServer < 1000L ? 3 : 4))));
            if (this.server.pingToServer < 0L) {
                s1 = I18n.format("multiplayer.status.no_connection", new Object[0]);
            } else {
                s1 = this.server.pingToServer + "ms";
                s = this.server.playerList;
            }
        } else {
            k = 1;
            l = (int)(Minecraft.getSystemTime() / 100L + (long)(p_192634_1_ * 2) & 7L);
            if (l > 4) {
                l = 8 - l;
            }
            s1 = I18n.format("multiplayer.status.pinging", new Object[0]);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_ + p_192634_4_ - 15, p_192634_3_, k * 10, 176 + l * 8, 10.0f, 8.0f, 256.0f, 256.0f);
        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64)) {
            this.lastIconB64 = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }
        if (this.icon != null) {
            this.drawTextureAt(p_192634_2_, p_192634_3_, this.serverIcon);
        } else {
            this.drawTextureAt(p_192634_2_, p_192634_3_, UNKNOWN_SERVER);
        }
        int i1 = p_192634_6_ - p_192634_2_;
        int j1 = p_192634_7_ - p_192634_3_;
        if (i1 >= p_192634_4_ - 15 && i1 <= p_192634_4_ - 5 && j1 >= 0 && j1 <= 8) {
            this.owner.setHoveringText(s1);
        } else if (i1 >= p_192634_4_ - j - 15 - 2 && i1 <= p_192634_4_ - 15 - 2 && j1 >= 0 && j1 <= 8) {
            this.owner.setHoveringText(s);
        }
        if (this.mc.gameSettings.touchscreen || p_192634_8_) {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            int k1 = p_192634_6_ - p_192634_2_;
            int l1 = p_192634_7_ - p_192634_3_;
            if (this.canJoin()) {
                if (k1 < 32 && k1 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            if (this.owner.canMoveUp(this, p_192634_1_)) {
                if (k1 < 16 && l1 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            if (this.owner.canMoveDown(this, p_192634_1_)) {
                if (k1 < 16 && l1 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
        }
    }

    protected void drawTextureAt(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_) {
        this.mc.getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
        GlStateManager.disableBlend();
    }

    private boolean canJoin() {
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void prepareServerIcon() {
        if (this.server.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.icon = null;
        } else {
            BufferedImage bufferedimage;
            block9: {
                ByteBuf bytebuf = Unpooled.copiedBuffer((CharSequence)this.server.getBase64EncodedIconData(), (Charset)StandardCharsets.UTF_8);
                ByteBuf bytebuf1 = null;
                try {
                    bytebuf1 = Base64.decode((ByteBuf)bytebuf);
                    bufferedimage = TextureUtil.readBufferedImage((InputStream)new ByteBufInputStream(bytebuf1));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break block9;
                }
                catch (Throwable throwable) {
                    LOGGER.error("Invalid icon for server {} ({})", this.server.serverName, this.server.serverIP, throwable);
                    this.server.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    if (bytebuf1 != null) {
                        bytebuf1.release();
                    }
                }
                return;
            }
            if (this.icon == null) {
                this.icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.serverIcon, this.icon);
            }
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.icon.getTextureData(), 0, bufferedimage.getWidth());
            this.icon.updateDynamicTexture();
        }
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        if (relativeX <= 32) {
            if (relativeX < 32 && relativeX > 16 && this.canJoin()) {
                this.owner.selectServer(slotIndex);
                this.owner.connectToSelected();
                return true;
            }
            if (relativeX < 16 && relativeY < 16 && this.owner.canMoveUp(this, slotIndex)) {
                this.owner.moveServerUp(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
            if (relativeX < 16 && relativeY > 16 && this.owner.canMoveDown(this, slotIndex)) {
                this.owner.moveServerDown(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
        }
        this.owner.selectServer(slotIndex);
        if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.owner.connectToSelected();
        }
        this.lastClickTime = Minecraft.getSystemTime();
        return false;
    }

    @Override
    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    public ServerData getServerData() {
        return this.server;
    }
}

