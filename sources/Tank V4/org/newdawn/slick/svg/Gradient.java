package org.newdawn.slick.svg;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.geom.Transform;

public class Gradient {
   private String name;
   private ArrayList steps = new ArrayList();
   private float x1;
   private float x2;
   private float y1;
   private float y2;
   private float r;
   private Image image;
   private boolean radial;
   private Transform transform;
   private String ref;

   public Gradient(String var1, boolean var2) {
      this.name = var1;
      this.radial = var2;
   }

   public boolean isRadial() {
      return this.radial;
   }

   public void setTransform(Transform var1) {
      this.transform = var1;
   }

   public Transform getTransform() {
      return this.transform;
   }

   public void reference(String var1) {
      this.ref = var1;
   }

   public void resolve(Diagram var1) {
      if (this.ref != null) {
         Gradient var2 = var1.getGradient(this.ref);

         for(int var3 = 0; var3 < var2.steps.size(); ++var3) {
            this.steps.add(var2.steps.get(var3));
         }

      }
   }

   public void genImage() {
      if (this.image == null) {
         ImageBuffer var1 = new ImageBuffer(128, 16);

         for(int var2 = 0; var2 < 128; ++var2) {
            Color var3 = this.getColorAt((float)var2 / 128.0F);

            for(int var4 = 0; var4 < 16; ++var4) {
               var1.setRGBA(var2, var4, var3.getRedByte(), var3.getGreenByte(), var3.getBlueByte(), var3.getAlphaByte());
            }
         }

         this.image = var1.getImage();
      }

   }

   public Image getImage() {
      this.genImage();
      return this.image;
   }

   public void setR(float var1) {
      this.r = var1;
   }

   public void setX1(float var1) {
      this.x1 = var1;
   }

   public void setX2(float var1) {
      this.x2 = var1;
   }

   public void setY1(float var1) {
      this.y1 = var1;
   }

   public void setY2(float var1) {
      this.y2 = var1;
   }

   public float getR() {
      return this.r;
   }

   public float getX1() {
      return this.x1;
   }

   public float getX2() {
      return this.x2;
   }

   public float getY1() {
      return this.y1;
   }

   public float getY2() {
      return this.y2;
   }

   public void addStep(float var1, Color var2) {
      this.steps.add(new Gradient.Step(this, var1, var2));
   }

   public Color getColorAt(float var1) {
      if (var1 <= 0.0F) {
         return ((Gradient.Step)this.steps.get(0)).col;
      } else if (var1 > 1.0F) {
         return ((Gradient.Step)this.steps.get(this.steps.size() - 1)).col;
      } else {
         for(int var2 = 1; var2 < this.steps.size(); ++var2) {
            Gradient.Step var3 = (Gradient.Step)this.steps.get(var2 - 1);
            Gradient.Step var4 = (Gradient.Step)this.steps.get(var2);
            if (var1 <= var4.location) {
               float var5 = var4.location - var3.location;
               var1 -= var3.location;
               float var6 = var1 / var5;
               Color var7 = new Color(1, 1, 1, 1);
               var7.a = var3.col.a * (1.0F - var6) + var4.col.a * var6;
               var7.r = var3.col.r * (1.0F - var6) + var4.col.r * var6;
               var7.g = var3.col.g * (1.0F - var6) + var4.col.g * var6;
               var7.b = var3.col.b * (1.0F - var6) + var4.col.b * var6;
               return var7;
            }
         }

         return Color.black;
      }
   }

   private class Step {
      float location;
      Color col;
      private final Gradient this$0;

      public Step(Gradient var1, float var2, Color var3) {
         this.this$0 = var1;
         this.location = var2;
         this.col = var3;
      }
   }
}
