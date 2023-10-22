package javafx.scene.effect;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.Identity;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class ImageInput extends Effect {
   private ObjectProperty source;
   private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener() {
      public void invalidated(Observable var1) {
         ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
         ImageInput.this.effectBoundsChanged();
      }
   };
   private Image oldImage;
   private DoubleProperty x;
   private DoubleProperty y;

   public ImageInput() {
   }

   public ImageInput(Image var1) {
      this.setSource(var1);
   }

   public ImageInput(Image var1, double var2, double var4) {
      this.setSource(var1);
      this.setX(var2);
      this.setY(var4);
   }

   Identity impl_createImpl() {
      return new Identity((Filterable)null);
   }

   public final void setSource(Image var1) {
      this.sourceProperty().set(var1);
   }

   public final Image getSource() {
      return this.source == null ? null : (Image)this.source.get();
   }

   public final ObjectProperty sourceProperty() {
      if (this.source == null) {
         this.source = new ObjectPropertyBase() {
            private boolean needsListeners = false;

            public void invalidated() {
               Image var1 = (Image)this.get();
               Toolkit.ImageAccessor var2 = Toolkit.getImageAccessor();
               if (this.needsListeners) {
                  var2.getImageProperty(ImageInput.this.oldImage).removeListener(ImageInput.this.platformImageChangeListener.getWeakListener());
               }

               this.needsListeners = var1 != null && (var2.isAnimation(var1) || var1.getProgress() < 1.0);
               ImageInput.this.oldImage = var1;
               if (this.needsListeners) {
                  var2.getImageProperty(var1).addListener(ImageInput.this.platformImageChangeListener.getWeakListener());
               }

               ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ImageInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ImageInput.this;
            }

            public String getName() {
               return "source";
            }
         };
      }

      return this.source;
   }

   public final void setX(double var1) {
      this.xProperty().set(var1);
   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            public void invalidated() {
               ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ImageInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ImageInput.this;
            }

            public String getName() {
               return "x";
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      this.yProperty().set(var1);
   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            public void invalidated() {
               ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ImageInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ImageInput.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   void impl_update() {
      Identity var1 = (Identity)this.impl_getImpl();
      Image var2 = this.getSource();
      if (var2 != null && var2.impl_getPlatformImage() != null) {
         var1.setSource(Toolkit.getToolkit().toFilterable(var2));
      } else {
         var1.setSource((Filterable)null);
      }

      var1.setLocation(new Point2D((float)this.getX(), (float)this.getY()));
   }

   boolean impl_checkChainContains(Effect var1) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      Image var5 = this.getSource();
      if (var5 != null && var5.impl_getPlatformImage() != null) {
         float var6 = (float)this.getX();
         float var7 = (float)this.getY();
         float var8 = (float)var5.getWidth();
         float var9 = (float)var5.getHeight();
         RectBounds var10 = new RectBounds(var6, var7, var6 + var8, var7 + var9);
         return transformBounds(var2, var10);
      } else {
         return new RectBounds();
      }
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      return new ImageInput(this.getSource(), this.getX(), this.getY());
   }
}
