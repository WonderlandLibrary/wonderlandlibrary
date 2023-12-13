package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Vec3d;

public class TransformHelper {
   private TransformHelper() {
   }

   public static BaseBounds general3dBoundsTransform(CanTransformVec3d var0, BaseBounds var1, BaseBounds var2, Vec3d var3) {
      if (var3 == null) {
         var3 = new Vec3d();
      }

      double var4 = (double)var1.getMinX();
      double var6 = (double)var1.getMinY();
      double var8 = (double)var1.getMinZ();
      double var10 = (double)var1.getMaxX();
      double var12 = (double)var1.getMaxY();
      double var14 = (double)var1.getMaxZ();
      var3.set(var10, var12, var14);
      var3 = var0.transform(var3, var3);
      double var16 = var3.x;
      double var18 = var3.y;
      double var20 = var3.z;
      double var22 = var3.x;
      double var24 = var3.y;
      double var26 = var3.z;
      var3.set(var4, var12, var14);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      var3.set(var4, var6, var14);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      var3.set(var10, var6, var14);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      var3.set(var4, var12, var8);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      var3.set(var10, var12, var8);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      var3.set(var4, var6, var8);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      var3.set(var10, var6, var8);
      var3 = var0.transform(var3, var3);
      if (var3.x > var22) {
         var22 = var3.x;
      }

      if (var3.y > var24) {
         var24 = var3.y;
      }

      if (var3.z > var26) {
         var26 = var3.z;
      }

      if (var3.x < var16) {
         var16 = var3.x;
      }

      if (var3.y < var18) {
         var18 = var3.y;
      }

      if (var3.z < var20) {
         var20 = var3.z;
      }

      return var2.deriveWithNewBounds((float)var16, (float)var18, (float)var20, (float)var22, (float)var24, (float)var26);
   }
}
