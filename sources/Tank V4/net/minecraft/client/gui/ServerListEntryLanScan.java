package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry {
   private final Minecraft mc = Minecraft.getMinecraft();

   public void setSelected(int var1, int var2, int var3) {
   }

   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      return false;
   }

   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      int var9 = var3 + var5 / 2 - Minecraft.fontRendererObj.FONT_HEIGHT / 2;
      FontRenderer var10000 = Minecraft.fontRendererObj;
      String var10001 = I18n.format("lanServer.scanning");
      GuiScreen var10002 = this.mc.currentScreen;
      var10000.drawString(var10001, (double)(GuiScreen.width / 2 - Minecraft.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning")) / 2), (double)var9, 16777215);
      String var10;
      switch((int)(Minecraft.getSystemTime() / 300L % 4L)) {
      case 0:
      default:
         var10 = "O o o";
         break;
      case 1:
      case 3:
         var10 = "o O o";
         break;
      case 2:
         var10 = "o o O";
      }

      var10002 = this.mc.currentScreen;
      Minecraft.fontRendererObj.drawString(var10, (double)(GuiScreen.width / 2 - Minecraft.fontRendererObj.getStringWidth(var10) / 2), (double)(var9 + Minecraft.fontRendererObj.FONT_HEIGHT), 8421504);
   }
}
