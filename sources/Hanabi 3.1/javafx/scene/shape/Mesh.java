package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import sun.util.logging.PlatformLogger;

public abstract class Mesh {
   private final BooleanProperty dirty = new SimpleBooleanProperty(true);

   protected Mesh() {
      if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var1 = Mesh.class.getName();
         PlatformLogger.getLogger(var1).warning("System can't support ConditionalFeature.SCENE3D");
      }

   }

   final boolean isDirty() {
      return this.dirty.getValue();
   }

   void setDirty(boolean var1) {
      this.dirty.setValue(var1);
   }

   final BooleanProperty dirtyProperty() {
      return this.dirty;
   }

   abstract NGTriangleMesh getPGMesh();

   abstract void impl_updatePG();

   abstract BaseBounds computeBounds(BaseBounds var1);

   /** @deprecated */
   @Deprecated
   protected abstract boolean impl_computeIntersects(PickRay var1, PickResultChooser var2, Node var3, CullFace var4, boolean var5);
}
