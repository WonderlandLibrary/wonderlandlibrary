package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSphere;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public class Sphere extends Shape3D {
   static final int DEFAULT_DIVISIONS = 64;
   static final double DEFAULT_RADIUS = 1.0;
   private int divisions;
   private TriangleMesh mesh;
   private DoubleProperty radius;

   public Sphere() {
      this(1.0, 64);
   }

   public Sphere(double var1) {
      this(var1, 64);
   }

   public Sphere(double var1, int var3) {
      this.divisions = 64;
      this.divisions = var3 < 1 ? 1 : var3;
      this.setRadius(var1);
   }

   public final void setRadius(double var1) {
      this.radiusProperty().set(var1);
   }

   public final double getRadius() {
      return this.radius == null ? 1.0 : this.radius.get();
   }

   public final DoubleProperty radiusProperty() {
      if (this.radius == null) {
         this.radius = new SimpleDoubleProperty(this, "radius", 1.0) {
            public void invalidated() {
               Sphere.this.impl_markDirty(DirtyBits.MESH_GEOM);
               Sphere.this.manager.invalidateSphereMesh(Sphere.this.key);
               Sphere.this.key = 0;
               Sphere.this.impl_geomChanged();
            }
         };
      }

      return this.radius;
   }

   public int getDivisions() {
      return this.divisions;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGSphere();
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.MESH_GEOM)) {
         NGSphere var1 = (NGSphere)this.impl_getPeer();
         float var2 = (float)this.getRadius();
         if (var2 < 0.0F) {
            var1.updateMesh((NGTriangleMesh)null);
         } else {
            if (this.key == 0) {
               this.key = generateKey(var2, this.divisions);
            }

            this.mesh = this.manager.getSphereMesh(var2, this.divisions, this.key);
            this.mesh.impl_updatePG();
            var1.updateMesh(this.mesh.impl_getPGTriangleMesh());
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      float var3 = (float)this.getRadius();
      if (var3 < 0.0F) {
         return var1.makeEmpty();
      } else {
         var1 = var1.deriveWithNewBounds(-var3, -var3, -var3, var3, var3, var3);
         var1 = var2.transform(var1, var1);
         return var1;
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      double var5 = this.getRadius();
      double var7 = var1 * var1 + var3 * var3;
      return var7 <= var5 * var5;
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeIntersects(PickRay var1, PickResultChooser var2) {
      boolean var3 = this.divisions < 64 && this.mesh != null;
      double var4 = this.getRadius();
      Vec3d var6 = var1.getDirectionNoClone();
      double var7 = var6.x;
      double var9 = var6.y;
      double var11 = var6.z;
      Vec3d var13 = var1.getOriginNoClone();
      double var14 = var13.x;
      double var16 = var13.y;
      double var18 = var13.z;
      double var20 = var7 * var7 + var9 * var9 + var11 * var11;
      double var22 = 2.0 * (var7 * var14 + var9 * var16 + var11 * var18);
      double var24 = var14 * var14 + var16 * var16 + var18 * var18 - var4 * var4;
      double var26 = var22 * var22 - 4.0 * var20 * var24;
      if (var26 < 0.0) {
         return false;
      } else {
         double var28 = Math.sqrt(var26);
         double var30 = var22 < 0.0 ? (-var22 - var28) / 2.0 : (-var22 + var28) / 2.0;
         double var32 = var30 / var20;
         double var34 = var24 / var30;
         double var36;
         if (var32 > var34) {
            var36 = var32;
            var32 = var34;
            var34 = var36;
         }

         var36 = var1.getNearClip();
         double var38 = var1.getFarClip();
         if (!(var34 < var36) && !(var32 > var38)) {
            double var40 = var32;
            CullFace var42 = this.getCullFace();
            if (var32 < var36 || var42 == CullFace.FRONT) {
               if (var34 <= var38 && this.getCullFace() != CullFace.BACK) {
                  var40 = var34;
               } else if (!var3) {
                  return false;
               }
            }

            if (!Double.isInfinite(var40) && !Double.isNaN(var40)) {
               if (var3) {
                  return this.mesh.impl_computeIntersects(var1, var2, this, var42, false);
               } else {
                  if (var2 != null && var2.isCloser(var40)) {
                     Point3D var43 = PickResultChooser.computePoint(var1, var40);
                     Point3D var44 = new Point3D(var43.getX(), 0.0, var43.getZ());
                     Point3D var45 = var44.crossProduct(Rotate.Z_AXIS);
                     double var46 = var44.angle(Rotate.Z_AXIS);
                     if (var45.getY() > 0.0) {
                        var46 = 360.0 - var46;
                     }

                     Point2D var48 = new Point2D(1.0 - var46 / 360.0, 0.5 + var43.getY() / (2.0 * var4));
                     var2.offer(this, var40, -1, var43, var48);
                  }

                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private static int correctDivisions(int var0) {
      return (var0 + 3) / 4 * 4;
   }

   static TriangleMesh createMesh(int var0, float var1) {
      var0 = correctDivisions(var0);
      int var2 = var0 / 2;
      int var3 = var0 * (var2 - 1) + 2;
      int var4 = (var0 + 1) * (var2 - 1) + var0 * 2;
      int var5 = var0 * (var2 - 2) * 2 + var0 * 2;
      float var6 = 1.0F / (float)var0;
      float[] var7 = new float[var3 * 3];
      float[] var8 = new float[var4 * 2];
      int[] var9 = new int[var5 * 6];
      int var10 = 0;
      int var11 = 0;

      int var12;
      float var13;
      int var17;
      for(var12 = 0; var12 < var2 - 1; ++var12) {
         var13 = var6 * (float)(var12 + 1 - var2 / 2) * 2.0F * 3.1415927F;
         float var14 = (float)Math.sin((double)var13);
         float var15 = (float)Math.cos((double)var13);
         float var16 = 0.5F + var14 * 0.5F;

         for(var17 = 0; var17 < var0; ++var17) {
            double var18 = (double)(var6 * (float)var17 * 2.0F * 3.1415927F);
            float var20 = (float)Math.sin(var18);
            float var21 = (float)Math.cos(var18);
            var7[var10 + 0] = var20 * var15 * var1;
            var7[var10 + 2] = var21 * var15 * var1;
            var7[var10 + 1] = var14 * var1;
            var8[var11 + 0] = 1.0F - var6 * (float)var17;
            var8[var11 + 1] = var16;
            var10 += 3;
            var11 += 2;
         }

         var8[var11 + 0] = 0.0F;
         var8[var11 + 1] = var16;
         var11 += 2;
      }

      var7[var10 + 0] = 0.0F;
      var7[var10 + 1] = -var1;
      var7[var10 + 2] = 0.0F;
      var7[var10 + 3] = 0.0F;
      var7[var10 + 4] = var1;
      var7[var10 + 5] = 0.0F;
      var10 += 6;
      var12 = (var2 - 1) * var0;
      var13 = 0.00390625F;

      int var25;
      for(var25 = 0; var25 < var0; ++var25) {
         var8[var11 + 0] = 1.0F - var6 * (0.5F + (float)var25);
         var8[var11 + 1] = var13;
         var11 += 2;
      }

      for(var25 = 0; var25 < var0; ++var25) {
         var8[var11 + 0] = 1.0F - var6 * (0.5F + (float)var25);
         var8[var11 + 1] = 1.0F - var13;
         var11 += 2;
      }

      var25 = 0;

      int var19;
      int var22;
      int var23;
      int var26;
      int var27;
      int var28;
      int var30;
      int var31;
      for(var26 = 0; var26 < var2 - 2; ++var26) {
         for(var27 = 0; var27 < var0; ++var27) {
            var17 = var26 * var0 + var27;
            var28 = var17 + 1;
            var19 = var17 + var0;
            var30 = var28 + var0;
            var31 = var17 + var26;
            var22 = var31 + 1;
            var23 = var31 + var0 + 1;
            int var24 = var22 + var0 + 1;
            var9[var25 + 0] = var17;
            var9[var25 + 1] = var31;
            var9[var25 + 2] = var28 % var0 == 0 ? var28 - var0 : var28;
            var9[var25 + 3] = var22;
            var9[var25 + 4] = var19;
            var9[var25 + 5] = var23;
            var25 += 6;
            var9[var25 + 0] = var30 % var0 == 0 ? var30 - var0 : var30;
            var9[var25 + 1] = var24;
            var9[var25 + 2] = var19;
            var9[var25 + 3] = var23;
            var9[var25 + 4] = var28 % var0 == 0 ? var28 - var0 : var28;
            var9[var25 + 5] = var22;
            var25 += 6;
         }
      }

      var26 = var12;
      var27 = (var2 - 1) * (var0 + 1);

      for(var17 = 0; var17 < var0; ++var17) {
         var19 = var17 + 1;
         var30 = var27 + var17;
         var9[var25 + 0] = var26;
         var9[var25 + 1] = var30;
         var9[var25 + 2] = var19 == var0 ? 0 : var19;
         var9[var25 + 3] = var19;
         var9[var25 + 4] = var17;
         var9[var25 + 5] = var17;
         var25 += 6;
      }

      ++var26;
      var27 += var0;
      var17 = (var2 - 2) * var0;

      for(var28 = 0; var28 < var0; ++var28) {
         var19 = var17 + var28;
         var30 = var17 + var28 + 1;
         var31 = var27 + var28;
         var22 = (var2 - 2) * (var0 + 1) + var28;
         var23 = var22 + 1;
         var9[var25 + 0] = var26;
         var9[var25 + 1] = var31;
         var9[var25 + 2] = var19;
         var9[var25 + 3] = var22;
         var9[var25 + 4] = var30 % var0 == 0 ? var30 - var0 : var30;
         var9[var25 + 5] = var23;
         var25 += 6;
      }

      TriangleMesh var29 = new TriangleMesh(true);
      var29.getPoints().setAll(var7);
      var29.getTexCoords().setAll(var8);
      var29.getFaces().setAll(var9);
      return var29;
   }

   private static int generateKey(float var0, int var1) {
      int var2 = 5;
      var2 = 23 * var2 + Float.floatToIntBits(var0);
      var2 = 23 * var2 + var1;
      return var2;
   }
}
