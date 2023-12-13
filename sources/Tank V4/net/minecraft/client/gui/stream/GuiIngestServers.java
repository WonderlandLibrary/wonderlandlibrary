package net.minecraft.client.gui.stream;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.util.EnumChatFormatting;
import tv.twitch.broadcast.IngestServer;

public class GuiIngestServers extends GuiScreen {
   private final GuiScreen field_152309_a;
   private String field_152310_f;
   private GuiIngestServers.ServerList field_152311_g;

   public void onGuiClosed() {
      if (this.mc.getTwitchStream().func_152908_z()) {
         this.mc.getTwitchStream().func_152932_y().func_153039_l();
      }

   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.mc.displayGuiScreen(this.field_152309_a);
         } else {
            this.mc.gameSettings.streamPreferredServer = "";
            this.mc.gameSettings.saveOptions();
         }
      }

   }

   public GuiIngestServers(GuiScreen var1) {
      this.field_152309_a = var1;
   }

   static FontRenderer access$0(GuiIngestServers var0) {
      return var0.fontRendererObj;
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.field_152311_g.handleMouseInput();
   }

   public void initGui() {
      this.field_152310_f = I18n.format("options.stream.ingest.title");
      this.field_152311_g = new GuiIngestServers.ServerList(this, this.mc);
      if (!this.mc.getTwitchStream().func_152908_z()) {
         this.mc.getTwitchStream().func_152909_x();
      }

      this.buttonList.add(new GuiButton(1, width / 2 - 155, height - 24 - 6, 150, 20, I18n.format("gui.done")));
      this.buttonList.add(new GuiButton(2, width / 2 + 5, height - 24 - 6, 150, 20, I18n.format("options.stream.ingest.reset")));
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.field_152311_g.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, this.field_152310_f, width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   class ServerList extends GuiSlot {
      final GuiIngestServers this$0;

      protected int getScrollBarX() {
         return super.getScrollBarX() + 15;
      }

      protected int getSize() {
         return this.mc.getTwitchStream().func_152925_v().length;
      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         this.mc.gameSettings.streamPreferredServer = this.mc.getTwitchStream().func_152925_v()[var1].serverUrl;
         this.mc.gameSettings.saveOptions();
      }

      protected void drawBackground() {
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         IngestServer var7 = this.mc.getTwitchStream().func_152925_v()[var1];
         String var8 = var7.serverUrl.replaceAll("\\{stream_key\\}", "");
         String var9 = (int)var7.bitrateKbps + " kbps";
         String var10 = null;
         IngestServerTester var11 = this.mc.getTwitchStream().func_152932_y();
         if (var11 != null) {
            if (var7 == var11.func_153040_c()) {
               var8 = EnumChatFormatting.GREEN + var8;
               var9 = (int)(var11.func_153030_h() * 100.0F) + "%";
            } else if (var1 < var11.func_153028_p()) {
               if (var7.bitrateKbps == 0.0F) {
                  var9 = EnumChatFormatting.RED + "Down!";
               }
            } else {
               var9 = EnumChatFormatting.OBFUSCATED + "1234" + EnumChatFormatting.RESET + " kbps";
            }
         } else if (var7.bitrateKbps == 0.0F) {
            var9 = EnumChatFormatting.RED + "Down!";
         }

         var2 -= 15;
         if (this.isSelected(var1)) {
            var10 = EnumChatFormatting.BLUE + "(Preferred)";
         } else if (var7.defaultServer) {
            var10 = EnumChatFormatting.GREEN + "(Default)";
         }

         this.this$0.drawString(GuiIngestServers.access$0(this.this$0), var7.serverName, var2 + 2, var3 + 5, 16777215);
         this.this$0.drawString(GuiIngestServers.access$0(this.this$0), var8, var2 + 2, var3 + GuiIngestServers.access$0(this.this$0).FONT_HEIGHT + 5 + 3, 3158064);
         this.this$0.drawString(GuiIngestServers.access$0(this.this$0), var9, this.getScrollBarX() - 5 - GuiIngestServers.access$0(this.this$0).getStringWidth(var9), var3 + 5, 8421504);
         if (var10 != null) {
            this.this$0.drawString(GuiIngestServers.access$0(this.this$0), var10, this.getScrollBarX() - 5 - GuiIngestServers.access$0(this.this$0).getStringWidth(var10), var3 + 5 + 3 + GuiIngestServers.access$0(this.this$0).FONT_HEIGHT, 8421504);
         }

      }

      public ServerList(GuiIngestServers var1, Minecraft var2) {
         super(var2, GuiIngestServers.width, GuiIngestServers.height, 32, GuiIngestServers.height - 35, (int)((double)Minecraft.fontRendererObj.FONT_HEIGHT * 3.5D));
         this.this$0 = var1;
         this.setShowSelectionBox(false);
      }

      protected boolean isSelected(int var1) {
         return this.mc.getTwitchStream().func_152925_v()[var1].serverUrl.equals(this.mc.gameSettings.streamPreferredServer);
      }
   }
}
