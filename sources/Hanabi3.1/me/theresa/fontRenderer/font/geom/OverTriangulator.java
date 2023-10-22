package me.theresa.fontRenderer.font.geom;

public class OverTriangulator implements Triangulator {
   private final float[][] triangles;

   public OverTriangulator(Triangulator tris) {
      this.triangles = new float[tris.getTriangleCount() * 6 * 3][2];
      int tcount = 0;

      for(int i = 0; i < tris.getTriangleCount(); ++i) {
         float cx = 0.0F;
         float cy = 0.0F;

         int p;
         for(p = 0; p < 3; ++p) {
            float[] pt = tris.getTrianglePoint(i, p);
            cx += pt[0];
            cy += pt[1];
         }

         cx /= 3.0F;
         cy /= 3.0F;

         float[] pt1;
         float[] pt2;
         int n;
         for(p = 0; p < 3; ++p) {
            n = p + 1;
            if (n > 2) {
               n = 0;
            }

            pt1 = tris.getTrianglePoint(i, p);
            pt2 = tris.getTrianglePoint(i, n);
            pt1[0] = (pt1[0] + pt2[0]) / 2.0F;
            pt1[1] = (pt1[1] + pt2[1]) / 2.0F;
            this.triangles[tcount * 3 + 0][0] = cx;
            this.triangles[tcount * 3 + 0][1] = cy;
            this.triangles[tcount * 3 + 1][0] = pt1[0];
            this.triangles[tcount * 3 + 1][1] = pt1[1];
            this.triangles[tcount * 3 + 2][0] = pt2[0];
            this.triangles[tcount * 3 + 2][1] = pt2[1];
            ++tcount;
         }

         for(p = 0; p < 3; ++p) {
            n = p + 1;
            if (n > 2) {
               n = 0;
            }

            pt1 = tris.getTrianglePoint(i, p);
            pt2 = tris.getTrianglePoint(i, n);
            pt2[0] = (pt1[0] + pt2[0]) / 2.0F;
            pt2[1] = (pt1[1] + pt2[1]) / 2.0F;
            this.triangles[tcount * 3 + 0][0] = cx;
            this.triangles[tcount * 3 + 0][1] = cy;
            this.triangles[tcount * 3 + 1][0] = pt1[0];
            this.triangles[tcount * 3 + 1][1] = pt1[1];
            this.triangles[tcount * 3 + 2][0] = pt2[0];
            this.triangles[tcount * 3 + 2][1] = pt2[1];
            ++tcount;
         }
      }

   }

   public void addPolyPoint(float x, float y) {
   }

   public int getTriangleCount() {
      return this.triangles.length / 3;
   }

   public float[] getTrianglePoint(int tri, int i) {
      float[] pt = this.triangles[tri * 3 + i];
      return new float[]{pt[0], pt[1]};
   }

   public void startHole() {
   }

   public boolean triangulate() {
      return true;
   }
}
