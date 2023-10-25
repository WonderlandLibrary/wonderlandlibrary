/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.application.Platform
 *  javafx.geometry.Point2D
 *  javafx.geometry.Point3D
 *  javafx.scene.Node
 *  javafx.scene.SubScene
 *  javafx.scene.input.PickResult
 */
package com.sun.javafx.scene.input;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SubSceneHelper;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.PickResult;

public class PickResultChooser {
    private double distance = Double.POSITIVE_INFINITY;
    private Node node;
    private int face = -1;
    private Point3D point;
    private Point3D normal;
    private Point2D texCoord;
    private boolean empty = true;
    private boolean closed = false;

    public static Point3D computePoint(PickRay pickRay, double d) {
        Vec3d vec3d = pickRay.getOriginNoClone();
        Vec3d vec3d2 = pickRay.getDirectionNoClone();
        return new Point3D(vec3d.x + vec3d2.x * d, vec3d.y + vec3d2.y * d, vec3d.z + vec3d2.z * d);
    }

    public PickResult toPickResult() {
        if (this.empty) {
            return null;
        }
        return new PickResult(this.node, this.point, this.distance, this.face, this.normal, this.texCoord);
    }

    public boolean isCloser(double d) {
        return d < this.distance || this.empty;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean offer(Node node, double d, int n, Point3D point3D, Point2D point2D) {
        return this.processOffer(node, node, d, point3D, n, this.normal, point2D);
    }

    public boolean offer(Node node, double d, Point3D point3D) {
        return this.processOffer(node, node, d, point3D, -1, null, null);
    }

    public boolean offerSubScenePickResult(SubScene subScene, PickResult pickResult, double d) {
        if (pickResult == null) {
            return false;
        }
        return this.processOffer(pickResult.getIntersectedNode(), (Node)subScene, d, pickResult.getIntersectedPoint(), pickResult.getIntersectedFace(), pickResult.getIntersectedNormal(), pickResult.getIntersectedTexCoord());
    }

    private boolean processOffer(Node node, Node node2, double d, Point3D point3D, int n, Point3D point3D2, Point2D point2D) {
        SubScene subScene = NodeHelper.getSubScene(node2);
        boolean bl = Platform.isSupported((ConditionalFeature)ConditionalFeature.SCENE3D) ? (subScene != null ? SubSceneHelper.isDepthBuffer(subScene) : node2.getScene().isDepthBuffer()) : false;
        boolean bl2 = bl && NodeHelper.isDerivedDepthTest(node2);
        boolean bl3 = false;
        if ((this.empty || bl2 && d < this.distance) && !this.closed) {
            this.node = node;
            this.distance = d;
            this.face = n;
            this.point = point3D;
            this.normal = point3D2;
            this.texCoord = point2D;
            this.empty = false;
            bl3 = true;
        }
        if (!bl2) {
            this.closed = true;
        }
        return bl3;
    }

    public final Node getIntersectedNode() {
        return this.node;
    }

    public final double getIntersectedDistance() {
        return this.distance;
    }

    public final int getIntersectedFace() {
        return this.face;
    }

    public final Point3D getIntersectedPoint() {
        return this.point;
    }

    public final Point3D getIntersectedNormal() {
        return this.normal;
    }

    public final Point2D getIntersectedTexCoord() {
        return this.texCoord;
    }
}

