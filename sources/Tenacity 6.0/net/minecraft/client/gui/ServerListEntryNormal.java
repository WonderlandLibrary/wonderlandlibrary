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
import com.google.common.base.Charsets;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import java.net.UnknownHostException;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry
{
    private static final Logger logger;
    private static final ThreadPoolExecutor field_148302_b;
    private static final ResourceLocation UNKNOWN_SERVER;
    private static final ResourceLocation SERVER_SELECTION_BUTTONS;
    private final GuiMultiplayer owner;
    private final Minecraft mc;
    private final ServerData server;
    private final ResourceLocation serverIcon;
    private String field_148299_g;
    private DynamicTexture field_148305_h;
    private long field_148298_f;
    
    protected ServerListEntryNormal(final GuiMultiplayer p_i45048_1_, final ServerData serverIn) {
        this.owner = p_i45048_1_;
        this.server = serverIn;
        this.mc = Minecraft.getMinecraft();
        this.serverIcon = new ResourceLocation("servers/" + serverIn.serverIP + "/icon");
        this.field_148305_h = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        if (!this.server.field_78841_f) {
            this.server.field_78841_f = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            ServerListEntryNormal.field_148302_b.submit(() -> {
                try {
                    this.owner.getOldServerPinger().ping(this.server);
                }
                catch (UnknownHostException var2) {
                    this.server.pingToServer = -1L;
                    this.server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
                }
                catch (Exception var3) {
                    this.server.pingToServer = -1L;
                    this.server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
                }
                return;
            });
        }
        if (this.server.serverIP.equals("smp.norules.wtf") || this.server.serverIP.equals("play.norules.wtf")) {
            RenderUtil.scaleStart((float)(x - 20), (float)y, 2.0f);
            this.mc.fontRendererObj.drawString("§6\u2605", (float)(x - 20), (float)y, -1);
            RenderUtil.scaleEnd();
        }
        if (this.server.serverIP.equals("mc.hypixel.net")) {
            RenderUtil.scaleStart((float)(x - 20), (float)y, 2.0f);
            this.mc.fontRendererObj.drawString("§6\u2605", (float)(x - 20), (float)y, -1);
            RenderUtil.scaleEnd();
        }
        final boolean flag = this.server.version > 47;
        final boolean flag2 = this.server.version < 47;
        final boolean flag3 = flag || flag2;
        this.mc.fontRendererObj.drawString(this.server.serverName, (float)(x + 32 + 3), (float)(y + 1), 16777215);
        final List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(this.server.serverMOTD, listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            this.mc.fontRendererObj.drawString(list.get(i), (float)(x + 32 + 3), (float)(y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i), 8421504);
        }
        final String s2 = flag3 ? (EnumChatFormatting.DARK_RED + this.server.gameVersion) : this.server.populationInfo;
        final int j = (int)this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, (float)(x + listWidth - j - 15 - 2), (float)(y + 1), 8421504);
        int k = 0;
        String s3 = null;
        int l;
        String s4;
        if (flag3) {
            l = 5;
            s4 = (flag ? "Client out of date!" : "Server out of date!");
            s3 = this.server.playerList;
        }
        else if (this.server.field_78841_f && this.server.pingToServer != -2L) {
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
                s4 = "(no connection)";
            }
            else {
                s4 = this.server.pingToServer + "ms";
                s3 = this.server.playerList;
            }
        }
        else {
            k = 1;
            l = (int)(Minecraft.getSystemTime() / 100L + slotIndex * 2L & 0x7L);
            if (l > 4) {
                l = 8 - l;
            }
            s4 = "Pinging...";
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture((float)(x + listWidth - 15), (float)y, (float)(k * 10), (float)(176 + l * 8), 10.0f, 8.0f, 256.0f, 256.0f);
        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.field_148299_g)) {
            this.field_148299_g = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }
        if (this.field_148305_h != null) {
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
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int k2 = mouseX - x;
            final int l2 = mouseY - y;
            if (this.func_178013_b()) {
                if (k2 < 32 && k2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            if (this.owner.func_175392_a(this, slotIndex)) {
                if (k2 < 16 && l2 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 96.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 96.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            if (this.owner.func_175394_b(this, slotIndex)) {
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
    
    private boolean func_178013_b() {
        return true;
    }
    
    private void prepareServerIcon() {
        if (this.server.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.field_148305_h = null;
        }
        else {
            final ByteBuf bytebuf = Unpooled.copiedBuffer((CharSequence)this.server.getBase64EncodedIconData(), Charsets.UTF_8);
            final ByteBuf bytebuf2 = Base64.decode(bytebuf);
            BufferedImage bufferedimage = null;
            Label_0219: {
                try {
                    bufferedimage = TextureUtil.readBufferedImage((InputStream)new ByteBufInputStream(bytebuf2));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break Label_0219;
                }
                catch (Throwable throwable) {
                    ServerListEntryNormal.logger.error("Invalid icon for server " + this.server.serverName + " (" + this.server.serverIP + ")", throwable);
                    this.server.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    bytebuf2.release();
                }
                return;
            }
            if (this.field_148305_h == null) {
                this.field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.serverIcon, this.field_148305_h);
            }
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
            this.field_148305_h.updateDynamicTexture();
        }
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        if (p_148278_5_ <= 32) {
            if (p_148278_5_ < 32 && p_148278_5_ > 16 && this.func_178013_b()) {
                this.owner.selectServer(slotIndex);
                this.owner.connectToSelected();
                return true;
            }
            if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.owner.func_175392_a(this, slotIndex)) {
                this.owner.func_175391_a(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
            if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.owner.func_175394_b(this, slotIndex)) {
                this.owner.func_175393_b(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
        }
        this.owner.selectServer(slotIndex);
        if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
            this.owner.connectToSelected();
        }
        this.field_148298_f = Minecraft.getSystemTime();
        return false;
    }
    
    @Override
    public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public ServerData getServerData() {
        return this.server;
    }
    
    static {
        logger = LogManager.getLogger();
        field_148302_b = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
        UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
        SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    }
}
