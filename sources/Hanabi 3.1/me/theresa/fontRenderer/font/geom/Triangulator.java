package me.theresa.fontRenderer.font.geom;

import java.io.Serializable;

public interface Triangulator extends Serializable {
   int getTriangleCount();

   float[] getTrianglePoint(int var1, int var2);

   void addPolyPoint(float var1, float var2);

   void startHole();

   boolean triangulate();
}
