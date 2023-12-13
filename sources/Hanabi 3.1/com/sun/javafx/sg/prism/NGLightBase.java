package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;

public class NGLightBase extends NGNode {
   private Color color;
   private boolean lightOn;
   private Affine3D worldTransform;
   Object[] scopedNodes;

   protected NGLightBase() {
      this.color = Color.WHITE;
      this.lightOn = true;
      this.scopedNodes = null;
   }

   public void setTransformMatrix(BaseTransform var1) {
      super.setTransformMatrix(var1);
   }

   protected void doRender(Graphics var1) {
   }

   protected void renderContent(Graphics var1) {
   }

   protected boolean hasOverlappingContents() {
      return false;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Object var1) {
      if (!this.color.equals(var1)) {
         this.color = (Color)var1;
         this.visualsChanged();
      }

   }

   public boolean isLightOn() {
      return this.lightOn;
   }

   public void setLightOn(boolean var1) {
      if (this.lightOn != var1) {
         this.visualsChanged();
         this.lightOn = var1;
      }

   }

   public Affine3D getWorldTransform() {
      return this.worldTransform;
   }

   public void setWorldTransform(Affine3D var1) {
      this.worldTransform = var1;
   }

   public void setScope(Object[] var1) {
      if (this.scopedNodes != var1) {
         this.scopedNodes = var1;
         this.visualsChanged();
      }

   }

   final boolean affects(NGShape3D var1) {
      if (!this.lightOn) {
         return false;
      } else if (this.scopedNodes == null) {
         return true;
      } else {
         for(int var2 = 0; var2 < this.scopedNodes.length; ++var2) {
            Object var3 = this.scopedNodes[var2];
            if (var3 instanceof NGGroup) {
               for(NGNode var4 = var1.getParent(); var4 != null; var4 = var4.getParent()) {
                  if (var3 == var4) {
                     return true;
                  }
               }
            } else if (var3 == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public void release() {
   }
}
