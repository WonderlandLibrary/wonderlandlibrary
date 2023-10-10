package net.minecraft.client.renderer;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public abstract class InventoryEffectRenderer extends GuiContainer {
   private boolean hasActivePotionEffects;

   private void drawActivePotionEffects() {
      int var1 = this.guiLeft - 124;
      int var2 = this.guiTop;
      boolean var3 = true;
      Collection var4 = Minecraft.thePlayer.getActivePotionEffects();
      if (!var4.isEmpty()) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableLighting();
         int var5 = 33;
         if (var4.size() > 5) {
            var5 = 132 / (var4.size() - 1);
         }

         for(Iterator var7 = Minecraft.thePlayer.getActivePotionEffects().iterator(); var7.hasNext(); var2 += var5) {
            PotionEffect var6 = (PotionEffect)var7.next();
            Potion var8 = Potion.potionTypes[var6.getPotionID()];
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(inventoryBackground);
            drawTexturedModalRect(var1, var2, 0, 166, 140, 32);
            if (var8.hasStatusIcon()) {
               int var9 = var8.getStatusIconIndex();
               drawTexturedModalRect(var1 + 6, var2 + 7, 0 + var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
            }

            String var11 = I18n.format(var8.getName());
            if (var6.getAmplifier() == 1) {
               var11 = var11 + " " + I18n.format("enchantment.level.2");
            } else if (var6.getAmplifier() == 2) {
               var11 = var11 + " " + I18n.format("enchantment.level.3");
            } else if (var6.getAmplifier() == 3) {
               var11 = var11 + " " + I18n.format("enchantment.level.4");
            }

            this.fontRendererObj.drawStringWithShadow(var11, (float)(var1 + 10 + 18), (float)(var2 + 6), 16777215);
            String var10 = Potion.getDurationString(var6);
            this.fontRendererObj.drawStringWithShadow(var10, (float)(var1 + 10 + 18), (float)(var2 + 6 + 10), 8355711);
         }
      }

   }

   protected void updateActivePotionEffects() {
      if (!Minecraft.thePlayer.getActivePotionEffects().isEmpty()) {
         this.guiLeft = 160 + (width - this.xSize - 200) / 2;
         this.hasActivePotionEffects = true;
      } else {
         this.guiLeft = (width - this.xSize) / 2;
         this.hasActivePotionEffects = false;
      }

   }

   public void initGui() {
      super.initGui();
      this.updateActivePotionEffects();
   }

   public InventoryEffectRenderer(Container var1) {
      super(var1);
   }

   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      if (this.hasActivePotionEffects) {
         this.drawActivePotionEffects();
      }

   }
}
