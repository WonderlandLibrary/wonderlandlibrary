package com.sun.javafx.geometry;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public final class BoundsUtils {
   private BoundsUtils() {
   }

   private static double min4(double var0, double var2, double var4, double var6) {
      return Math.min(Math.min(var0, var2), Math.min(var4, var6));
   }

   private static double min8(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      return Math.min(min4(var0, var2, var4, var6), min4(var8, var10, var12, var14));
   }

   private static double max4(double var0, double var2, double var4, double var6) {
      return Math.max(Math.max(var0, var2), Math.max(var4, var6));
   }

   private static double max8(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      return Math.max(max4(var0, var2, var4, var6), max4(var8, var10, var12, var14));
   }

   public static Bounds createBoundingBox(Point2D var0, Point2D var1, Point2D var2, Point2D var3, Point2D var4, Point2D var5, Point2D var6, Point2D var7) {
      if (var0 != null && var1 != null && var2 != null && var3 != null && var4 != null && var5 != null && var6 != null && var7 != null) {
         double var8 = min8(var0.getX(), var1.getX(), var2.getX(), var3.getX(), var4.getX(), var5.getX(), var6.getX(), var7.getX());
         double var10 = max8(var0.getX(), var1.getX(), var2.getX(), var3.getX(), var4.getX(), var5.getX(), var6.getX(), var7.getX());
         double var12 = min8(var0.getY(), var1.getY(), var2.getY(), var3.getY(), var4.getY(), var5.getY(), var6.getY(), var7.getY());
         double var14 = max8(var0.getY(), var1.getY(), var2.getY(), var3.getY(), var4.getY(), var5.getY(), var6.getY(), var7.getY());
         return new BoundingBox(var8, var12, var10 - var8, var14 - var12);
      } else {
         return null;
      }
   }

   public static Bounds createBoundingBox(Point3D var0, Point3D var1, Point3D var2, Point3D var3, Point3D var4, Point3D var5, Point3D var6, Point3D var7) {
      if (var0 != null && var1 != null && var2 != null && var3 != null && var4 != null && var5 != null && var6 != null && var7 != null) {
         double var8 = min8(var0.getX(), var1.getX(), var2.getX(), var3.getX(), var4.getX(), var5.getX(), var6.getX(), var7.getX());
         double var10 = max8(var0.getX(), var1.getX(), var2.getX(), var3.getX(), var4.getX(), var5.getX(), var6.getX(), var7.getX());
         double var12 = min8(var0.getY(), var1.getY(), var2.getY(), var3.getY(), var4.getY(), var5.getY(), var6.getY(), var7.getY());
         double var14 = max8(var0.getY(), var1.getY(), var2.getY(), var3.getY(), var4.getY(), var5.getY(), var6.getY(), var7.getY());
         double var16 = min8(var0.getZ(), var1.getZ(), var2.getZ(), var3.getZ(), var4.getZ(), var5.getZ(), var6.getZ(), var7.getZ());
         double var18 = max8(var0.getZ(), var1.getZ(), var2.getZ(), var3.getZ(), var4.getZ(), var5.getZ(), var6.getZ(), var7.getZ());
         return new BoundingBox(var8, var12, var16, var10 - var8, var14 - var12, var18 - var16);
      } else {
         return null;
      }
   }

   public static Bounds createBoundingBox(Point2D var0, Point2D var1, Point2D var2, Point2D var3) {
      if (var0 != null && var1 != null && var2 != null && var3 != null) {
         double var4 = min4(var0.getX(), var1.getX(), var2.getX(), var3.getX());
         double var6 = max4(var0.getX(), var1.getX(), var2.getX(), var3.getX());
         double var8 = min4(var0.getY(), var1.getY(), var2.getY(), var3.getY());
         double var10 = max4(var0.getY(), var1.getY(), var2.getY(), var3.getY());
         return new BoundingBox(var4, var8, var6 - var4, var10 - var8);
      } else {
         return null;
      }
   }
}
