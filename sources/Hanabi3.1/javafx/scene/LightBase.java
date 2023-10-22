package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.tk.Toolkit;
import java.util.Iterator;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import sun.util.logging.PlatformLogger;

public abstract class LightBase extends Node {
   private Affine3D localToSceneTx;
   private ObjectProperty color;
   private BooleanProperty lightOn;
   private ObservableList scope;

   protected LightBase() {
      this(Color.WHITE);
   }

   protected LightBase(Color var1) {
      this.localToSceneTx = new Affine3D();
      if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var2 = LightBase.class.getName();
         PlatformLogger.getLogger(var2).warning("System can't support ConditionalFeature.SCENE3D");
      }

      this.setColor(var1);
      this.localToSceneTransformProperty().addListener((var1x) -> {
         this.impl_markDirty(DirtyBits.NODE_LIGHT_TRANSFORM);
      });
   }

   public final void setColor(Color var1) {
      this.colorProperty().set(var1);
   }

   public final Color getColor() {
      return this.color == null ? null : (Color)this.color.get();
   }

   public final ObjectProperty colorProperty() {
      if (this.color == null) {
         this.color = new SimpleObjectProperty(this, "color") {
            protected void invalidated() {
               LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT);
            }
         };
      }

      return this.color;
   }

   public final void setLightOn(boolean var1) {
      this.lightOnProperty().set(var1);
   }

   public final boolean isLightOn() {
      return this.lightOn == null ? true : this.lightOn.get();
   }

   public final BooleanProperty lightOnProperty() {
      if (this.lightOn == null) {
         this.lightOn = new SimpleBooleanProperty(this, "lightOn", true) {
            protected void invalidated() {
               LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT);
            }
         };
      }

      return this.lightOn;
   }

   public ObservableList getScope() {
      if (this.scope == null) {
         this.scope = new TrackableObservableList() {
            protected void onChanged(ListChangeListener.Change var1) {
               LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT_SCOPE);

               label45:
               while(var1.next()) {
                  Iterator var2 = var1.getRemoved().iterator();

                  while(true) {
                     Node var3;
                     do {
                        if (!var2.hasNext()) {
                           var2 = var1.getAddedSubList().iterator();

                           while(true) {
                              do {
                                 if (!var2.hasNext()) {
                                    continue label45;
                                 }

                                 var3 = (Node)var2.next();
                              } while(!(var3 instanceof Parent) && !(var3 instanceof Shape3D));

                              LightBase.this.markChildrenDirty(var3);
                           }
                        }

                        var3 = (Node)var2.next();
                     } while(!(var3 instanceof Parent) && !(var3 instanceof Shape3D));

                     LightBase.this.markChildrenDirty(var3);
                  }
               }

            }
         };
      }

      return this.scope;
   }

   void scenesChanged(Scene var1, SubScene var2, Scene var3, SubScene var4) {
      if (var4 != null) {
         var4.removeLight(this);
      } else if (var3 != null) {
         var3.removeLight(this);
      }

      if (var2 != null) {
         var2.addLight(this);
      } else if (var1 != null) {
         var1.addLight(this);
      }

   }

   private void markOwnerDirty() {
      SubScene var1 = this.getSubScene();
      if (var1 != null) {
         var1.markContentDirty();
      } else {
         Scene var2 = this.getScene();
         if (var2 != null) {
            var2.setNeedsRepaint();
         }
      }

   }

   private void markChildrenDirty(Node var1) {
      if (var1 instanceof Shape3D) {
         ((Shape3D)var1).impl_markDirty(DirtyBits.NODE_DRAWMODE);
      } else if (var1 instanceof Parent) {
         Iterator var2 = ((Parent)var1).getChildren().iterator();

         while(var2.hasNext()) {
            Node var3 = (Node)var2.next();
            this.markChildrenDirty(var3);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_markDirty(DirtyBits var1) {
      super.impl_markDirty(var1);
      if (this.scope != null && !this.getScope().isEmpty()) {
         if (var1 != DirtyBits.NODE_LIGHT_SCOPE) {
            ObservableList var2 = this.getScope();
            int var3 = 0;

            for(int var4 = var2.size(); var3 < var4; ++var3) {
               this.markChildrenDirty((Node)var2.get(var3));
            }
         }
      } else {
         this.markOwnerDirty();
      }

   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGLightBase var1 = (NGLightBase)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.NODE_LIGHT)) {
         var1.setColor(this.getColor() == null ? Toolkit.getPaintAccessor().getPlatformPaint(Color.WHITE) : Toolkit.getPaintAccessor().getPlatformPaint(this.getColor()));
         var1.setLightOn(this.isLightOn());
      }

      if (this.impl_isDirty(DirtyBits.NODE_LIGHT_SCOPE) && this.scope != null) {
         ObservableList var2 = this.getScope();
         if (var2.isEmpty()) {
            var1.setScope((Object[])null);
         } else {
            Object[] var3 = new Object[var2.size()];

            for(int var4 = 0; var4 < var2.size(); ++var4) {
               Node var5 = (Node)var2.get(var4);
               var3[var4] = var5.impl_getPeer();
            }

            var1.setScope(var3);
         }
      }

      if (this.impl_isDirty(DirtyBits.NODE_LIGHT_TRANSFORM)) {
         this.localToSceneTx.setToIdentity();
         this.getLocalToSceneTransform().impl_apply(this.localToSceneTx);
         var1.setWorldTransform(this.localToSceneTx);
      }

   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      return new BoxBounds();
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
