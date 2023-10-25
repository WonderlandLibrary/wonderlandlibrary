package me.rhys.client.ui.alt.element;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.Manager;
import me.rhys.client.ui.alt.AltUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;

public class AltServerButton extends Button {
  private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
  
  private static final ThreadPoolExecutor THREAD_EXECUTOR = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
  
  private static final int ICON_SIZE = 32;
  
  private final ServerData serverData;
  
  private final ResourceLocation resourceLocation;
  
  private DynamicTexture dynamicTexture;
  
  private String imgData;
  
  public ServerData getServerData() {
    return this.serverData;
  }
  
  public ResourceLocation getResourceLocation() {
    return this.resourceLocation;
  }
  
  public DynamicTexture getDynamicTexture() {
    return this.dynamicTexture;
  }
  
  public String getImgData() {
    return this.imgData;
  }
  
  public AltServerButton(ServerData serverData, Vec2f offset, int width, int height) {
    super(serverData.serverName, offset, width, height);
    this.serverData = serverData;
    this.resourceLocation = new ResourceLocation("servers/" + serverData.serverIP + "/icon");
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    int l;
    String s1;
    UIScreen screen = getScreen();
    if (this.background == ColorUtil.Colors.TRANSPARENT.getColor())
      RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.buttonColors.background); 
    if (!this.serverData.field_78841_f) {
      this.serverData.field_78841_f = true;
      this.serverData.pingToServer = -2L;
      this.serverData.serverMOTD = "";
      this.serverData.populationInfo = "";
      THREAD_EXECUTOR.submit(() -> {
            try {
              AltUI.getServerPinger().ping(this.serverData);
            } catch (UnknownHostException var2) {
              this.serverData.pingToServer = -1L;
              this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
            } catch (Exception var3) {
              this.serverData.pingToServer = -1L;
              this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
            } 
          });
    } 
    FontUtil.drawString(this.label, this.pos.clone().add(37, 3), screen.theme.buttonColors.text);
    boolean bigFlag = (this.serverData.version > 47);
    boolean smallFlag = (this.serverData.version < 47);
    boolean flag = (bigFlag || smallFlag);
    String statusDisplay = flag ? (EnumChatFormatting.DARK_RED + this.serverData.gameVersion) : this.serverData.populationInfo;
    int j = (int)FontUtil.getStringWidth(statusDisplay);
    FontUtil.drawString(statusDisplay, (this.pos.clone().add(this.width, 0)).x - j - 15.0F - 2.0F, this.pos.y + 3.0F, 8421504);
    int k = 0;
    String s = null;
    if (flag) {
      l = 5;
      s1 = bigFlag ? "Client out of date!" : "Server out of date!";
      s = this.serverData.playerList;
    } else if (this.serverData.field_78841_f && this.serverData.pingToServer != -2L) {
      if (this.serverData.pingToServer < 0L) {
        l = 5;
      } else if (this.serverData.pingToServer < 150L) {
        l = 0;
      } else if (this.serverData.pingToServer < 300L) {
        l = 1;
      } else if (this.serverData.pingToServer < 600L) {
        l = 2;
      } else if (this.serverData.pingToServer < 1000L) {
        l = 3;
      } else {
        l = 4;
      } 
      if (this.serverData.pingToServer < 0L) {
        s1 = "(no connection)";
      } else {
        s1 = this.serverData.pingToServer + "ms";
        s = this.serverData.playerList;
      } 
    } else {
      k = 1;
      l = (int)(Minecraft.getSystemTime() / 100L + 4L & 0x7L);
      if (l > 4)
        l = 8 - l; 
      s1 = "Pinging...";
    } 
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
    Gui.drawModalRectWithCustomSizedTexture((int)this.pos.x + this.width - 15, (int)this.pos.y + 1, (k * 10), (176 + l * 8), 10, 8, 256.0F, 256.0F);
    if (this.serverData.serverMOTD != null) {
      (Minecraft.getMinecraft()).fontRendererObj.drawSplitString(this.serverData.serverMOTD, (int)(this.pos.clone().add(37.0F, 4.0F + FontUtil.getFontHeight())).x, (int)(this.pos.clone().add(37.0F, 4.0F + FontUtil.getFontHeight())).y, this.width - 37, -1);
    } else {
      FontUtil.drawString("...", this.pos.clone().add(37.0F, 4.0F + FontUtil.getFontHeight()), -1);
    } 
    GlStateManager.pushMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    if (this.serverData.getBase64EncodedIconData() != null && !this.serverData.getBase64EncodedIconData().equals(this.imgData)) {
      this.imgData = this.serverData.getBase64EncodedIconData();
      prepareServerIcon();
    } 
    if (this.dynamicTexture != null) {
      drawServerIcon(this.pos, this.resourceLocation);
    } else {
      drawServerIcon(this.pos, UNKNOWN_SERVER);
    } 
    int i1 = (int)(mouse.x - this.pos.x);
    int j1 = (int)(mouse.y - this.pos.y);
    if (i1 >= this.width - 15 && i1 <= this.width - 5 && j1 >= 0 && j1 <= 8) {
      Manager.UI.ALT.setHoveringText(s1);
    } else if (i1 >= this.width - j - 15 - 2 && i1 <= this.width - 15 - 2 && j1 >= 0 && j1 <= 8) {
      Manager.UI.ALT.setHoveringText(s);
    } 
    GlStateManager.popMatrix();
  }
  
  private void prepareServerIcon() {
    Minecraft mc = Minecraft.getMinecraft();
    if (this.serverData.getBase64EncodedIconData() == null) {
      mc.getTextureManager().deleteTexture(this.resourceLocation);
      this.dynamicTexture = null;
    } else {
      BufferedImage bufferedimage;
      ByteBuf byteBuf0 = Unpooled.copiedBuffer(this.serverData.getBase64EncodedIconData(), Charsets.UTF_8);
      ByteBuf byteBuf1 = Base64.decode(byteBuf0);
      try {
        bufferedimage = TextureUtil.readBufferedImage((InputStream)new ByteBufInputStream(byteBuf1));
        Validate.validState((bufferedimage.getWidth() == 64), "Must be 64 pixels wide", new Object[0]);
        Validate.validState((bufferedimage.getHeight() == 64), "Must be 64 pixels high", new Object[0]);
      } catch (Throwable throwable) {
        this.serverData.setBase64EncodedIconData((String)null);
      } finally {
        byteBuf0.release();
        byteBuf1.release();
      } 
      if (this.dynamicTexture == null) {
        this.dynamicTexture = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
        mc.getTextureManager().loadTexture(this.resourceLocation, (ITextureObject)this.dynamicTexture);
      } 
      bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.dynamicTexture.getTextureData(), 0, bufferedimage.getWidth());
      this.dynamicTexture.updateDynamicTexture();
    } 
  }
  
  protected void drawServerIcon(Vec2f pos, ResourceLocation resourceLocation) {
    Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    GlStateManager.enableBlend();
    Gui.drawModalRectWithCustomSizedTexture((int)pos.x, (int)pos.y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
    GlStateManager.disableBlend();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\alt\element\AltServerButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */