package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry {
   private final GuiMultiplayer field_148292_c;
   protected final Minecraft mc;
   private long field_148290_d = 0L;
   protected final LanServerDetector.LanServer field_148291_b;

   protected ServerListEntryLanDetected(GuiMultiplayer var1, LanServerDetector.LanServer var2) {
      this.field_148292_c = var1;
      this.field_148291_b = var2;
      this.mc = Minecraft.getMinecraft();
   }

   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public LanServerDetector.LanServer getLanServer() {
      return this.field_148291_b;
   }

   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      Minecraft.fontRendererObj.drawString(I18n.format("lanServer.title"), (double)(var2 + 32 + 3), (double)(var3 + 1), 16777215);
      Minecraft.fontRendererObj.drawString(this.field_148291_b.getServerMotd(), (double)(var2 + 32 + 3), (double)(var3 + 12), 8421504);
      if (this.mc.gameSettings.hideServerAddress) {
         Minecraft.fontRendererObj.drawString(I18n.format("selectServer.hiddenAddress"), (double)(var2 + 32 + 3), (double)(var3 + 12 + 11), 3158064);
      } else {
         Minecraft.fontRendererObj.drawString(this.field_148291_b.getServerIpPort(), (double)(var2 + 32 + 3), (double)(var3 + 12 + 11), 3158064);
      }

   }

   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.field_148292_c.selectServer(var1);
      if (Minecraft.getSystemTime() - this.field_148290_d < 250L) {
         this.field_148292_c.connectToSelected();
      }

      this.field_148290_d = Minecraft.getSystemTime();
      return false;
   }

   public void setSelected(int var1, int var2, int var3) {
   }
}
