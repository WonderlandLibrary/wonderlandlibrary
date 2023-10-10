package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public abstract class GuiSlot {
   protected int initialClickY = -2;
   protected boolean field_148163_i = true;
   private boolean enabled = true;
   protected boolean showSelectionBox = true;
   protected int left;
   private int scrollUpButtonID;
   protected long lastClicked;
   protected int mouseY;
   protected final int slotHeight;
   protected boolean hasListHeader;
   private int scrollDownButtonID;
   protected int headerPadding;
   protected int top;
   protected float amountScrolled;
   protected int bottom;
   protected int width;
   protected int mouseX;
   protected int right;
   protected final Minecraft mc;
   protected boolean field_178041_q = true;
   protected float scrollMultiplier;
   protected int height;
   protected int selectedElement = -1;

   public int getSlotIndexFromScreenCoords(int var1, int var2) {
      int var3 = this.left + this.width / 2 - this.getListWidth() / 2;
      int var4 = this.left + this.width / 2 + this.getListWidth() / 2;
      int var5 = var2 - this.top - this.headerPadding + (int)this.amountScrolled - 4;
      int var6 = var5 / this.slotHeight;
      return var1 < this.getScrollBarX() && var1 >= var3 && var1 <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
   }

   public void registerScrollButtons(int var1, int var2) {
      this.scrollUpButtonID = var1;
      this.scrollDownButtonID = var2;
   }

   public void drawScreen(int var1, int var2, float var3) {
      if (this.field_178041_q) {
         this.mouseX = var1;
         this.mouseY = var2;
         this.drawBackground();
         int var4 = this.getScrollBarX();
         int var5 = var4 + 6;
         this.bindAmountScrolled();
         GlStateManager.disableLighting();
         GlStateManager.disableFog();
         Tessellator var6 = Tessellator.getInstance();
         WorldRenderer var7 = var6.getWorldRenderer();
         this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         float var8 = 32.0F;
         var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         var7.pos((double)this.left, (double)this.bottom, 0.0D).tex((double)((float)this.left / var8), (double)((float)(this.bottom + (int)this.amountScrolled) / var8)).color(32, 32, 32, 255).endVertex();
         var7.pos((double)this.right, (double)this.bottom, 0.0D).tex((double)((float)this.right / var8), (double)((float)(this.bottom + (int)this.amountScrolled) / var8)).color(32, 32, 32, 255).endVertex();
         var7.pos((double)this.right, (double)this.top, 0.0D).tex((double)((float)this.right / var8), (double)((float)(this.top + (int)this.amountScrolled) / var8)).color(32, 32, 32, 255).endVertex();
         var7.pos((double)this.left, (double)this.top, 0.0D).tex((double)((float)this.left / var8), (double)((float)(this.top + (int)this.amountScrolled) / var8)).color(32, 32, 32, 255).endVertex();
         var6.draw();
         int var9 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
         int var10 = this.top + 4 - (int)this.amountScrolled;
         if (this.hasListHeader) {
            this.drawListHeader(var9, var10, var6);
         }

         this.drawSelectionBox(var9, var10, var1, var2);
         GlStateManager.disableDepth();
         byte var11 = 4;
         this.overlayBackground(0, this.top, 255, 255);
         this.overlayBackground(this.bottom, this.height, 255, 255);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
         GlStateManager.disableAlpha();
         GlStateManager.shadeModel(7425);
         GlStateManager.disableTexture2D();
         var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         var7.pos((double)this.left, (double)(this.top + var11), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
         var7.pos((double)this.right, (double)(this.top + var11), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
         var7.pos((double)this.right, (double)this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
         var7.pos((double)this.left, (double)this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
         var6.draw();
         var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         var7.pos((double)this.left, (double)this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
         var7.pos((double)this.right, (double)this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
         var7.pos((double)this.right, (double)(this.bottom - var11), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
         var7.pos((double)this.left, (double)(this.bottom - var11), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
         var6.draw();
         int var12 = this.func_148135_f();
         if (var12 > 0) {
            int var13 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            var13 = MathHelper.clamp_int(var13, 32, this.bottom - this.top - 8);
            int var14 = (int)this.amountScrolled * (this.bottom - this.top - var13) / var12 + this.top;
            if (var14 < this.top) {
               var14 = this.top;
            }

            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var4, (double)this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)var5, (double)this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)var5, (double)this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)var4, (double)this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            var6.draw();
            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var4, (double)(var14 + var13), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var5, (double)(var14 + var13), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var5, (double)var14, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var4, (double)var14, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
            var6.draw();
            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var4, (double)(var14 + var13 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
            var7.pos((double)(var5 - 1), (double)(var14 + var13 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
            var7.pos((double)(var5 - 1), (double)var14, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
            var7.pos((double)var4, (double)var14, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
            var6.draw();
         }

         this.func_148142_b(var1, var2);
         GlStateManager.enableTexture2D();
         GlStateManager.shadeModel(7424);
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
      }

   }

   protected abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

   public GuiSlot(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
      this.mc = var1;
      this.width = var2;
      this.height = var3;
      this.top = var4;
      this.bottom = var5;
      this.slotHeight = var6;
      this.left = 0;
      this.right = var2;
   }

   protected void func_148132_a(int var1, int var2) {
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public int func_148135_f() {
      return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
   }

   protected abstract int getSize();

   public void handleMouseInput() {
      if (this.mouseY != 0) {
         int var1;
         int var2;
         int var3;
         int var4;
         if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
            var1 = (this.width - this.getListWidth()) / 2;
            var2 = (this.width + this.getListWidth()) / 2;
            var3 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
            var4 = var3 / this.slotHeight;
            if (var4 < this.getSize() && this.mouseX >= var1 && this.mouseX <= var2 && var4 >= 0 && var3 >= 0) {
               this.elementClicked(var4, false, this.mouseX, this.mouseY);
               this.selectedElement = var4;
            } else if (this.mouseX >= var1 && this.mouseX <= var2 && var3 < 0) {
               this.func_148132_a(this.mouseX - var1, this.mouseY - this.top + (int)this.amountScrolled - 4);
            }
         }

         if (Mouse.isButtonDown(0) && this.getEnabled()) {
            if (this.initialClickY != -1) {
               if (this.initialClickY >= 0) {
                  this.amountScrolled -= (float)(this.mouseY - this.initialClickY) * this.scrollMultiplier;
                  this.initialClickY = this.mouseY;
               }
            } else {
               boolean var10 = true;
               if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                  var2 = (this.width - this.getListWidth()) / 2;
                  var3 = (this.width + this.getListWidth()) / 2;
                  var4 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                  int var5 = var4 / this.slotHeight;
                  if (var5 < this.getSize() && this.mouseX >= var2 && this.mouseX <= var3 && var5 >= 0 && var4 >= 0) {
                     boolean var6 = var5 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                     this.elementClicked(var5, var6, this.mouseX, this.mouseY);
                     this.selectedElement = var5;
                     this.lastClicked = Minecraft.getSystemTime();
                  } else if (this.mouseX >= var2 && this.mouseX <= var3 && var4 < 0) {
                     this.func_148132_a(this.mouseX - var2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                     var10 = false;
                  }

                  int var11 = this.getScrollBarX();
                  int var7 = var11 + 6;
                  if (this.mouseX >= var11 && this.mouseX <= var7) {
                     this.scrollMultiplier = -1.0F;
                     int var8 = this.func_148135_f();
                     if (var8 < 1) {
                        var8 = 1;
                     }

                     int var9 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                     var9 = MathHelper.clamp_int(var9, 32, this.bottom - this.top - 8);
                     this.scrollMultiplier /= (float)(this.bottom - this.top - var9) / (float)var8;
                  } else {
                     this.scrollMultiplier = 1.0F;
                  }

                  if (var10) {
                     this.initialClickY = this.mouseY;
                  } else {
                     this.initialClickY = -2;
                  }
               } else {
                  this.initialClickY = -2;
               }
            }
         } else {
            this.initialClickY = -1;
         }

         var1 = Mouse.getEventDWheel();
         if (var1 != 0) {
            if (var1 > 0) {
               var1 = -1;
            } else if (var1 < 0) {
               var1 = 1;
            }

            this.amountScrolled += (float)(var1 * this.slotHeight / 2);
         }
      }

   }

   protected void overlayBackground(int var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.getInstance();
      WorldRenderer var6 = var5.getWorldRenderer();
      this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float var7 = 32.0F;
      var6.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var6.pos((double)this.left, (double)var2, 0.0D).tex(0.0D, (double)((float)var2 / 32.0F)).color(64, 64, 64, var4).endVertex();
      var6.pos((double)(this.left + this.width), (double)var2, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)var2 / 32.0F)).color(64, 64, 64, var4).endVertex();
      var6.pos((double)(this.left + this.width), (double)var1, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)var1 / 32.0F)).color(64, 64, 64, var3).endVertex();
      var6.pos((double)this.left, (double)var1, 0.0D).tex(0.0D, (double)((float)var1 / 32.0F)).color(64, 64, 64, var3).endVertex();
      var5.draw();
   }

   public int getListWidth() {
      return 220;
   }

   protected void drawListHeader(int var1, int var2, Tessellator var3) {
   }

   protected int getContentHeight() {
      return this.getSize() * this.slotHeight + this.headerPadding;
   }

   protected abstract void drawBackground();

   protected abstract void elementClicked(int var1, boolean var2, int var3, int var4);

   public int getAmountScrolled() {
      return (int)this.amountScrolled;
   }

   public void setShowSelectionBox(boolean var1) {
      this.showSelectionBox = var1;
   }

   public void setSlotXBoundsFromLeft(int var1) {
      this.left = var1;
      this.right = var1 + this.width;
   }

   protected void func_148142_b(int var1, int var2) {
   }

   public void setDimensions(int var1, int var2, int var3, int var4) {
      this.width = var1;
      this.height = var2;
      this.top = var3;
      this.bottom = var4;
      this.left = 0;
      this.right = var1;
   }

   public int getSlotHeight() {
      return this.slotHeight;
   }

   protected void func_178040_a(int var1, int var2, int var3) {
   }

   protected void drawSelectionBox(int var1, int var2, int var3, int var4) {
      int var5 = this.getSize();
      Tessellator var6 = Tessellator.getInstance();
      WorldRenderer var7 = var6.getWorldRenderer();

      for(int var8 = 0; var8 < var5; ++var8) {
         int var9 = var2 + var8 * this.slotHeight + this.headerPadding;
         int var10 = this.slotHeight - 4;
         if (var9 > this.bottom || var9 + var10 < this.top) {
            this.func_178040_a(var8, var1, var9);
         }

         if (this.showSelectionBox && this.isSelected(var8)) {
            int var11 = this.left + (this.width / 2 - this.getListWidth() / 2);
            int var12 = this.left + this.width / 2 + this.getListWidth() / 2;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableTexture2D();
            var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var7.pos((double)var11, (double)(var9 + var10 + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var12, (double)(var9 + var10 + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var12, (double)(var9 - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)var11, (double)(var9 - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
            var7.pos((double)(var11 + 1), (double)(var9 + var10 + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)(var12 - 1), (double)(var9 + var10 + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)(var12 - 1), (double)(var9 - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            var7.pos((double)(var11 + 1), (double)(var9 - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            var6.draw();
            GlStateManager.enableTexture2D();
         }

         this.drawSlot(var8, var1, var9, var10, var3, var4);
      }

   }

   protected void setHasListHeader(boolean var1, int var2) {
      this.hasListHeader = var1;
      this.headerPadding = var2;
      if (!var1) {
         this.headerPadding = 0;
      }

   }

   protected abstract boolean isSelected(int var1);

   protected int getScrollBarX() {
      return this.width / 2 + 124;
   }

   public void scrollBy(int var1) {
      this.amountScrolled += (float)var1;
      this.bindAmountScrolled();
      this.initialClickY = -2;
   }

   public void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == this.scrollUpButtonID) {
            this.amountScrolled -= (float)(this.slotHeight * 2 / 3);
            this.initialClickY = -2;
            this.bindAmountScrolled();
         } else if (var1.id == this.scrollDownButtonID) {
            this.amountScrolled += (float)(this.slotHeight * 2 / 3);
            this.initialClickY = -2;
            this.bindAmountScrolled();
         }
      }

   }

   protected void bindAmountScrolled() {
      this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, (float)this.func_148135_f());
   }
}
