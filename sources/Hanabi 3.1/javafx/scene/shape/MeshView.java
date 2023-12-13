package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGMeshView;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;

public class MeshView extends Shape3D {
   private ObjectProperty mesh;

   public MeshView() {
   }

   public MeshView(Mesh var1) {
      this.setMesh(var1);
   }

   public final void setMesh(Mesh var1) {
      this.meshProperty().set(var1);
   }

   public final Mesh getMesh() {
      return this.mesh == null ? null : (Mesh)this.mesh.get();
   }

   public final ObjectProperty meshProperty() {
      if (this.mesh == null) {
         this.mesh = new SimpleObjectProperty(this, "mesh") {
            private Mesh old = null;
            private final ChangeListener meshChangeListener = (var1x, var2x, var3x) -> {
               if (var3x) {
                  MeshView.this.impl_markDirty(DirtyBits.MESH_GEOM);
                  MeshView.this.impl_geomChanged();
               }

            };
            private final WeakChangeListener weakMeshChangeListener;

            {
               this.weakMeshChangeListener = new WeakChangeListener(this.meshChangeListener);
            }

            protected void invalidated() {
               if (this.old != null) {
                  this.old.dirtyProperty().removeListener(this.weakMeshChangeListener);
               }

               Mesh var1 = (Mesh)this.get();
               if (var1 != null) {
                  var1.dirtyProperty().addListener(this.weakMeshChangeListener);
               }

               MeshView.this.impl_markDirty(DirtyBits.MESH);
               MeshView.this.impl_markDirty(DirtyBits.MESH_GEOM);
               MeshView.this.impl_geomChanged();
               this.old = var1;
            }
         };
      }

      return this.mesh;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGMeshView var1 = (NGMeshView)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.MESH_GEOM) && this.getMesh() != null) {
         this.getMesh().impl_updatePG();
      }

      if (this.impl_isDirty(DirtyBits.MESH)) {
         var1.setMesh(this.getMesh() == null ? null : this.getMesh().getPGMesh());
      }

   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGMeshView();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.getMesh() != null) {
         var1 = this.getMesh().computeBounds(var1);
         var1 = var2.transform(var1, var1);
      } else {
         var1.makeEmpty();
      }

      return var1;
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeIntersects(PickRay var1, PickResultChooser var2) {
      return this.getMesh().impl_computeIntersects(var1, var2, this, this.getCullFace(), true);
   }
}
