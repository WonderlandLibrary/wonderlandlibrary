// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import java.awt.image.BufferedImage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.apache.commons.lang3.Validate;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import java.net.UnknownHostException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry
{
    private static final Logger LOGGER;
    private static final ThreadPoolExecutor EXECUTOR;
    private static final ResourceLocation UNKNOWN_SERVER;
    private static final ResourceLocation SERVER_SELECTION_BUTTONS;
    private final GuiMultiplayer owner;
    private final Minecraft mc;
    private final ServerData server;
    private final ResourceLocation serverIcon;
    private String lastIconB64;
    private DynamicTexture icon;
    private long lastClickTime;
    
    protected ServerListEntryNormal(final GuiMultiplayer ownerIn, final ServerData serverIn) {
        this.owner = ownerIn;
        this.server = serverIn;
        this.mc = Minecraft.getMinecraft();
        this.serverIcon = new ResourceLocation("servers/" + serverIn.serverIP + "/icon");
        this.icon = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
        if (!this.server.pinged) {
            this.server.pinged = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            ServerListEntryNormal.EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
                    }
                    catch (UnknownHostException var2) {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_resolve", new Object[0]);
                    }
                    catch (Exception var3) {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect", new Object[0]);
                    }
                }
            });
        }
        final boolean flag = this.server.version > 340;
        final boolean flag2 = this.server.version < 340;
        final boolean flag3 = flag || flag2;
        this.mc.fontRenderer.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
        final List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(this.server.serverMOTD, listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            this.mc.fontRenderer.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRenderer.FONT_HEIGHT * i, 8421504);
        }
        final String s2 = flag3 ? (TextFormatting.DARK_RED + this.server.gameVersion) : this.server.populationInfo;
        final int j = this.mc.fontRenderer.getStringWidth(s2);
        this.mc.fontRenderer.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
        int k = 0;
        String s3 = null;
        int l;
        String s4;
        if (flag3) {
            l = 5;
            s4 = I18n.format(flag ? "multiplayer.status.client_out_of_date" : "multiplayer.status.server_out_of_date", new Object[0]);
            s3 = this.server.playerList;
        }
        else if (this.server.pinged && this.server.pingToServer != -2L) {
            if (this.server.pingToServer < 0L) {
                l = 5;
            }
            else if (this.server.pingToServer < 150L) {
                l = 0;
            }
            else if (this.server.pingToServer < 300L) {
                l = 1;
            }
            else if (this.server.pingToServer < 600L) {
                l = 2;
            }
            else if (this.server.pingToServer < 1000L) {
                l = 3;
            }
            else {
                l = 4;
            }
            if (this.server.pingToServer < 0L) {
                s4 = I18n.format("multiplayer.status.no_connection", new Object[0]);
            }
            else {
                s4 = this.server.pingToServer + "ms";
                s3 = this.server.playerList;
            }
        }
        else {
            k = 1;
            l = (int)(Minecraft.getSystemTime() / 100L + slotIndex * 2 & 0x7L);
            if (l > 4) {
                l = 8 - l;
            }
            s4 = I18n.format("multiplayer.status.pinging", new Object[0]);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        Gui.drawModalRectWithCustomSizedTexture((float)(x + listWidth - 15), (float)y, (float)(k * 10), (float)(176 + l * 8), 10.0f, 8.0f, 256.0f, 256.0f);
        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64)) {
            this.lastIconB64 = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }
        if (this.icon != null) {
            this.drawTextureAt(x, y, this.serverIcon);
        }
        else {
            this.drawTextureAt(x, y, ServerListEntryNormal.UNKNOWN_SERVER);
        }
        final int i2 = mouseX - x;
        final int j2 = mouseY - y;
        if (i2 >= listWidth - 15 && i2 <= listWidth - 5 && j2 >= 0 && j2 <= 8) {
            this.owner.setHoveringText(s4);
        }
        else if (i2 >= listWidth - j - 15 - 2 && i2 <= listWidth - 15 - 2 && j2 >= 0 && j2 <= 8) {
            this.owner.setHoveringText(s3);
        }
        if (this.mc.gameSettings.touchscreen || isSelected) {
            this.mc.getTextureManager().bindTexture(ServerListEntryNormal.SERVER_SELECTION_BUTTONS);
            Gui.drawRect((float)x, (float)y, (float)(x + 32), (float)(y + 32), -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int k2 = mouseX - x;
            final int l2 = mouseY - y;
            if (this.canJoin()) {
                if (k2 < 32 && k2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            if (this.owner.canMoveUp(this, slotIndex)) {
                if (k2 < 16 && l2 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 96.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 96.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            if (this.owner.canMoveDown(this, slotIndex)) {
                if (k2 < 16 && l2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 64.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 64.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
        }
    }
    
    protected void drawTextureAt(final int p_178012_1_, final int p_178012_2_, final ResourceLocation p_178012_3_) {
        this.mc.getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((float)p_178012_1_, (float)p_178012_2_, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
        GlStateManager.disableBlend();
    }
    
    private boolean canJoin() {
        return true;
    }
    
    private void prepareServerIcon() {
        if (this.server.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.icon = null;
        }
        else {
            final ByteBuf bytebuf = Unpooled.copiedBuffer((CharSequence)this.server.getBase64EncodedIconData(), StandardCharsets.UTF_8);
            ByteBuf bytebuf2 = null;
            BufferedImage bufferedimage = null;
            Label_0204: {
                try {
                    bytebuf2 = Base64.decode(bytebuf);
                    bufferedimage = TextureUtil.readBufferedImage((InputStream)new ByteBufInputStream(bytebuf2));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break Label_0204;
                }
                catch (Throwable throwable) {
                    ServerListEntryNormal.LOGGER.error("Invalid icon for server {} ({})", (Object)this.server.serverName, (Object)this.server.serverIP, (Object)throwable);
                    this.server.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    if (bytebuf2 != null) {
                        bytebuf2.release();
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
    public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
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
    public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public ServerData getServerData() {
        return this.server;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EXECUTOR = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
        UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
        SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    }
}
