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

   public static Point3D computePoint(PickRay var0, double var1) {
      Vec3d var3 = var0.getOriginNoClone();
      Vec3d var4 = var0.getDirectionNoClone();
      return new Point3D(var3.x + var4.x * var1, var3.y + var4.y * var1, var3.z + var4.z * var1);
   }

   public PickResult toPickResult() {
      return this.empty ? null : new PickResult(this.node, this.point, this.distance, this.face, this.normal, this.texCoord);
   }

   public boolean isCloser(double var1) {
      return var1 < this.distance || this.empty;
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public boolean offer(Node var1, double var2, int var4, Point3D var5, Point2D var6) {
      return this.processOffer(var1, var1, var2, var5, var4, this.normal, var6);
   }

   public boolean offer(Node var1, double var2, Point3D var4) {
      return this.processOffer(var1, var1, var2, var4, -1, (Point3D)null, (Point2D)null);
   }

   public boolean offerSubScenePickResult(SubScene var1, PickResult var2, double var3) {
      return var2 == null ? false : this.processOffer(var2.getIntersectedNode(), var1, var3, var2.getIntersectedPoint(), var2.getIntersectedFace(), var2.getIntersectedNormal(), var2.getIntersectedTexCoord());
   }

   private boolean processOffer(Node var1, Node var2, double var3, Point3D var5, int var6, Point3D var7, Point2D var8) {
      SubScene var9 = NodeHelper.getSubScene(var2);
      boolean var10 = Platform.isSupported(ConditionalFeature.SCENE3D) ? (var9 != null ? SubSceneHelper.isDepthBuffer(var9) : var2.getScene().isDepthBuffer()) : false;
      boolean var11 = var10 && NodeHelper.isDerivedDepthTest(var2);
      boolean var12 = false;
      if ((this.empty || var11 && var3 < this.distance) && !this.closed) {
         this.node = var1;
         this.distance = var3;
         this.face = var6;
         this.point = var5;
         this.normal = var7;
         this.texCoord = var8;
         this.empty = false;
         var12 = true;
      }

      if (!var11) {
         this.closed = true;
      }

      return var12;
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
