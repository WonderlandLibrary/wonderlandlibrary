package com.sun.javafx.geom;

public interface PathConsumer2D {
   void moveTo(float var1, float var2);

   void lineTo(float var1, float var2);

   void quadTo(float var1, float var2, float var3, float var4);

   void curveTo(float var1, float var2, float var3, float var4, float var5, float var6);

   void closePath();

   void pathDone();
}
