package com.sun.javafx.scene;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;

public class CameraHelper {
   private static CameraAccessor cameraAccessor;

   private CameraHelper() {
   }

   public static Point2D project(Camera var0, Point3D var1) {
      return cameraAccessor.project(var0, var1);
   }

   public static Point2D pickNodeXYPlane(Camera var0, Node var1, double var2, double var4) {
      return cameraAccessor.pickNodeXYPlane(var0, var1, var2, var4);
   }

   public static Point3D pickProjectPlane(Camera var0, double var1, double var3) {
      return cameraAccessor.pickProjectPlane(var0, var1, var3);
   }

   public static void setCameraAccessor(CameraAccessor var0) {
      if (cameraAccessor != null) {
         throw new IllegalStateException();
      } else {
         cameraAccessor = var0;
      }
   }

   private static void forceInit(Class var0) {
      try {
         Class.forName(var0.getName(), true, var0.getClassLoader());
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   static {
      forceInit(Camera.class);
   }

   public interface CameraAccessor {
      Point2D project(Camera var1, Point3D var2);

      Point2D pickNodeXYPlane(Camera var1, Node var2, double var3, double var5);

      Point3D pickProjectPlane(Camera var1, double var2, double var4);
   }
}
