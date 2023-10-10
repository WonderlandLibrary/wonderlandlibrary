package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiSpectator extends Gui implements ISpectatorMenuRecipient {
   public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
   private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
   private SpectatorMenu field_175271_i;
   private long field_175270_h;
   private final Minecraft field_175268_g;

   public void renderTooltip(ScaledResolution var1, float var2) {
      if (this.field_175271_i != null) {
         float var3 = this.func_175265_c();
         if (var3 <= 0.0F) {
            this.field_175271_i.func_178641_d();
         } else {
            int var4 = var1.getScaledWidth() / 2;
            float var5 = zLevel;
            zLevel = -90.0F;
            float var6 = (float)ScaledResolution.getScaledHeight() - 22.0F * var3;
            SpectatorDetails var7 = this.field_175271_i.func_178646_f();
            this.func_175258_a(var1, var3, var4, var6, var7);
            zLevel = var5;
         }
      }

   }

   public GuiSpectator(Minecraft var1) {
      this.field_175268_g = var1;
   }

   public void func_175261_b() {
      this.field_175270_h = Minecraft.getSystemTime();
      if (this != false) {
         int var1 = this.field_175271_i.func_178648_e();
         if (var1 != -1) {
            this.field_175271_i.func_178644_b(var1);
         }
      } else {
         this.field_175271_i = new SpectatorMenu(this);
      }

   }

   public void func_175260_a(int var1) {
      this.field_175270_h = Minecraft.getSystemTime();
      if (this.field_175271_i != null) {
         this.field_175271_i.func_178644_b(var1);
      } else {
         this.field_175271_i = new SpectatorMenu(this);
      }

   }

   public void func_175263_a(ScaledResolution var1) {
      int var2 = (int)(this.func_175265_c() * 255.0F);
      if (var2 > 3 && this.field_175271_i != null) {
         ISpectatorMenuObject var3 = this.field_175271_i.func_178645_b();
         String var4 = var3 != SpectatorMenu.field_178657_a ? var3.getSpectatorName().getFormattedText() : this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();
         if (var4 != null) {
            int var5 = (var1.getScaledWidth() - Minecraft.fontRendererObj.getStringWidth(var4)) / 2;
            int var6 = ScaledResolution.getScaledHeight() - 35;
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Minecraft.fontRendererObj.drawStringWithShadow(var4, (float)var5, (float)var6, 16777215 + (var2 << 24));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }

   }

   private void func_175266_a(int var1, int var2, float var3, float var4, ISpectatorMenuObject var5) {
      this.field_175268_g.getTextureManager().bindTexture(field_175269_a);
      if (var5 != SpectatorMenu.field_178657_a) {
         int var6 = (int)(var4 * 255.0F);
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)var2, var3, 0.0F);
         float var7 = var5.func_178662_A_() ? 1.0F : 0.25F;
         GlStateManager.color(var7, var7, var7, var4);
         var5.func_178663_a(var7, var6);
         GlStateManager.popMatrix();
         String var8 = String.valueOf(GameSettings.getKeyDisplayString(this.field_175268_g.gameSettings.keyBindsHotbar[var1].getKeyCode()));
         if (var6 > 3 && var5.func_178662_A_()) {
            Minecraft.fontRendererObj.drawStringWithShadow(var8, (float)(var2 + 19 - 2 - Minecraft.fontRendererObj.getStringWidth(var8)), var3 + 6.0F + 3.0F, 16777215 + (var6 << 24));
         }
      }

   }

   protected void func_175258_a(ScaledResolution var1, float var2, int var3, float var4, SpectatorDetails var5) {
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(1.0F, 1.0F, 1.0F, var2);
      this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
      drawTexturedModalRect((float)(var3 - 91), var4, 0, 0, 182, 22);
      if (var5.func_178681_b() >= 0) {
         drawTexturedModalRect((float)(var3 - 91 - 1 + var5.func_178681_b() * 20), var4 - 1.0F, 0, 22, 24, 22);
      }

      RenderHelper.enableGUIStandardItemLighting();

      for(int var6 = 0; var6 < 9; ++var6) {
         this.func_175266_a(var6, var1.getScaledWidth() / 2 - 90 + var6 * 20 + 2, var4 + 3.0F, var2, var5.func_178680_a(var6));
      }

      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
   }

   public void func_175257_a(SpectatorMenu var1) {
      this.field_175271_i = null;
      this.field_175270_h = 0L;
   }

   public void func_175259_b(int var1) {
      int var2;
      for(var2 = this.field_175271_i.func_178648_e() + var1; var2 >= 0 && var2 <= 8 && (this.field_175271_i.func_178643_a(var2) == SpectatorMenu.field_178657_a || !this.field_175271_i.func_178643_a(var2).func_178662_A_()); var2 += var1) {
      }

      if (var2 >= 0 && var2 <= 8) {
         this.field_175271_i.func_178644_b(var2);
         this.field_175270_h = Minecraft.getSystemTime();
      }

   }

   private float func_175265_c() {
      long var1 = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
      return MathHelper.clamp_float((float)var1 / 2000.0F, 0.0F, 1.0F);
   }
}
