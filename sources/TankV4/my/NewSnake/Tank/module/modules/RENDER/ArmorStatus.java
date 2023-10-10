package my.NewSnake.Tank.module.modules.RENDER;

import java.awt.Color;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class ArmorStatus extends Module {
   public Minecraft mc = Minecraft.getMinecraft();

   public void getRainbow2(String var1, double var2, double var4) {
      int var6 = (int)var2;

      for(int var7 = 0; var7 < var1.length(); ++var7) {
         String var8 = String.valueOf(var1.charAt(var7));
         ClientUtils.clientFont().drawStringWithShadow(var8, (double)var6, var4, getRainbow(540, -15 * var7 * 3));
         var6 += ClientUtils.clientFont().getStringWidth(var8);
      }

   }

   @EventTarget
   private void onRender2D(Render2DEvent var1) {
      int var2 = var1.getWidth() / 2 + 9;

      for(int var3 = 0; var3 < 5; ++var3) {
         ItemStack var4 = Minecraft.thePlayer.getEquipmentInSlot(var3);
         if (var4 != null) {
            float var5 = this.mc.getRenderItem().zLevel;
            GL11.glPushMatrix();
            GlStateManager.clear(256);
            GlStateManager.disableAlpha();
            RenderHelper.enableStandardItemLighting();
            this.mc.getRenderItem().zLevel = -100.0F;
            this.mc.getRenderItem().renderItemIntoGUI(var4, var2, var1.getHeight() - 55);
            this.mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, var4, var2, var1.getHeight() - 55);
            this.mc.getRenderItem().zLevel = var5;
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableAlpha();
            GL11.glPopMatrix();
            if (var4.getItem() instanceof ItemSword || var4.getItem() instanceof ItemTool || var4.getItem() instanceof ItemArmor || var4.getItem() instanceof ItemBow) {
               GlStateManager.pushMatrix();
               int var6 = var4.getMaxDamage() - var4.getItemDamage();
               int var7 = var1.getHeight() - 60;
               GlStateManager.scale(0.5D, 0.5D, 0.5D);
               this.getRainbow2(String.valueOf(var6), (double)((float)(var2 + 4) / 0.5F), (double)((float)var7 / 0.5F));
               GlStateManager.popMatrix();
            }

            var2 += 16;
         }
      }

   }

   public static int getRainbow(int var0, int var1) {
      float var2 = (float)((System.currentTimeMillis() + (long)var1) % (long)var0);
      var2 /= (float)var0;
      return Color.getHSBColor(var2, 0.9F, 1.0F).getRGB();
   }
}
