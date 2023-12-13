package javafx.scene.paint;

import com.sun.javafx.sg.prism.NGPhongMaterial;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import sun.util.logging.PlatformLogger;

public abstract class Material {
   private final BooleanProperty dirty = new SimpleBooleanProperty(true);

   protected Material() {
      if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var1 = Material.class.getName();
         PlatformLogger.getLogger(var1).warning("System can't support ConditionalFeature.SCENE3D");
      }

   }

   final boolean isDirty() {
      return this.dirty.getValue();
   }

   void setDirty(boolean var1) {
      this.dirty.setValue(var1);
   }

   /** @deprecated */
   @Deprecated
   public final BooleanProperty impl_dirtyProperty() {
      return this.dirty;
   }

   /** @deprecated */
   @Deprecated
   public abstract void impl_updatePG();

   /** @deprecated */
   @Deprecated
   public abstract NGPhongMaterial impl_getNGMaterial();
}
