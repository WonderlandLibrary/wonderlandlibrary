package com.sun.javafx.scene;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.SubScene;

public class SceneUtils {
   public static Point3D subSceneToScene(SubScene var0, Point3D var1) {
      for(SubScene var2 = var0; var2 != null; var2 = NodeHelper.getSubScene(var2)) {
         Point2D var3 = CameraHelper.project(SubSceneHelper.getEffectiveCamera(var0), var1);
         var1 = var2.localToScene(var3.getX(), var3.getY(), 0.0);
      }

      return var1;
   }

   public static Point2D sceneToSubScenePlane(SubScene var0, Point2D var1) {
      var1 = computeSubSceneCoordinates(var1.getX(), var1.getY(), var0);
      return var1;
   }

   private static Point2D computeSubSceneCoordinates(double var0, double var2, SubScene var4) {
      SubScene var5 = NodeHelper.getSubScene(var4);
      if (var5 == null) {
         return CameraHelper.pickNodeXYPlane(SceneHelper.getEffectiveCamera(var4.getScene()), var4, var0, var2);
      } else {
         Point2D var6 = computeSubSceneCoordinates(var0, var2, var5);
         if (var6 != null) {
            var6 = CameraHelper.pickNodeXYPlane(SubSceneHelper.getEffectiveCamera(var5), var4, var6.getX(), var6.getY());
         }

         return var6;
      }
   }
}
