package Velo.api.Util.Render;


import Velo.api.Util.Other.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ScrollingText {
   private final String text;
   private final float stringWidth;
   private final int width;
   private final int height;
   private final int backgroundColor;
   private final int transparentBackgroundColor;
   private final int textColor;
   private float scale = 1.0F;
   private ScrollingText parent;
   private ScrollingText child;
   private long lastTime;
   private long startOfWait;
   private float offset;
   private ScrollingText.State state;

   public ScrollingText(String text, int width, int height, int backgroundColor, int textColor) {
      this.state = ScrollingText.State.LEFT;
      this.text = text;
      this.stringWidth = (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) * ((float)height / 10.0F);
      this.width = width;
      this.height = height;
      this.backgroundColor = backgroundColor;
      this.transparentBackgroundColor = (backgroundColor >> 16 & 255) << 16 | (backgroundColor >> 8 & 255) << 8 | backgroundColor & 255;
      this.textColor = textColor;
   }

   public String getText() {
      return this.text;
   }

   public void setParent(ScrollingText parent) {
      this.parent = parent;
   }

   public void setChild(ScrollingText child) {
      this.child = child;
   }

   public float getScale() {
      return this.scale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public void render(float x, float y) {
      if (this.stringWidth > (float)this.width) {
         double delta = (double)(Minecraft.getSystemTime() - this.lastTime) / 50.0D;
         this.lastTime = Minecraft.getSystemTime();
         switch(this.state) {
         case LEFT:
            if (this.startOfWait == 0L) {
               this.startOfWait = this.lastTime;
            }

            if ((this.child == null || this.child.stringWidth <= (float)this.child.width || this.child.state == ScrollingText.State.LEFT && this.child.startOfWait != 0L && this.child.lastTime - this.child.startOfWait > 4000L) && (this.parent == null || this.parent.stringWidth <= (float)this.parent.width || this.parent.state != ScrollingText.State.LEFT) && this.lastTime - this.startOfWait > 4000L) {
               this.startOfWait = 0L;
               this.state = ScrollingText.State.SCROLL_RIGHT;
            }
            break;
         case SCROLL_RIGHT:
            this.offset = (float)((double)this.offset + delta);
            if (this.offset >= this.stringWidth - (float)this.width) {
               this.offset = this.stringWidth - (float)this.width;
               this.state = ScrollingText.State.RIGHT;
            }
            break;
         case RIGHT:
            if (this.startOfWait == 0L) {
               this.startOfWait = this.lastTime;
            }

            if ((this.child == null || this.child.stringWidth <= (float)this.child.width || this.child.state == ScrollingText.State.RIGHT && this.child.startOfWait != 0L && this.child.lastTime - this.child.startOfWait > 2500L) && (this.parent == null || this.parent.stringWidth <= (float)this.parent.width || this.parent.state != ScrollingText.State.RIGHT) && this.lastTime - this.startOfWait > 2500L) {
               this.startOfWait = 0L;
               this.state = ScrollingText.State.SCROLL_LEFT;
            }
            break;
         case SCROLL_LEFT:
            this.offset = (float)((double)this.offset - delta);
            if (this.offset <= 0.0F) {
               this.offset = 0.0F;
               this.state = ScrollingText.State.LEFT;
            }
         }
      }

      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      float scaleFactor = (float)res.getScaleFactor() * this.scale;
      Depth.pre();
      Depth.mask();
      this.drawScaledString(this.text, x - this.offset, y, -1, (float)this.height / 10.0F);
      Depth.render(518);
      RenderUtil.rectangle((double)x, (double)y, (double)(x + (float)this.width), (double)(y + (float)this.height), Colors.getColor(175));
      Depth.post();
      if (this.offset > 0.0F) {
         GlStateManager.pushMatrix();
         RenderUtil.drawGradientSideways((double)x, (double)y, (double)(x + 5.0F), (double)(y + (float)this.height), this.backgroundColor, this.transparentBackgroundColor);
         GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
         GlStateManager.popMatrix();
      }

      if (this.stringWidth > (float)this.width && this.offset < this.stringWidth - (float)this.width) {
         GlStateManager.pushMatrix();
         RenderUtil.drawGradientSideways((double)(x + (float)this.width - 5.0F), (double)y, (double)(x + (float)this.width), (double)(y + (float)this.height), this.transparentBackgroundColor, this.backgroundColor);
         GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
         GlStateManager.popMatrix();
      }

   }

   private void drawScaledString(String string, float x, float y, int color, float scale) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, 1.0F);
      GlStateManager.scale(scale, scale, scale);
      Minecraft.getMinecraft().fontRendererObj.drawString(string, 0.0F, 0.0F, color);
      GlStateManager.popMatrix();
   }

   private static enum State {
      LEFT,
      SCROLL_RIGHT,
      RIGHT,
      SCROLL_LEFT;
   }
}
