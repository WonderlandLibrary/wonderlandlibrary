package javafx.scene.shape;

import com.sun.javafx.collections.FloatArraySyncer;
import com.sun.javafx.collections.IntegerArraySyncer;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.shape.ObservableFaceArrayImpl;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ArrayChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import sun.util.logging.PlatformLogger;

public class TriangleMesh extends Mesh {
   private final ObservableFloatArray points;
   private final ObservableFloatArray normals;
   private final ObservableFloatArray texCoords;
   private final ObservableFaceArray faces;
   private final ObservableIntegerArray faceSmoothingGroups;
   private final Listener pointsSyncer;
   private final Listener normalsSyncer;
   private final Listener texCoordsSyncer;
   private final Listener facesSyncer;
   private final Listener faceSmoothingGroupsSyncer;
   private final boolean isPredefinedShape;
   private boolean isValidDirty;
   private boolean isPointsValid;
   private boolean isNormalsValid;
   private boolean isTexCoordsValid;
   private boolean isFacesValid;
   private boolean isFaceSmoothingGroupValid;
   private int refCount;
   private BaseBounds cachedBounds;
   private ObjectProperty vertexFormat;
   private NGTriangleMesh peer;

   public TriangleMesh() {
      this(false);
   }

   public TriangleMesh(VertexFormat var1) {
      this(false);
      this.setVertexFormat(var1);
   }

   TriangleMesh(boolean var1) {
      this.points = FXCollections.observableFloatArray();
      this.normals = FXCollections.observableFloatArray();
      this.texCoords = FXCollections.observableFloatArray();
      this.faces = new ObservableFaceArrayImpl();
      this.faceSmoothingGroups = FXCollections.observableIntegerArray();
      this.pointsSyncer = new Listener(this.points);
      this.normalsSyncer = new Listener(this.normals);
      this.texCoordsSyncer = new Listener(this.texCoords);
      this.facesSyncer = new Listener(this.faces);
      this.faceSmoothingGroupsSyncer = new Listener(this.faceSmoothingGroups);
      this.isValidDirty = true;
      this.refCount = 1;
      this.isPredefinedShape = var1;
      if (var1) {
         this.isPointsValid = true;
         this.isNormalsValid = true;
         this.isTexCoordsValid = true;
         this.isFacesValid = true;
         this.isFaceSmoothingGroupValid = true;
      } else {
         this.isPointsValid = false;
         this.isNormalsValid = false;
         this.isTexCoordsValid = false;
         this.isFacesValid = false;
         this.isFaceSmoothingGroupValid = false;
      }

   }

   public final void setVertexFormat(VertexFormat var1) {
      this.vertexFormatProperty().set(var1);
   }

   public final VertexFormat getVertexFormat() {
      return this.vertexFormat == null ? VertexFormat.POINT_TEXCOORD : (VertexFormat)this.vertexFormat.get();
   }

   public final ObjectProperty vertexFormatProperty() {
      if (this.vertexFormat == null) {
         this.vertexFormat = new SimpleObjectProperty(this, "vertexFormat") {
            protected void invalidated() {
               TriangleMesh.this.setDirty(true);
               TriangleMesh.this.facesSyncer.setDirty(true);
               TriangleMesh.this.faceSmoothingGroupsSyncer.setDirty(true);
            }
         };
      }

      return this.vertexFormat;
   }

   public final int getPointElementSize() {
      return this.getVertexFormat().getPointElementSize();
   }

   public final int getNormalElementSize() {
      return this.getVertexFormat().getNormalElementSize();
   }

   public final int getTexCoordElementSize() {
      return this.getVertexFormat().getTexCoordElementSize();
   }

   public final int getFaceElementSize() {
      return this.getVertexFormat().getVertexIndexSize() * 3;
   }

   public final ObservableFloatArray getPoints() {
      return this.points;
   }

   public final ObservableFloatArray getNormals() {
      return this.normals;
   }

   public final ObservableFloatArray getTexCoords() {
      return this.texCoords;
   }

   public final ObservableFaceArray getFaces() {
      return this.faces;
   }

   public final ObservableIntegerArray getFaceSmoothingGroups() {
      return this.faceSmoothingGroups;
   }

   void setDirty(boolean var1) {
      super.setDirty(var1);
      if (!var1) {
         this.pointsSyncer.setDirty(false);
         this.normalsSyncer.setDirty(false);
         this.texCoordsSyncer.setDirty(false);
         this.facesSyncer.setDirty(false);
         this.faceSmoothingGroupsSyncer.setDirty(false);
      }

   }

   int getRefCount() {
      return this.refCount;
   }

   synchronized void incRef() {
      ++this.refCount;
   }

   synchronized void decRef() {
      --this.refCount;
   }

   /** @deprecated */
   @Deprecated
   NGTriangleMesh impl_getPGTriangleMesh() {
      if (this.peer == null) {
         this.peer = new NGTriangleMesh();
      }

      return this.peer;
   }

   NGTriangleMesh getPGMesh() {
      return this.impl_getPGTriangleMesh();
   }

   private boolean validatePoints() {
      if (this.points.size() == 0) {
         return false;
      } else if (this.points.size() % this.getVertexFormat().getPointElementSize() != 0) {
         String var1 = TriangleMesh.class.getName();
         PlatformLogger.getLogger(var1).warning("points.size() has to be divisible by getPointElementSize(). It is to store multiple x, y, and z coordinates of this mesh");
         return false;
      } else {
         return true;
      }
   }

   private boolean validateNormals() {
      if (this.getVertexFormat() != VertexFormat.POINT_NORMAL_TEXCOORD) {
         return true;
      } else if (this.normals.size() == 0) {
         return false;
      } else if (this.normals.size() % this.getVertexFormat().getNormalElementSize() != 0) {
         String var1 = TriangleMesh.class.getName();
         PlatformLogger.getLogger(var1).warning("normals.size() has to be divisible by getNormalElementSize(). It is to store multiple nx, ny, and nz coordinates of this mesh");
         return false;
      } else {
         return true;
      }
   }

   private boolean validateTexCoords() {
      if (this.texCoords.size() == 0) {
         return false;
      } else if (this.texCoords.size() % this.getVertexFormat().getTexCoordElementSize() != 0) {
         String var1 = TriangleMesh.class.getName();
         PlatformLogger.getLogger(var1).warning("texCoords.size() has to be divisible by getTexCoordElementSize(). It is to store multiple u and v texture coordinates of this mesh");
         return false;
      } else {
         return true;
      }
   }

   private boolean validateFaces() {
      if (this.faces.size() == 0) {
         return false;
      } else {
         String var1 = TriangleMesh.class.getName();
         if (this.faces.size() % this.getFaceElementSize() != 0) {
            PlatformLogger.getLogger(var1).warning("faces.size() has to be divisible by getFaceElementSize().");
            return false;
         } else {
            int var2;
            int var3;
            int var4;
            if (this.getVertexFormat() == VertexFormat.POINT_TEXCOORD) {
               var2 = this.points.size() / this.getVertexFormat().getPointElementSize();
               var3 = this.texCoords.size() / this.getVertexFormat().getTexCoordElementSize();

               for(var4 = 0; var4 < this.faces.size(); ++var4) {
                  if (var4 % 2 == 0 && (this.faces.get(var4) >= var2 || this.faces.get(var4) < 0) || var4 % 2 != 0 && (this.faces.get(var4) >= var3 || this.faces.get(var4) < 0)) {
                     PlatformLogger.getLogger(var1).warning("The values in the faces array must be within the range of the number of vertices in the points array (0 to points.length / 3 - 1) for the point indices and within the range of the number of the vertices in the texCoords array (0 to texCoords.length / 2 - 1) for the texture coordinate indices.");
                     return false;
                  }
               }
            } else {
               if (this.getVertexFormat() != VertexFormat.POINT_NORMAL_TEXCOORD) {
                  PlatformLogger.getLogger(var1).warning("Unsupported VertexFormat: " + this.getVertexFormat().toString());
                  return false;
               }

               var2 = this.points.size() / this.getVertexFormat().getPointElementSize();
               var3 = this.normals.size() / this.getVertexFormat().getNormalElementSize();
               var4 = this.texCoords.size() / this.getVertexFormat().getTexCoordElementSize();

               for(int var5 = 0; var5 < this.faces.size(); var5 += 3) {
                  if (this.faces.get(var5) >= var2 || this.faces.get(var5) < 0 || this.faces.get(var5 + 1) >= var3 || this.faces.get(var5 + 1) < 0 || this.faces.get(var5 + 2) >= var4 || this.faces.get(var5 + 2) < 0) {
                     PlatformLogger.getLogger(var1).warning("The values in the faces array must be within the range of the number of vertices in the points array (0 to points.length / 3 - 1) for the point indices, and within the range of the number of the vertices in the normals array (0 to normals.length / 3 - 1) for the normals indices, and number of the vertices in the texCoords array (0 to texCoords.length / 2 - 1) for the texture coordinate indices.");
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   private boolean validateFaceSmoothingGroups() {
      if (this.faceSmoothingGroups.size() != 0 && this.faceSmoothingGroups.size() != this.faces.size() / this.getFaceElementSize()) {
         String var1 = TriangleMesh.class.getName();
         PlatformLogger.getLogger(var1).warning("faceSmoothingGroups.size() has to equal to number of faces.");
         return false;
      } else {
         return true;
      }
   }

   private boolean validate() {
      if (this.isPredefinedShape) {
         return true;
      } else {
         if (this.isValidDirty) {
            if (this.pointsSyncer.dirtyInFull) {
               this.isPointsValid = this.validatePoints();
            }

            if (this.normalsSyncer.dirtyInFull) {
               this.isNormalsValid = this.validateNormals();
            }

            if (this.texCoordsSyncer.dirtyInFull) {
               this.isTexCoordsValid = this.validateTexCoords();
            }

            if (this.facesSyncer.dirty || this.pointsSyncer.dirtyInFull || this.normalsSyncer.dirtyInFull || this.texCoordsSyncer.dirtyInFull) {
               this.isFacesValid = this.isPointsValid && this.isNormalsValid && this.isTexCoordsValid && this.validateFaces();
            }

            if (this.faceSmoothingGroupsSyncer.dirtyInFull || this.facesSyncer.dirtyInFull) {
               this.isFaceSmoothingGroupValid = this.isFacesValid && this.validateFaceSmoothingGroups();
            }

            this.isValidDirty = false;
         }

         return this.isPointsValid && this.isNormalsValid && this.isTexCoordsValid && this.isFaceSmoothingGroupValid && this.isFacesValid;
      }
   }

   /** @deprecated */
   @Deprecated
   void impl_updatePG() {
      if (this.isDirty()) {
         NGTriangleMesh var1 = this.impl_getPGTriangleMesh();
         if (this.validate()) {
            var1.setUserDefinedNormals(this.getVertexFormat() == VertexFormat.POINT_NORMAL_TEXCOORD);
            var1.syncPoints(this.pointsSyncer);
            var1.syncNormals(this.normalsSyncer);
            var1.syncTexCoords(this.texCoordsSyncer);
            var1.syncFaces(this.facesSyncer);
            var1.syncFaceSmoothingGroups(this.faceSmoothingGroupsSyncer);
         } else {
            var1.setUserDefinedNormals(false);
            var1.syncPoints((FloatArraySyncer)null);
            var1.syncNormals((FloatArraySyncer)null);
            var1.syncTexCoords((FloatArraySyncer)null);
            var1.syncFaces((IntegerArraySyncer)null);
            var1.syncFaceSmoothingGroups((IntegerArraySyncer)null);
         }

         this.setDirty(false);
      }
   }

   BaseBounds computeBounds(BaseBounds var1) {
      if (this.isDirty() || this.cachedBounds == null) {
         this.cachedBounds = new BoxBounds();
         if (this.validate()) {
            double var2 = (double)this.points.size();

            for(int var4 = 0; (double)var4 < var2; var4 += this.getVertexFormat().getPointElementSize()) {
               this.cachedBounds.add(this.points.get(var4), this.points.get(var4 + 1), this.points.get(var4 + 2));
            }
         }
      }

      return var1.deriveWithNewBounds(this.cachedBounds);
   }

   private Point3D computeCentroid(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      return new Point3D(var1 + (var13 + (var7 - var13) / 2.0 - var1) / 3.0, var3 + (var15 + (var9 - var15) / 2.0 - var3) / 3.0, var5 + (var17 + (var11 - var17) / 2.0 - var5) / 3.0);
   }

   private Point2D computeCentroid(Point2D var1, Point2D var2, Point2D var3) {
      Point2D var4 = var2.midpoint(var3);
      Point2D var5 = var4.subtract(var1);
      return var1.add(new Point2D(var5.getX() / 3.0, var5.getY() / 3.0));
   }

   private boolean computeIntersectsFace(PickRay var1, Vec3d var2, Vec3d var3, int var4, CullFace var5, Node var6, boolean var7, PickResultChooser var8) {
      int var9 = this.getVertexFormat().getVertexIndexSize();
      int var10 = this.getVertexFormat().getPointElementSize();
      int var11 = this.faces.get(var4) * var10;
      int var12 = this.faces.get(var4 + var9) * var10;
      int var13 = this.faces.get(var4 + 2 * var9) * var10;
      float var14 = this.points.get(var11);
      float var15 = this.points.get(var11 + 1);
      float var16 = this.points.get(var11 + 2);
      float var17 = this.points.get(var12);
      float var18 = this.points.get(var12 + 1);
      float var19 = this.points.get(var12 + 2);
      float var20 = this.points.get(var13);
      float var21 = this.points.get(var13 + 1);
      float var22 = this.points.get(var13 + 2);
      float var23 = var17 - var14;
      float var24 = var18 - var15;
      float var25 = var19 - var16;
      float var26 = var20 - var14;
      float var27 = var21 - var15;
      float var28 = var22 - var16;
      double var29 = var3.y * (double)var28 - var3.z * (double)var27;
      double var31 = var3.z * (double)var26 - var3.x * (double)var28;
      double var33 = var3.x * (double)var27 - var3.y * (double)var26;
      double var35 = (double)var23 * var29 + (double)var24 * var31 + (double)var25 * var33;
      if (var35 == 0.0) {
         return false;
      } else {
         double var37 = 1.0 / var35;
         double var39 = var2.x - (double)var14;
         double var41 = var2.y - (double)var15;
         double var43 = var2.z - (double)var16;
         double var45 = var37 * (var39 * var29 + var41 * var31 + var43 * var33);
         if (!(var45 < 0.0) && !(var45 > 1.0)) {
            double var47 = var41 * (double)var25 - var43 * (double)var24;
            double var49 = var43 * (double)var23 - var39 * (double)var25;
            double var51 = var39 * (double)var24 - var41 * (double)var23;
            double var53 = var37 * (var3.x * var47 + var3.y * var49 + var3.z * var51);
            if (!(var53 < 0.0) && !(var45 + var53 > 1.0)) {
               double var55 = var37 * ((double)var26 * var47 + (double)var27 * var49 + (double)var28 * var51);
               if (var55 >= var1.getNearClip() && var55 <= var1.getFarClip()) {
                  Point3D var57;
                  if (var5 != CullFace.NONE) {
                     var57 = new Point3D((double)(var24 * var28 - var25 * var27), (double)(var25 * var26 - var23 * var28), (double)(var23 * var27 - var24 * var26));
                     double var58 = var57.angle(new Point3D(-var3.x, -var3.y, -var3.z));
                     if ((var58 >= 90.0 || var5 != CullFace.BACK) && (var58 <= 90.0 || var5 != CullFace.FRONT)) {
                        return false;
                     }
                  }

                  if (!Double.isInfinite(var55) && !Double.isNaN(var55)) {
                     if (var8 != null && var8.isCloser(var55)) {
                        var57 = PickResultChooser.computePoint(var1, var55);
                        Point3D var94 = this.computeCentroid((double)var14, (double)var15, (double)var16, (double)var17, (double)var18, (double)var19, (double)var20, (double)var21, (double)var22);
                        Point3D var59 = new Point3D((double)var14 - var94.getX(), (double)var15 - var94.getY(), (double)var16 - var94.getZ());
                        Point3D var60 = new Point3D((double)var17 - var94.getX(), (double)var18 - var94.getY(), (double)var19 - var94.getZ());
                        Point3D var61 = new Point3D((double)var20 - var94.getX(), (double)var21 - var94.getY(), (double)var22 - var94.getZ());
                        Point3D var62 = var60.subtract(var59);
                        Point3D var63 = var61.subtract(var59);
                        Point3D var64 = var62.crossProduct(var63);
                        if (var64.getZ() < 0.0) {
                           var64 = new Point3D(-var64.getX(), -var64.getY(), -var64.getZ());
                        }

                        Point3D var65 = var64.crossProduct(Rotate.Z_AXIS);
                        double var66 = Math.atan2(var65.magnitude(), var64.dotProduct(Rotate.Z_AXIS));
                        Rotate var68 = new Rotate(Math.toDegrees(var66), var65);
                        Point3D var69 = var68.transform(var59);
                        Point3D var70 = var68.transform(var60);
                        Point3D var71 = var68.transform(var61);
                        Point3D var72 = var68.transform(var57.subtract(var94));
                        Point2D var73 = new Point2D(var69.getX(), var69.getY());
                        Point2D var74 = new Point2D(var70.getX(), var70.getY());
                        Point2D var75 = new Point2D(var71.getX(), var71.getY());
                        Point2D var76 = new Point2D(var72.getX(), var72.getY());
                        int var77 = this.getVertexFormat().getTexCoordElementSize();
                        int var78 = this.getVertexFormat().getTexCoordIndexOffset();
                        int var79 = this.faces.get(var4 + var78) * var77;
                        int var80 = this.faces.get(var4 + var9 + var78) * var77;
                        int var81 = this.faces.get(var4 + var9 * 2 + var78) * var77;
                        Point2D var82 = new Point2D((double)this.texCoords.get(var79), (double)this.texCoords.get(var79 + 1));
                        Point2D var83 = new Point2D((double)this.texCoords.get(var80), (double)this.texCoords.get(var80 + 1));
                        Point2D var84 = new Point2D((double)this.texCoords.get(var81), (double)this.texCoords.get(var81 + 1));
                        Point2D var85 = this.computeCentroid(var82, var83, var84);
                        Point2D var86 = var82.subtract(var85);
                        Point2D var87 = var83.subtract(var85);
                        Point2D var88 = var84.subtract(var85);
                        Affine var89 = new Affine(var73.getX(), var74.getX(), var75.getX(), var73.getY(), var74.getY(), var75.getY());
                        Affine var90 = new Affine(var86.getX(), var87.getX(), var88.getX(), var86.getY(), var87.getY(), var88.getY());
                        Point2D var91 = null;

                        try {
                           var89.invert();
                           var90.append(var89);
                           var91 = var85.add(var90.transform(var76));
                        } catch (NonInvertibleTransformException var93) {
                        }

                        var8.offer(var6, var55, var7 ? var4 / this.getFaceElementSize() : -1, var57, var91);
                        return true;
                     } else {
                        return true;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeIntersects(PickRay var1, PickResultChooser var2, Node var3, CullFace var4, boolean var5) {
      boolean var6 = false;
      if (this.validate()) {
         int var7 = this.faces.size();
         Vec3d var8 = var1.getOriginNoClone();
         Vec3d var9 = var1.getDirectionNoClone();

         for(int var10 = 0; var10 < var7; var10 += this.getFaceElementSize()) {
            if (this.computeIntersectsFace(var1, var8, var9, var10, var4, var3, var5, var2)) {
               var6 = true;
            }
         }
      }

      return var6;
   }

   private class Listener implements ArrayChangeListener, FloatArraySyncer, IntegerArraySyncer {
      protected final ObservableArray array;
      protected boolean dirty = true;
      protected boolean dirtyInFull = true;
      protected int dirtyRangeFrom;
      protected int dirtyRangeLength;

      public Listener(ObservableArray var2) {
         this.array = var2;
         var2.addListener(this);
      }

      protected final void addDirtyRange(int var1, int var2) {
         if (var2 > 0 && !this.dirtyInFull) {
            this.markDirty();
            if (this.dirtyRangeLength == 0) {
               this.dirtyRangeFrom = var1;
               this.dirtyRangeLength = var2;
            } else {
               int var3 = Math.min(this.dirtyRangeFrom, var1);
               int var4 = Math.max(this.dirtyRangeFrom + this.dirtyRangeLength, var1 + var2);
               this.dirtyRangeFrom = var3;
               this.dirtyRangeLength = var4 - var3;
            }
         }

      }

      protected void markDirty() {
         this.dirty = true;
         TriangleMesh.this.setDirty(true);
      }

      public void onChanged(ObservableArray var1, boolean var2, int var3, int var4) {
         if (var2) {
            this.setDirty(true);
         } else {
            this.addDirtyRange(var3, var4 - var3);
         }

         TriangleMesh.this.isValidDirty = true;
      }

      public final void setDirty(boolean var1) {
         this.dirtyInFull = var1;
         if (var1) {
            this.markDirty();
            this.dirtyRangeFrom = 0;
            this.dirtyRangeLength = this.array.size();
         } else {
            this.dirty = false;
            this.dirtyRangeFrom = this.dirtyRangeLength = 0;
         }

      }

      public float[] syncTo(float[] var1, int[] var2) {
         assert var2 != null && var2.length == 2;

         ObservableFloatArray var3 = (ObservableFloatArray)this.array;
         if (!this.dirtyInFull && var1 != null && var1.length == var3.size()) {
            var2[0] = this.dirtyRangeFrom;
            var2[1] = this.dirtyRangeLength;
            var3.copyTo(this.dirtyRangeFrom, var1, this.dirtyRangeFrom, this.dirtyRangeLength);
            return var1;
         } else {
            var2[0] = 0;
            var2[1] = var3.size();
            return var3.toArray((float[])null);
         }
      }

      public int[] syncTo(int[] var1, int[] var2) {
         assert var2 != null && var2.length == 2;

         ObservableIntegerArray var3 = (ObservableIntegerArray)this.array;
         if (!this.dirtyInFull && var1 != null && var1.length == var3.size()) {
            var2[0] = this.dirtyRangeFrom;
            var2[1] = this.dirtyRangeLength;
            var3.copyTo(this.dirtyRangeFrom, var1, this.dirtyRangeFrom, this.dirtyRangeLength);
            return var1;
         } else {
            var2[0] = 0;
            var2[1] = var3.size();
            return var3.toArray((int[])null);
         }
      }
   }
}
