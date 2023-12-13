package org.newdawn.slick.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class LocatedImage {
   private Image image;
   private int x;
   private int y;
   private Color filter;
   private float width;
   private float height;

   public LocatedImage(Image var1, int var2, int var3) {
      this.filter = Color.white;
      this.image = var1;
      this.x = var2;
      this.y = var3;
      this.width = (float)var1.getWidth();
      this.height = (float)var1.getHeight();
   }

   public float getHeight() {
      return this.height;
   }

   public float getWidth() {
      return this.width;
   }

   public void setHeight(float var1) {
      this.height = var1;
   }

   public void setWidth(float var1) {
      this.width = var1;
   }

   public void setColor(Color var1) {
      this.filter = var1;
   }

   public Color getColor() {
      return this.filter;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public void draw() {
      this.image.draw((float)this.x, (float)this.y, this.width, this.height, this.filter);
   }
}
