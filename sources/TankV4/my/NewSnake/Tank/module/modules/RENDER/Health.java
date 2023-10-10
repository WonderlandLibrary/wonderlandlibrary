package my.NewSnake.Tank.module.modules.RENDER;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

@Module.Mod
public final class Health extends Module {
   private int width;
   private final DecimalFormat decimalFormat;

   @EventTarget
   public void onRender2D(Render2DEvent var1) {
      ScaledResolution var3 = new ScaledResolution(ClientUtils.mc());
      ClientUtils.mc();
      if (Minecraft.thePlayer.getHealth() >= 0.0F) {
         ClientUtils.mc();
         if (Minecraft.thePlayer.getHealth() < 10.0F) {
            this.width = 3;
         }
      }

      ClientUtils.mc();
      if (Minecraft.thePlayer.getHealth() >= 10.0F) {
         ClientUtils.mc();
         if (Minecraft.thePlayer.getHealth() < 100.0F) {
            this.width = 3;
         }
      }

      ClientUtils.mc();
      float var4 = Minecraft.thePlayer.getHealth();
      ClientUtils.mc();
      float var5 = Minecraft.thePlayer.getAbsorptionAmount();
      String var6 = var5 <= 0.0F ? "" : "§e" + this.decimalFormat.format((double)(var5 / 2.0F)) + "§6❤";
      String var7 = this.decimalFormat.format((double)(var4 / 2.0F)) + "§c❤ " + var6;
      int var8 = (new ScaledResolution(ClientUtils.mc())).getScaledWidth() / 2 - this.width;
      new ScaledResolution(ClientUtils.mc());
      int var9 = ScaledResolution.getScaledHeight() / 2 + 25;
      ClientUtils.mc();
      Minecraft.fontRendererObj.drawString(var7, var5 > 0.0F ? (float)var8 - 15.5F : (float)var8 - 3.5F, (float)var9, this.getHealthColor(), true);
      GL11.glPushAttrib(1048575);
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      ClientUtils.mc().getTextureManager().bindTexture(net.minecraft.client.gui.Gui.icons);
      float var2 = 0.0F;

      while(true) {
         ClientUtils.mc();
         float var10000;
         if (!(var2 < Minecraft.thePlayer.getMaxHealth() / 2.0F)) {
            var2 = 0.0F;

            while(true) {
               ClientUtils.mc();
               if (!(var2 < Minecraft.thePlayer.getHealth() / 2.0F)) {
                  GL11.glPopAttrib();
                  GL11.glPopMatrix();
                  return;
               }

               var10000 = (float)var3.getScaledWidth() / 2.0F;
               ClientUtils.mc();
               net.minecraft.client.gui.Gui.drawTexturedModalRect(var10000 - Minecraft.thePlayer.getMaxHealth() / 2.5F * 10.0F / 2.0F + var2 * 8.0F, (float)ScaledResolution.getScaledHeight() / 2.0F + 15.0F, 52, 0, 9, 9);
               ++var2;
            }
         }

         var10000 = (float)var3.getScaledWidth() / 2.0F;
         ClientUtils.mc();
         net.minecraft.client.gui.Gui.drawTexturedModalRect(var10000 - Minecraft.thePlayer.getMaxHealth() / 2.5F * 10.0F / 2.0F + var2 * 8.0F, (float)ScaledResolution.getScaledHeight() / 2.0F + 15.0F, 16, 0, 9, 9);
         ++var2;
      }
   }

   public Health() {
      this.decimalFormat = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
   }

   private int getHealthColor() {
      ClientUtils.mc();
      if (Minecraft.thePlayer.getHealth() <= 2.0F) {
         return (new Color(255, 0, 0)).getRGB();
      } else {
         ClientUtils.mc();
         if (Minecraft.thePlayer.getHealth() <= 6.0F) {
            return (new Color(255, 110, 0)).getRGB();
         } else {
            ClientUtils.mc();
            if (Minecraft.thePlayer.getHealth() <= 8.0F) {
               return (new Color(255, 182, 0)).getRGB();
            } else {
               ClientUtils.mc();
               if (Minecraft.thePlayer.getHealth() <= 10.0F) {
                  return (new Color(255, 255, 0)).getRGB();
               } else {
                  ClientUtils.mc();
                  if (Minecraft.thePlayer.getHealth() <= 13.0F) {
                     return (new Color(255, 255, 0)).getRGB();
                  } else {
                     ClientUtils.mc();
                     if (Minecraft.thePlayer.getHealth() <= 15.5F) {
                        return (new Color(182, 255, 0)).getRGB();
                     } else {
                        ClientUtils.mc();
                        if (Minecraft.thePlayer.getHealth() <= 18.0F) {
                           return (new Color(108, 255, 0)).getRGB();
                        } else {
                           ClientUtils.mc();
                           return Minecraft.thePlayer.getHealth() <= 20.0F ? (new Color(0, 255, 0)).getRGB() : 0;
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
