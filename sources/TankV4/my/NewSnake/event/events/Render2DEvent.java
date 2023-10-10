package my.NewSnake.event.events;

import my.NewSnake.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent extends Event {
   private ScaledResolution scaledResolution;
   private int width;
   private int height;
   private float partialTicks;

   public void setTicks(int var1) {
      this.partialTicks = (float)var1;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public ScaledResolution getScaledResolution() {
      return this.scaledResolution;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public void setHeight(int var1) {
      this.height = var1;
   }

   public Render2DEvent(int var1, int var2, ScaledResolution var3, float var4) {
      this.width = var1;
      this.height = var2;
      this.scaledResolution = var3;
      this.partialTicks = var4;
   }

   public float getTicks() {
      return this.partialTicks;
   }
}
