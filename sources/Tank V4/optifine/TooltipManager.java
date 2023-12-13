package optifine;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.settings.GameSettings;

public class TooltipManager {
   private GuiScreen guiScreen = null;
   private int lastMouseX = 0;
   private int lastMouseY = 0;
   private long mouseStillTime = 0L;

   public TooltipManager(GuiScreen var1) {
      this.guiScreen = var1;
   }

   public void drawTooltips(int var1, int var2, List var3) {
      if (Math.abs(var1 - this.lastMouseX) <= 5 && Math.abs(var2 - this.lastMouseY) <= 5) {
         short var4 = 700;
         if (System.currentTimeMillis() >= this.mouseStillTime + (long)var4) {
            int var5 = GuiScreen.width / 2 - 150;
            int var6 = GuiScreen.height / 6 - 7;
            if (var2 <= var6 + 98) {
               var6 += 105;
            }

            int var7 = var5 + 150 + 150;
            int var8 = var6 + 84 + 10;
            GuiButton var9 = this.getSelectedButton(var1, var2, var3);
            if (var9 instanceof IOptionControl) {
               IOptionControl var10 = (IOptionControl)var9;
               GameSettings.Options var11 = var10.getOption();
               String[] var12 = getTooltipLines(var11);
               if (var12 == null) {
                  return;
               }

               GuiVideoSettings.drawGradientRect(this.guiScreen, var5, var6, var7, var8, -536870912, -536870912);

               for(int var13 = 0; var13 < var12.length; ++var13) {
                  String var14 = var12[var13];
                  int var15 = 14540253;
                  if (var14.endsWith("!")) {
                     var15 = 16719904;
                  }

                  Minecraft.getMinecraft();
                  FontRenderer var16 = Minecraft.fontRendererObj;
                  var16.drawStringWithShadow(var14, (float)(var5 + 5), (float)(var6 + 5 + var13 * 11), var15);
               }
            }
         }
      } else {
         this.lastMouseX = var1;
         this.lastMouseY = var2;
         this.mouseStillTime = System.currentTimeMillis();
      }

   }

   private GuiButton getSelectedButton(int var1, int var2, List var3) {
      for(int var4 = 0; var4 < var3.size(); ++var4) {
         GuiButton var5 = (GuiButton)var3.get(var4);
         int var6 = GuiVideoSettings.getButtonWidth(var5);
         int var7 = GuiVideoSettings.getButtonHeight(var5);
         boolean var8 = var1 >= var5.xPosition && var2 >= var5.yPosition && var1 < var5.xPosition + var6 && var2 < var5.yPosition + var7;
         if (var8) {
            return var5;
         }
      }

      return null;
   }

   private static String[] getTooltipLines(GameSettings.Options var0) {
      return getTooltipLines(var0.getEnumString());
   }

   private static String[] getTooltipLines(String var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 10; ++var2) {
         String var3 = var0 + ".tooltip." + (var2 + 1);
         String var4 = Lang.get(var3, (String)null);
         if (var4 == null) {
            break;
         }

         var1.add(var4);
      }

      if (var1.size() <= 0) {
         return null;
      } else {
         String[] var5 = (String[])var1.toArray(new String[var1.size()]);
         return var5;
      }
   }
}
