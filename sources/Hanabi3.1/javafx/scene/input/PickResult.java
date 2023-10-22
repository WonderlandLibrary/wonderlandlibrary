package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;

public class PickResult {
   public static final int FACE_UNDEFINED = -1;
   private Node node;
   private Point3D point;
   private double distance;
   private int face;
   private Point3D normal;
   private Point2D texCoord;

   public PickResult(@NamedArg("node") Node var1, @NamedArg("point") Point3D var2, @NamedArg("distance") double var3, @NamedArg("face") int var5, @NamedArg("texCoord") Point2D var6) {
      this.distance = Double.POSITIVE_INFINITY;
      this.face = -1;
      this.node = var1;
      this.point = var2;
      this.distance = var3;
      this.face = var5;
      this.normal = null;
      this.texCoord = var6;
   }

   public PickResult(@NamedArg("node") Node var1, @NamedArg("point") Point3D var2, @NamedArg("distance") double var3, @NamedArg("face") int var5, @NamedArg("normal") Point3D var6, @NamedArg("texCoord") Point2D var7) {
      this.distance = Double.POSITIVE_INFINITY;
      this.face = -1;
      this.node = var1;
      this.point = var2;
      this.distance = var3;
      this.face = var5;
      this.normal = var6;
      this.texCoord = var7;
   }

   public PickResult(@NamedArg("node") Node var1, @NamedArg("point") Point3D var2, @NamedArg("distance") double var3) {
      this.distance = Double.POSITIVE_INFINITY;
      this.face = -1;
      this.node = var1;
      this.point = var2;
      this.distance = var3;
      this.face = -1;
      this.normal = null;
      this.texCoord = null;
   }

   public PickResult(@NamedArg("target") EventTarget var1, @NamedArg("sceneX") double var2, @NamedArg("sceneY") double var4) {
      this(var1 instanceof Node ? (Node)var1 : null, var1 instanceof Node ? ((Node)var1).sceneToLocal(var2, var4, 0.0) : new Point3D(var2, var4, 0.0), 1.0);
   }

   public final Node getIntersectedNode() {
      return this.node;
   }

   public final Point3D getIntersectedPoint() {
      return this.point;
   }

   public final double getIntersectedDistance() {
      return this.distance;
   }

   public final int getIntersectedFace() {
      return this.face;
   }

   public final Point3D getIntersectedNormal() {
      return this.normal;
   }

   public final Point2D getIntersectedTexCoord() {
      return this.texCoord;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("PickResult [");
      var1.append("node = ").append(this.getIntersectedNode()).append(", point = ").append(this.getIntersectedPoint()).append(", distance = ").append(this.getIntersectedDistance());
      if (this.getIntersectedFace() != -1) {
         var1.append(", face = ").append(this.getIntersectedFace());
      }

      if (this.getIntersectedNormal() != null) {
         var1.append(", normal = ").append(this.getIntersectedNormal());
      }

      if (this.getIntersectedTexCoord() != null) {
         var1.append(", texCoord = ").append(this.getIntersectedTexCoord());
      }

      return var1.toString();
   }
}
