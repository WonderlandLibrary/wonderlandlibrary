package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiSlider extends GuiButton {
   private final GuiPageButtonList.GuiResponder responder;
   private GuiSlider.FormatHelper formatHelper;
   private String name;
   private float sliderPosition = 1.0F;
   public boolean isMouseDown;
   private final float max;
   private final float min;

   public float func_175220_c() {
      return this.min + (this.max - this.min) * this.sliderPosition;
   }

   public float func_175217_d() {
      return this.sliderPosition;
   }

   protected int getHoverState(boolean var1) {
      return 0;
   }

   private String getDisplayString() {
      return this.formatHelper == null ? I18n.format(this.name) + ": " + this.func_175220_c() : this.formatHelper.getText(this.id, I18n.format(this.name), this.func_175220_c());
   }

   public GuiSlider(GuiPageButtonList.GuiResponder var1, int var2, int var3, int var4, String var5, float var6, float var7, float var8, GuiSlider.FormatHelper var9) {
      super(var2, var3, var4, 150, 20, "");
      this.name = var5;
      this.min = var6;
      this.max = var7;
      this.sliderPosition = (var8 - var6) / (var7 - var6);
      this.formatHelper = var9;
      this.responder = var1;
      this.displayString = this.getDisplayString();
   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (super.mousePressed(var1, var2, var3)) {
         this.sliderPosition = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
         if (this.sliderPosition < 0.0F) {
            this.sliderPosition = 0.0F;
         }

         if (this.sliderPosition > 1.0F) {
            this.sliderPosition = 1.0F;
         }

         this.displayString = this.getDisplayString();
         this.responder.onTick(this.id, this.func_175220_c());
         this.isMouseDown = true;
         return true;
      } else {
         return false;
      }
   }

   protected void mouseDragged(Minecraft var1, int var2, int var3) {
      if (this.visible) {
         if (this.isMouseDown) {
            this.sliderPosition = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderPosition < 0.0F) {
               this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F) {
               this.sliderPosition = 1.0F;
            }

            this.displayString = this.getDisplayString();
            this.responder.onTick(this.id, this.func_175220_c());
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
         drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
      }

   }

   public void mouseReleased(int var1, int var2) {
      this.isMouseDown = false;
   }

   public void func_175219_a(float var1) {
      this.sliderPosition = var1;
      this.displayString = this.getDisplayString();
      this.responder.onTick(this.id, this.func_175220_c());
   }

   public void func_175218_a(float var1, boolean var2) {
      this.sliderPosition = (var1 - this.min) / (this.max - this.min);
      this.displayString = this.getDisplayString();
      if (var2) {
         this.responder.onTick(this.id, this.func_175220_c());
      }

   }

   public interface FormatHelper {
      String getText(int var1, String var2, float var3);
   }
}
