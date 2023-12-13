package org.newdawn.slick.geom;

public interface Triangulator {
   int getTriangleCount();

   float[] getTrianglePoint(int var1, int var2);

   void addPolyPoint(float var1, float var2);

   void startHole();

   boolean triangulate();
}
