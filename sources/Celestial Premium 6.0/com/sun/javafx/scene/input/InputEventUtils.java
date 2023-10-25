/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point2D
 *  javafx.geometry.Point3D
 *  javafx.scene.Node
 *  javafx.scene.SubScene
 *  javafx.scene.input.PickResult
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.scene.input;

import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;
import com.sun.javafx.scene.SubSceneHelper;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;

public class InputEventUtils {
    private static final List<TransferMode> TM_ANY = List.of(TransferMode.COPY, TransferMode.MOVE, TransferMode.LINK);
    private static final List<TransferMode> TM_COPY_OR_MOVE = List.of(TransferMode.COPY, TransferMode.MOVE);

    public static Point3D recomputeCoordinates(PickResult pickResult, Object object) {
        boolean bl;
        Object object2 = pickResult.getIntersectedPoint();
        if (object2 == null) {
            return new Point3D(Double.NaN, Double.NaN, Double.NaN);
        }
        Node node = pickResult.getIntersectedNode();
        Node node2 = object instanceof Node ? (Node)object : null;
        SubScene subScene = node == null ? null : NodeHelper.getSubScene(node);
        SubScene subScene2 = node2 == null ? null : NodeHelper.getSubScene(node2);
        boolean bl2 = bl = subScene != subScene2;
        if (node != null) {
            object2 = node.localToScene(object2);
            if (bl && subScene != null) {
                object2 = SceneUtils.subSceneToScene(subScene, object2);
            }
        }
        if (node2 != null) {
            if (bl && subScene2 != null) {
                Point2D point2D = CameraHelper.project(SceneHelper.getEffectiveCamera(node2.getScene()), object2);
                object2 = (point2D = SceneUtils.sceneToSubScenePlane(subScene2, point2D)) == null ? null : CameraHelper.pickProjectPlane(SubSceneHelper.getEffectiveCamera(subScene2), point2D.getX(), point2D.getY());
            }
            if (object2 != null) {
                object2 = node2.sceneToLocal(object2);
            }
            if (object2 == null) {
                object2 = new Point3D(Double.NaN, Double.NaN, Double.NaN);
            }
        }
        return object2;
    }

    public static List<TransferMode> safeTransferModes(TransferMode[] arrtransferMode) {
        if (arrtransferMode == TransferMode.ANY) {
            return TM_ANY;
        }
        if (arrtransferMode == TransferMode.COPY_OR_MOVE) {
            return TM_COPY_OR_MOVE;
        }
        return Arrays.asList(arrtransferMode);
    }
}

