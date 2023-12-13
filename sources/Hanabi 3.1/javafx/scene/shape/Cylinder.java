package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGCylinder;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public class Cylinder extends Shape3D {
   static final int DEFAULT_DIVISIONS = 64;
   static final double DEFAULT_RADIUS = 1.0;
   static final double DEFAULT_HEIGHT = 2.0;
   private int divisions;
   private TriangleMesh mesh;
   private DoubleProperty height;
   private DoubleProperty radius;

   public Cylinder() {
      this(1.0, 2.0, 64);
   }

   public Cylinder(double var1, double var3) {
      this(var1, var3, 64);
   }

   public Cylinder(double var1, double var3, int var5) {
      this.divisions = 64;
      this.divisions = var5 < 3 ? 3 : var5;
      this.setRadius(var1);
      this.setHeight(var3);
   }

   public final void setHeight(double var1) {
      this.heightProperty().set(var1);
   }

   public final double getHeight() {
      return this.height == null ? 2.0 : this.height.get();
   }

   public final DoubleProperty heightProperty() {
      if (this.height == null) {
         this.height = new SimpleDoubleProperty(this, "height", 2.0) {
            public void invalidated() {
               Cylinder.this.impl_markDirty(DirtyBits.MESH_GEOM);
               Cylinder.this.manager.invalidateCylinderMesh(Cylinder.this.key);
               Cylinder.this.key = 0;
               Cylinder.this.impl_geomChanged();
            }
         };
      }

      return this.height;
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
               Cylinder.this.impl_markDirty(DirtyBits.MESH_GEOM);
               Cylinder.this.manager.invalidateCylinderMesh(Cylinder.this.key);
               Cylinder.this.key = 0;
               Cylinder.this.impl_geomChanged();
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
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.MESH_GEOM)) {
         NGCylinder var1 = (NGCylinder)this.impl_getPeer();
         float var2 = (float)this.getHeight();
         float var3 = (float)this.getRadius();
         if (!(var2 < 0.0F) && !(var3 < 0.0F)) {
            if (this.key == 0) {
               this.key = generateKey(var2, var3, this.divisions);
            }

            this.mesh = this.manager.getCylinderMesh(var2, var3, this.divisions, this.key);
            this.mesh.impl_updatePG();
            var1.updateMesh(this.mesh.impl_getPGTriangleMesh());
         } else {
            var1.updateMesh((NGTriangleMesh)null);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGCylinder();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      float var3 = (float)this.getHeight();
      float var4 = (float)this.getRadius();
      if (!(var4 < 0.0F) && !(var3 < 0.0F)) {
         float var5 = var3 * 0.5F;
         var1 = var1.deriveWithNewBounds(-var4, -var5, -var4, var4, var5, var4);
         var1 = var2.transform(var1, var1);
         return var1;
      } else {
         return var1.makeEmpty();
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      double var5 = this.getRadius();
      double var7 = this.getHeight() * 0.5;
      return -var5 <= var1 && var1 <= var5 && -var7 <= var3 && var3 <= var7;
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
      double var20 = this.getHeight();
      double var22 = var20 / 2.0;
      CullFace var24 = this.getCullFace();
      double var25 = var7 * var7 + var11 * var11;
      double var27 = 2.0 * (var7 * var14 + var11 * var18);
      double var29 = var14 * var14 + var18 * var18 - var4 * var4;
      double var31 = var27 * var27 - 4.0 * var25 * var29;
      double var37 = Double.POSITIVE_INFINITY;
      double var39 = var1.getNearClip();
      double var41 = var1.getFarClip();
      double var33;
      double var35;
      double var45;
      double var47;
      double var49;
      if (var31 >= 0.0 && (var7 != 0.0 || var11 != 0.0)) {
         double var43 = Math.sqrt(var31);
         var45 = var27 < 0.0 ? (-var27 - var43) / 2.0 : (-var27 + var43) / 2.0;
         var33 = var45 / var25;
         var35 = var29 / var45;
         if (var33 > var35) {
            var47 = var33;
            var33 = var35;
            var35 = var47;
         }

         var47 = var16 + var33 * var9;
         if (!(var33 < var39) && !(var47 < -var22) && !(var47 > var22) && var24 != CullFace.FRONT) {
            if (var33 <= var41) {
               var37 = var33;
            }
         } else {
            var49 = var16 + var35 * var9;
            if (var35 >= var39 && var35 <= var41 && var49 >= -var22 && var49 <= var22 && (var24 != CullFace.BACK || var3)) {
               var37 = var35;
            }
         }
      }

      boolean var54 = false;
      boolean var44 = false;
      if (var37 == Double.POSITIVE_INFINITY || !var3) {
         var45 = (-var22 - var16) / var9;
         var47 = (var22 - var16) / var9;
         boolean var57 = false;
         if (var45 < var47) {
            var33 = var45;
            var35 = var47;
            var57 = true;
         } else {
            var33 = var47;
            var35 = var45;
         }

         double var50;
         double var52;
         if (var33 >= var39 && var33 <= var41 && var33 < var37 && var24 != CullFace.FRONT) {
            var50 = var14 + var7 * var33;
            var52 = var18 + var11 * var33;
            if (var50 * var50 + var52 * var52 <= var4 * var4) {
               var44 = var57;
               var54 = !var57;
               var37 = var33;
            }
         }

         if (var35 >= var39 && var35 <= var41 && var35 < var37 && (var24 != CullFace.BACK || var3)) {
            var50 = var14 + var7 * var35;
            var52 = var18 + var11 * var35;
            if (var50 * var50 + var52 * var52 <= var4 * var4) {
               var54 = var57;
               var44 = !var57;
               var37 = var35;
            }
         }
      }

      if (!Double.isInfinite(var37) && !Double.isNaN(var37)) {
         if (var3) {
            return this.mesh.impl_computeIntersects(var1, var2, this, var24, false);
         } else {
            if (var2 != null && var2.isCloser(var37)) {
               Point3D var55 = PickResultChooser.computePoint(var1, var37);
               Point2D var46;
               if (var54) {
                  var46 = new Point2D(0.5 + var55.getX() / (2.0 * var4), 0.5 + var55.getZ() / (2.0 * var4));
               } else if (var44) {
                  var46 = new Point2D(0.5 + var55.getX() / (2.0 * var4), 0.5 - var55.getZ() / (2.0 * var4));
               } else {
                  Point3D var56 = new Point3D(var55.getX(), 0.0, var55.getZ());
                  Point3D var48 = var56.crossProduct(Rotate.Z_AXIS);
                  var49 = var56.angle(Rotate.Z_AXIS);
                  if (var48.getY() > 0.0) {
                     var49 = 360.0 - var49;
                  }

                  var46 = new Point2D(1.0 - var49 / 360.0, 0.5 + var55.getY() / var20);
               }

               var2.offer(this, var37, -1, var55, var46);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   static TriangleMesh createMesh(int var0, float var1, float var2) {
      int var3 = var0 * 2 + 2;
      int var4 = (var0 + 1) * 4 + 1;
      int var5 = var0 * 4;
      float var6 = 0.00390625F;
      float var7 = 1.0F / (float)var0;
      var1 *= 0.5F;
      float[] var8 = new float[var3 * 3];
      float[] var9 = new float[var4 * 2];
      int[] var10 = new int[var5 * 6];
      int[] var11 = new int[var5];
      int var12 = 0;
      int var13 = 0;

      int var14;
      double var15;
      for(var14 = 0; var14 < var0; ++var14) {
         var15 = (double)(var7 * (float)var14 * 2.0F) * Math.PI;
         var8[var12 + 0] = (float)(Math.sin(var15) * (double)var2);
         var8[var12 + 2] = (float)(Math.cos(var15) * (double)var2);
         var8[var12 + 1] = var1;
         var9[var13 + 0] = 1.0F - var7 * (float)var14;
         var9[var13 + 1] = 1.0F - var6;
         var12 += 3;
         var13 += 2;
      }

      var9[var13 + 0] = 0.0F;
      var9[var13 + 1] = 1.0F - var6;
      var13 += 2;

      for(var14 = 0; var14 < var0; ++var14) {
         var15 = (double)(var7 * (float)var14 * 2.0F) * Math.PI;
         var8[var12 + 0] = (float)(Math.sin(var15) * (double)var2);
         var8[var12 + 2] = (float)(Math.cos(var15) * (double)var2);
         var8[var12 + 1] = -var1;
         var9[var13 + 0] = 1.0F - var7 * (float)var14;
         var9[var13 + 1] = var6;
         var12 += 3;
         var13 += 2;
      }

      var9[var13 + 0] = 0.0F;
      var9[var13 + 1] = var6;
      var13 += 2;
      var8[var12 + 0] = 0.0F;
      var8[var12 + 1] = var1;
      var8[var12 + 2] = 0.0F;
      var8[var12 + 3] = 0.0F;
      var8[var12 + 4] = -var1;
      var8[var12 + 5] = 0.0F;
      var12 += 6;

      for(var14 = 0; var14 <= var0; ++var14) {
         var15 = var14 < var0 ? (double)(var7 * (float)var14 * 2.0F) * Math.PI : 0.0;
         var9[var13 + 0] = (float)(Math.sin(var15) * 0.5) + 0.5F;
         var9[var13 + 1] = (float)(Math.cos(var15) * 0.5) + 0.5F;
         var13 += 2;
      }

      for(var14 = 0; var14 <= var0; ++var14) {
         var15 = var14 < var0 ? (double)(var7 * (float)var14 * 2.0F) * Math.PI : 0.0;
         var9[var13 + 0] = 0.5F + (float)(Math.sin(var15) * 0.5);
         var9[var13 + 1] = 0.5F - (float)(Math.cos(var15) * 0.5);
         var13 += 2;
      }

      var9[var13 + 0] = 0.5F;
      var9[var13 + 1] = 0.5F;
      var13 += 2;
      var14 = 0;

      int var16;
      int var17;
      int var18;
      int var22;
      for(var22 = 0; var22 < var0; ++var22) {
         var16 = var22 + 1;
         var17 = var22 + var0;
         var18 = var16 + var0;
         var10[var14 + 0] = var22;
         var10[var14 + 1] = var22;
         var10[var14 + 2] = var17;
         var10[var14 + 3] = var17 + 1;
         var10[var14 + 4] = var16 == var0 ? 0 : var16;
         var10[var14 + 5] = var16;
         var14 += 6;
         var10[var14 + 0] = var18 % var0 == 0 ? var18 - var0 : var18;
         var10[var14 + 1] = var18 + 1;
         var10[var14 + 2] = var16 == var0 ? 0 : var16;
         var10[var14 + 3] = var16;
         var10[var14 + 4] = var17;
         var10[var14 + 5] = var17 + 1;
         var14 += 6;
      }

      var22 = (var0 + 1) * 2;
      var16 = (var0 + 1) * 4;
      var17 = var0 * 2;

      int var19;
      int var20;
      int var21;
      for(var18 = 0; var18 < var0; ++var18) {
         var19 = var18 + 1;
         var20 = var22 + var18;
         var21 = var20 + 1;
         var10[var14 + 0] = var18;
         var10[var14 + 1] = var20;
         var10[var14 + 2] = var19 == var0 ? 0 : var19;
         var10[var14 + 3] = var21;
         var10[var14 + 4] = var17;
         var10[var14 + 5] = var16;
         var14 += 6;
      }

      var17 = var0 * 2 + 1;
      var22 = (var0 + 1) * 3;

      for(var18 = 0; var18 < var0; ++var18) {
         var19 = var18 + 1 + var0;
         var20 = var22 + var18;
         var21 = var20 + 1;
         var10[var14 + 0] = var18 + var0;
         var10[var14 + 1] = var20;
         var10[var14 + 2] = var17;
         var10[var14 + 3] = var16;
         var10[var14 + 4] = var19 % var0 == 0 ? var19 - var0 : var19;
         var10[var14 + 5] = var21;
         var14 += 6;
      }

      for(var18 = 0; var18 < var0 * 2; ++var18) {
         var11[var18] = 1;
      }

      for(var18 = var0 * 2; var18 < var0 * 4; ++var18) {
         var11[var18] = 2;
      }

      TriangleMesh var23 = new TriangleMesh(true);
      var23.getPoints().setAll(var8);
      var23.getTexCoords().setAll(var9);
      var23.getFaces().setAll(var10);
      var23.getFaceSmoothingGroups().setAll(var11);
      return var23;
   }

   private static int generateKey(float var0, float var1, int var2) {
      int var3 = 7;
      var3 = 47 * var3 + Float.floatToIntBits(var0);
      var3 = 47 * var3 + Float.floatToIntBits(var1);
      var3 = 47 * var3 + var2;
      return var3;
   }
}
