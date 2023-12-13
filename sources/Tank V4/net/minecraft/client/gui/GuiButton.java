package net.minecraft.client.gui;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
   public int id;
   protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
   protected int width;
   public boolean enabled;
   public String displayString;
   public boolean visible;
   protected boolean hovered;
   public int xPosition;
   protected int height;
   public int yPosition;
   private Color baseColor;

   public GuiButton setBaseColor(Color var1) {
      this.baseColor = var1;
      return this;
   }

   public GuiButton(int var1, int var2, int var3, String var4) {
      this(var1, var2, var3, 200, 20, var4);
      this.baseColor = new Color(0, 0, 0, 200);
   }

   public GuiButton(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.width = 200;
      this.height = 20;
      this.enabled = true;
      this.visible = true;
      this.id = var1;
      this.xPosition = var2;
      this.yPosition = var3;
      this.width = var4;
      this.height = var5;
      this.displayString = var6;
   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      return this.enabled && this.visible && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
   }

   public void drawButton(Minecraft var1, int var2, int var3) {
      if (this.visible) {
         FontRenderer var4 = Minecraft.fontRendererObj;
         var1.getTextureManager().bindTexture(buttonTextures);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
         int var5 = this.getHoverState(this.hovered);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.blendFunc(770, 771);
         drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2, this.height);
         drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
         this.mouseDragged(var1, var2, var3);
         int var6 = 14737632;
         if (!this.enabled) {
            var6 = 10526880;
         } else if (this.hovered) {
            var6 = 16777120;
         }

         this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
      }

   }

   public void mouseReleased(int var1, int var2) {
   }

   public void drawButtonForegroundLayer(int var1, int var2) {
   }

   public int getButtonWidth() {
      return this.width;
   }

   protected int getHoverState(boolean var1) {
      byte var2 = 1;
      if (!this.enabled) {
         var2 = 0;
      } else if (var1) {
         var2 = 2;
      }

      return var2;
   }

   public boolean isMouseOver() {
      return this.hovered;
   }

   protected void mouseDragged(Minecraft var1, int var2, int var3) {
   }

   public void playPressSound(SoundHandler var1) {
      var1.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
   }

   public void setWidth(int var1) {
      this.width = var1;
   }
}
