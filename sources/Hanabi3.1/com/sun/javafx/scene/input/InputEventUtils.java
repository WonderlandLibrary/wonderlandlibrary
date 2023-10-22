package com.sun.javafx.scene.input;

import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;
import com.sun.javafx.scene.SubSceneHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;

public class InputEventUtils {
   private static final List TM_ANY;
   private static final List TM_COPY_OR_MOVE;

   public static Point3D recomputeCoordinates(PickResult var0, Object var1) {
      Point3D var2 = var0.getIntersectedPoint();
      if (var2 == null) {
         return new Point3D(Double.NaN, Double.NaN, Double.NaN);
      } else {
         Node var3 = var0.getIntersectedNode();
         Node var4 = var1 instanceof Node ? (Node)var1 : null;
         SubScene var5 = var3 == null ? null : NodeHelper.getSubScene(var3);
         SubScene var6 = var4 == null ? null : NodeHelper.getSubScene(var4);
         boolean var7 = var5 != var6;
         if (var3 != null) {
            var2 = var3.localToScene(var2);
            if (var7 && var5 != null) {
               var2 = SceneUtils.subSceneToScene(var5, var2);
            }
         }

         if (var4 != null) {
            if (var7 && var6 != null) {
               Point2D var8 = CameraHelper.project(SceneHelper.getEffectiveCamera(var4.getScene()), var2);
               var8 = SceneUtils.sceneToSubScenePlane(var6, var8);
               if (var8 == null) {
                  var2 = null;
               } else {
                  var2 = CameraHelper.pickProjectPlane(SubSceneHelper.getEffectiveCamera(var6), var8.getX(), var8.getY());
               }
            }

            if (var2 != null) {
               var2 = var4.sceneToLocal(var2);
            }

            if (var2 == null) {
               var2 = new Point3D(Double.NaN, Double.NaN, Double.NaN);
            }
         }

         return var2;
      }
   }

   public static List safeTransferModes(TransferMode[] var0) {
      if (var0 == TransferMode.ANY) {
         return TM_ANY;
      } else {
         return var0 == TransferMode.COPY_OR_MOVE ? TM_COPY_OR_MOVE : Arrays.asList(var0);
      }
   }

   static {
      TM_ANY = Collections.unmodifiableList(Arrays.asList(TransferMode.COPY, TransferMode.MOVE, TransferMode.LINK));
      TM_COPY_OR_MOVE = Collections.unmodifiableList(Arrays.asList(TransferMode.COPY, TransferMode.MOVE));
   }
}
