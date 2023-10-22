package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGShape3D;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import sun.util.logging.PlatformLogger;

public abstract class Shape3D extends Node {
   private static final PhongMaterial DEFAULT_MATERIAL = new PhongMaterial();
   PredefinedMeshManager manager = PredefinedMeshManager.getInstance();
   int key = 0;
   private ObjectProperty material;
   private ObjectProperty drawMode;
   private ObjectProperty cullFace;

   protected Shape3D() {
      if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var1 = Shape3D.class.getName();
         PlatformLogger.getLogger(var1).warning("System can't support ConditionalFeature.SCENE3D");
      }

   }

   public final void setMaterial(Material var1) {
      this.materialProperty().set(var1);
   }

   public final Material getMaterial() {
      return this.material == null ? null : (Material)this.material.get();
   }

   public final ObjectProperty materialProperty() {
      if (this.material == null) {
         this.material = new SimpleObjectProperty(this, "material") {
            private Material old = null;
            private final ChangeListener materialChangeListener = (var1x, var2x, var3x) -> {
               if (var3x) {
                  Shape3D.this.impl_markDirty(DirtyBits.MATERIAL);
               }

            };
            private final WeakChangeListener weakMaterialChangeListener;

            {
               this.weakMaterialChangeListener = new WeakChangeListener(this.materialChangeListener);
            }

            protected void invalidated() {
               if (this.old != null) {
                  this.old.impl_dirtyProperty().removeListener(this.weakMaterialChangeListener);
               }

               Material var1 = (Material)this.get();
               if (var1 != null) {
                  var1.impl_dirtyProperty().addListener(this.weakMaterialChangeListener);
               }

               Shape3D.this.impl_markDirty(DirtyBits.MATERIAL);
               Shape3D.this.impl_geomChanged();
               this.old = var1;
            }
         };
      }

      return this.material;
   }

   public final void setDrawMode(DrawMode var1) {
      this.drawModeProperty().set(var1);
   }

   public final DrawMode getDrawMode() {
      return this.drawMode == null ? DrawMode.FILL : (DrawMode)this.drawMode.get();
   }

   public final ObjectProperty drawModeProperty() {
      if (this.drawMode == null) {
         this.drawMode = new SimpleObjectProperty(this, "drawMode", DrawMode.FILL) {
            protected void invalidated() {
               Shape3D.this.impl_markDirty(DirtyBits.NODE_DRAWMODE);
            }
         };
      }

      return this.drawMode;
   }

   public final void setCullFace(CullFace var1) {
      this.cullFaceProperty().set(var1);
   }

   public final CullFace getCullFace() {
      return this.cullFace == null ? CullFace.BACK : (CullFace)this.cullFace.get();
   }

   public final ObjectProperty cullFaceProperty() {
      if (this.cullFace == null) {
         this.cullFace = new SimpleObjectProperty(this, "cullFace", CullFace.BACK) {
            protected void invalidated() {
               Shape3D.this.impl_markDirty(DirtyBits.NODE_CULLFACE);
            }
         };
      }

      return this.cullFace;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      return new BoxBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGShape3D var1 = (NGShape3D)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.MATERIAL)) {
         Object var2 = this.getMaterial() == null ? DEFAULT_MATERIAL : this.getMaterial();
         ((Material)var2).impl_updatePG();
         var1.setMaterial(((Material)var2).impl_getNGMaterial());
      }

      if (this.impl_isDirty(DirtyBits.NODE_DRAWMODE)) {
         var1.setDrawMode(this.getDrawMode() == null ? DrawMode.FILL : this.getDrawMode());
      }

      if (this.impl_isDirty(DirtyBits.NODE_CULLFACE)) {
         var1.setCullFace(this.getCullFace() == null ? CullFace.BACK : this.getCullFace());
      }

   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
