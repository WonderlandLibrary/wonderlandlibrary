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
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {
   private final ServerData field_148301_e;
   private String field_148299_g;
   private final ResourceLocation field_148306_i;
   private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
   private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
   private final GuiMultiplayer field_148303_c;
   private DynamicTexture field_148305_h;
   private final Minecraft mc;
   private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
   private long field_148298_f;
   private static final Logger logger = LogManager.getLogger();

   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var5 <= 32) {
         if (var5 < 32 && var5 > 16 && this.func_178013_b()) {
            this.field_148303_c.selectServer(var1);
            this.field_148303_c.connectToSelected();
            return true;
         }

         if (var5 < 16 && var6 < 16 && this.field_148303_c.func_175392_a(this, var1)) {
            this.field_148303_c.func_175391_a(this, var1, GuiScreen.isShiftKeyDown());
            return true;
         }

         if (var5 < 16 && var6 > 16 && this.field_148303_c.func_175394_b(this, var1)) {
            this.field_148303_c.func_175393_b(this, var1, GuiScreen.isShiftKeyDown());
            return true;
         }
      }

      this.field_148303_c.selectServer(var1);
      if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
         this.field_148303_c.connectToSelected();
      }

      this.field_148298_f = Minecraft.getSystemTime();
      return false;
   }

   private boolean func_178013_b() {
      return true;
   }

   public void setSelected(int var1, int var2, int var3) {
   }

   protected void func_178012_a(int var1, int var2, ResourceLocation var3) {
      this.mc.getTextureManager().bindTexture(var3);
      GlStateManager.enableBlend();
      Gui.drawModalRectWithCustomSizedTexture((double)var1, (double)var2, 0.0F, 0.0F, 32.0D, 32.0D, 32.0D, 32.0D);
      GlStateManager.disableBlend();
   }

   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      if (!this.field_148301_e.field_78841_f) {
         this.field_148301_e.field_78841_f = true;
         this.field_148301_e.pingToServer = -2L;
         this.field_148301_e.serverMOTD = "";
         this.field_148301_e.populationInfo = "";
         field_148302_b.submit(new Runnable(this) {
            final ServerListEntryNormal this$0;

            public void run() {
               try {
                  ServerListEntryNormal.access$0(this.this$0).getOldServerPinger().ping(ServerListEntryNormal.access$1(this.this$0));
               } catch (UnknownHostException var2) {
                  ServerListEntryNormal.access$1(this.this$0).pingToServer = -1L;
                  ServerListEntryNormal.access$1(this.this$0).serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
               } catch (Exception var3) {
                  ServerListEntryNormal.access$1(this.this$0).pingToServer = -1L;
                  ServerListEntryNormal.access$1(this.this$0).serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
               }

            }

            {
               this.this$0 = var1;
            }
         });
      }

      boolean var9 = this.field_148301_e.version > 47;
      boolean var10 = this.field_148301_e.version < 47;
      boolean var11 = var9 || var10;
      Minecraft.fontRendererObj.drawString(this.field_148301_e.serverName, (double)(var2 + 32 + 3), (double)(var3 + 1), 16777215);
      List var12 = Minecraft.fontRendererObj.listFormattedStringToWidth(this.field_148301_e.serverMOTD, var4 - 32 - 2);

      for(int var13 = 0; var13 < Math.min(var12.size(), 2); ++var13) {
         Minecraft.fontRendererObj.drawString((String)var12.get(var13), (double)(var2 + 32 + 3), (double)(var3 + 12 + Minecraft.fontRendererObj.FONT_HEIGHT * var13), 8421504);
      }

      String var23 = var11 ? EnumChatFormatting.DARK_RED + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo;
      int var14 = Minecraft.fontRendererObj.getStringWidth(var23);
      Minecraft.fontRendererObj.drawString(var23, (double)(var2 + var4 - var14 - 15 - 2), (double)(var3 + 1), 8421504);
      byte var15 = 0;
      String var16 = null;
      int var17;
      String var18;
      if (var11) {
         var17 = 5;
         var18 = var9 ? "Client out of date!" : "Server out of date!";
         var16 = this.field_148301_e.playerList;
      } else if (this.field_148301_e.field_78841_f && this.field_148301_e.pingToServer != -2L) {
         if (this.field_148301_e.pingToServer < 0L) {
            var17 = 5;
         } else if (this.field_148301_e.pingToServer < 150L) {
            var17 = 0;
         } else if (this.field_148301_e.pingToServer < 300L) {
            var17 = 1;
         } else if (this.field_148301_e.pingToServer < 600L) {
            var17 = 2;
         } else if (this.field_148301_e.pingToServer < 1000L) {
            var17 = 3;
         } else {
            var17 = 4;
         }

         if (this.field_148301_e.pingToServer < 0L) {
            var18 = "(no connection)";
         } else {
            var18 = this.field_148301_e.pingToServer + "ms";
            var16 = this.field_148301_e.playerList;
         }
      } else {
         var15 = 1;
         var17 = (int)(Minecraft.getSystemTime() / 100L + (long)(var1 * 2) & 7L);
         if (var17 > 4) {
            var17 = 8 - var17;
         }

         var18 = "Pinging...";
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(Gui.icons);
      Gui.drawModalRectWithCustomSizedTexture((double)(var2 + var4 - 15), (double)var3, (float)(var15 * 10), (float)(176 + var17 * 8), 10.0D, 8.0D, 256.0D, 256.0D);
      if (this.field_148301_e.getBase64EncodedIconData() != null && !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g)) {
         this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
         this.prepareServerIcon();
         this.field_148303_c.getServerList().saveServerList();
      }

      if (this.field_148305_h != null) {
         this.func_178012_a(var2, var3, this.field_148306_i);
      } else {
         this.func_178012_a(var2, var3, UNKNOWN_SERVER);
      }

      int var19 = var6 - var2;
      int var20 = var7 - var3;
      if (var19 >= var4 - 15 && var19 <= var4 - 5 && var20 >= 0 && var20 <= 8) {
         this.field_148303_c.setHoveringText(var18);
      } else if (var19 >= var4 - var14 - 15 - 2 && var19 <= var4 - 15 - 2 && var20 >= 0 && var20 <= 8) {
         this.field_148303_c.setHoveringText(var16);
      }

      if (this.mc.gameSettings.touchscreen || var8) {
         this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
         Gui.drawRect((double)var2, (double)var3, (double)(var2 + 32), (double)(var3 + 32), -1601138544);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         int var21 = var6 - var2;
         int var22 = var7 - var3;
         if (this.func_178013_b()) {
            if (var21 < 32 && var21 > 16) {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 0.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            } else {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 0.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            }
         }

         if (this.field_148303_c.func_175392_a(this, var1)) {
            if (var21 < 16 && var22 < 16) {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 96.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            } else {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 96.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            }
         }

         if (this.field_148303_c.func_175394_b(this, var1)) {
            if (var21 < 16 && var22 > 16) {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 64.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            } else {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 64.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            }
         }
      }

   }

   static ServerData access$1(ServerListEntryNormal var0) {
      return var0.field_148301_e;
   }

   static GuiMultiplayer access$0(ServerListEntryNormal var0) {
      return var0.field_148303_c;
   }

   protected ServerListEntryNormal(GuiMultiplayer var1, ServerData var2) {
      this.field_148303_c = var1;
      this.field_148301_e = var2;
      this.mc = Minecraft.getMinecraft();
      this.field_148306_i = new ResourceLocation("servers/" + var2.serverIP + "/icon");
      this.field_148305_h = (DynamicTexture)this.mc.getTextureManager().getTexture(this.field_148306_i);
   }

   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   private void prepareServerIcon() {
      if (this.field_148301_e.getBase64EncodedIconData() == null) {
         this.mc.getTextureManager().deleteTexture(this.field_148306_i);
         this.field_148305_h = null;
      } else {
         ByteBuf var1 = Unpooled.copiedBuffer((CharSequence)this.field_148301_e.getBase64EncodedIconData(), Charsets.UTF_8);
         ByteBuf var2 = Base64.decode(var1);

         BufferedImage var3;
         try {
            var3 = TextureUtil.readBufferedImage(new ByteBufInputStream(var2));
            Validate.validState(var3.getWidth() == 64, "Must be 64 pixels wide");
            Validate.validState(var3.getHeight() == 64, "Must be 64 pixels high");
         } catch (Throwable var6) {
            logger.error("Invalid icon for server " + this.field_148301_e.serverName + " (" + this.field_148301_e.serverIP + ")", var6);
            this.field_148301_e.setBase64EncodedIconData((String)null);
            var1.release();
            var2.release();
            return;
         }

         var1.release();
         var2.release();
         if (this.field_148305_h == null) {
            this.field_148305_h = new DynamicTexture(var3.getWidth(), var3.getHeight());
            this.mc.getTextureManager().loadTexture(this.field_148306_i, this.field_148305_h);
         }

         var3.getRGB(0, 0, var3.getWidth(), var3.getHeight(), this.field_148305_h.getTextureData(), 0, var3.getWidth());
         this.field_148305_h.updateDynamicTexture();
      }

   }

   public ServerData getServerData() {
      return this.field_148301_e;
   }
}
