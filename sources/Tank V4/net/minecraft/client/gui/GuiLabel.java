package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiLabel extends Gui {
   private int field_146165_q;
   private int field_146166_p;
   private FontRenderer fontRenderer;
   public boolean visible = true;
   public int field_146174_h;
   private int field_146163_s;
   private List field_146173_k;
   protected int field_146161_f = 20;
   public int field_146162_g;
   private boolean labelBgEnabled;
   private int field_146169_o;
   private int field_146168_n;
   protected int field_146167_a = 200;
   private boolean centered;
   public int field_175204_i;

   public void drawLabel(Minecraft var1, int var2, int var3) {
      if (this.visible) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         this.drawLabelBackground(var1, var2, var3);
         int var4 = this.field_146174_h + this.field_146161_f / 2 + this.field_146163_s / 2;
         int var5 = var4 - this.field_146173_k.size() * 10 / 2;

         for(int var6 = 0; var6 < this.field_146173_k.size(); ++var6) {
            if (this.centered) {
               this.drawCenteredString(this.fontRenderer, (String)this.field_146173_k.get(var6), this.field_146162_g + this.field_146167_a / 2, var5 + var6 * 10, this.field_146168_n);
            } else {
               this.drawString(this.fontRenderer, (String)this.field_146173_k.get(var6), this.field_146162_g, var5 + var6 * 10, this.field_146168_n);
            }
         }
      }

   }

   protected void drawLabelBackground(Minecraft var1, int var2, int var3) {
      if (this.labelBgEnabled) {
         int var4 = this.field_146167_a + this.field_146163_s * 2;
         int var5 = this.field_146161_f + this.field_146163_s * 2;
         int var6 = this.field_146162_g - this.field_146163_s;
         int var7 = this.field_146174_h - this.field_146163_s;
         drawRect((double)var6, (double)var7, (double)(var6 + var4), (double)(var7 + var5), this.field_146169_o);
         drawHorizontalLine(var6, var6 + var4, var7, this.field_146166_p);
         drawHorizontalLine(var6, var6 + var4, var7 + var5, this.field_146165_q);
         drawVerticalLine(var6, var7, var7 + var5, this.field_146166_p);
         drawVerticalLine(var6 + var4, var7, var7 + var5, this.field_146165_q);
      }

   }

   public GuiLabel(FontRenderer var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.fontRenderer = var1;
      this.field_175204_i = var2;
      this.field_146162_g = var3;
      this.field_146174_h = var4;
      this.field_146167_a = var5;
      this.field_146161_f = var6;
      this.field_146173_k = Lists.newArrayList();
      this.centered = false;
      this.labelBgEnabled = false;
      this.field_146168_n = var7;
      this.field_146169_o = -1;
      this.field_146166_p = -1;
      this.field_146165_q = -1;
      this.field_146163_s = 0;
   }

   public GuiLabel setCentered() {
      this.centered = true;
      return this;
   }

   public void func_175202_a(String var1) {
      this.field_146173_k.add(I18n.format(var1));
   }
}
