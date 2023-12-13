package javafx.scene.image;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGImageView;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

@DefaultProperty("image")
public class ImageView extends Node {
   private ObjectProperty image;
   private Image oldImage;
   private StringProperty imageUrl;
   private final AbstractNotifyListener platformImageChangeListener;
   private DoubleProperty x;
   private DoubleProperty y;
   private DoubleProperty fitWidth;
   private DoubleProperty fitHeight;
   private BooleanProperty preserveRatio;
   private BooleanProperty smooth;
   public static final boolean SMOOTH_DEFAULT = Toolkit.getToolkit().getDefaultImageSmooth();
   private ObjectProperty viewport;
   private double destWidth;
   private double destHeight;
   private boolean validWH;
   private static final String DEFAULT_STYLE_CLASS = "image-view";

   public ImageView() {
      this.imageUrl = null;
      this.platformImageChangeListener = new AbstractNotifyListener() {
         public void invalidated(Observable var1) {
            ImageView.this.invalidateWidthHeight();
            ImageView.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
            ImageView.this.impl_geomChanged();
         }
      };
      this.getStyleClass().add("image-view");
      this.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
   }

   public ImageView(String var1) {
      this(new Image(var1));
   }

   public ImageView(Image var1) {
      this.imageUrl = null;
      this.platformImageChangeListener = new AbstractNotifyListener() {
         public void invalidated(Observable var1) {
            ImageView.this.invalidateWidthHeight();
            ImageView.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
            ImageView.this.impl_geomChanged();
         }
      };
      this.getStyleClass().add("image-view");
      this.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
      this.setImage(var1);
   }

   public final void setImage(Image var1) {
      this.imageProperty().set(var1);
   }

   public final Image getImage() {
      return this.image == null ? null : (Image)this.image.get();
   }

   public final ObjectProperty imageProperty() {
      if (this.image == null) {
         this.image = new ObjectPropertyBase() {
            private boolean needsListeners = false;

            public void invalidated() {
               Image var1 = (Image)this.get();
               boolean var2 = var1 == null || ImageView.this.oldImage == null || ImageView.this.oldImage.getWidth() != var1.getWidth() || ImageView.this.oldImage.getHeight() != var1.getHeight();
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(ImageView.this.oldImage).removeListener(ImageView.this.platformImageChangeListener.getWeakListener());
               }

               this.needsListeners = var1 != null && (var1.isAnimation() || var1.getProgress() < 1.0);
               ImageView.this.oldImage = var1;
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(var1).addListener(ImageView.this.platformImageChangeListener.getWeakListener());
               }

               if (var2) {
                  ImageView.this.invalidateWidthHeight();
                  ImageView.this.impl_geomChanged();
               }

               ImageView.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "image";
            }
         };
      }

      return this.image;
   }

   private StringProperty imageUrlProperty() {
      if (this.imageUrl == null) {
         this.imageUrl = new StyleableStringProperty() {
            protected void invalidated() {
               String var1 = this.get();
               if (var1 != null) {
                  ImageView.this.setImage(StyleManager.getInstance().getCachedImage(var1));
               } else {
                  ImageView.this.setImage((Image)null);
               }

            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "imageUrl";
            }

            public CssMetaData getCssMetaData() {
               return ImageView.StyleableProperties.IMAGE;
            }
         };
      }

      return this.imageUrl;
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
            protected void invalidated() {
               ImageView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               ImageView.this.impl_geomChanged();
            }

            public Object getBean() {
               return ImageView.this;
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
            protected void invalidated() {
               ImageView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               ImageView.this.impl_geomChanged();
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   public final void setFitWidth(double var1) {
      this.fitWidthProperty().set(var1);
   }

   public final double getFitWidth() {
      return this.fitWidth == null ? 0.0 : this.fitWidth.get();
   }

   public final DoubleProperty fitWidthProperty() {
      if (this.fitWidth == null) {
         this.fitWidth = new DoublePropertyBase() {
            protected void invalidated() {
               ImageView.this.invalidateWidthHeight();
               ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
               ImageView.this.impl_geomChanged();
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "fitWidth";
            }
         };
      }

      return this.fitWidth;
   }

   public final void setFitHeight(double var1) {
      this.fitHeightProperty().set(var1);
   }

   public final double getFitHeight() {
      return this.fitHeight == null ? 0.0 : this.fitHeight.get();
   }

   public final DoubleProperty fitHeightProperty() {
      if (this.fitHeight == null) {
         this.fitHeight = new DoublePropertyBase() {
            protected void invalidated() {
               ImageView.this.invalidateWidthHeight();
               ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
               ImageView.this.impl_geomChanged();
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "fitHeight";
            }
         };
      }

      return this.fitHeight;
   }

   public final void setPreserveRatio(boolean var1) {
      this.preserveRatioProperty().set(var1);
   }

   public final boolean isPreserveRatio() {
      return this.preserveRatio == null ? false : this.preserveRatio.get();
   }

   public final BooleanProperty preserveRatioProperty() {
      if (this.preserveRatio == null) {
         this.preserveRatio = new BooleanPropertyBase() {
            protected void invalidated() {
               ImageView.this.invalidateWidthHeight();
               ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
               ImageView.this.impl_geomChanged();
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "preserveRatio";
            }
         };
      }

      return this.preserveRatio;
   }

   public final void setSmooth(boolean var1) {
      this.smoothProperty().set(var1);
   }

   public final boolean isSmooth() {
      return this.smooth == null ? SMOOTH_DEFAULT : this.smooth.get();
   }

   public final BooleanProperty smoothProperty() {
      if (this.smooth == null) {
         this.smooth = new BooleanPropertyBase(SMOOTH_DEFAULT) {
            protected void invalidated() {
               ImageView.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "smooth";
            }
         };
      }

      return this.smooth;
   }

   public final void setViewport(Rectangle2D var1) {
      this.viewportProperty().set(var1);
   }

   public final Rectangle2D getViewport() {
      return this.viewport == null ? null : (Rectangle2D)this.viewport.get();
   }

   public final ObjectProperty viewportProperty() {
      if (this.viewport == null) {
         this.viewport = new ObjectPropertyBase() {
            protected void invalidated() {
               ImageView.this.invalidateWidthHeight();
               ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
               ImageView.this.impl_geomChanged();
            }

            public Object getBean() {
               return ImageView.this;
            }

            public String getName() {
               return "viewport";
            }
         };
      }

      return this.viewport;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGImageView();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      this.recomputeWidthHeight();
      var1 = var1.deriveWithNewBounds((float)this.getX(), (float)this.getY(), 0.0F, (float)(this.getX() + this.destWidth), (float)(this.getY() + this.destHeight), 0.0F);
      var1 = var2.transform(var1, var1);
      return var1;
   }

   private void invalidateWidthHeight() {
      this.validWH = false;
   }

   private void recomputeWidthHeight() {
      if (!this.validWH) {
         Image var1 = this.getImage();
         Rectangle2D var2 = this.getViewport();
         double var3 = 0.0;
         double var5 = 0.0;
         if (var2 != null && var2.getWidth() > 0.0 && var2.getHeight() > 0.0) {
            var3 = var2.getWidth();
            var5 = var2.getHeight();
         } else if (var1 != null) {
            var3 = var1.getWidth();
            var5 = var1.getHeight();
         }

         double var7 = this.getFitWidth();
         double var9 = this.getFitHeight();
         if (!this.isPreserveRatio() || !(var3 > 0.0) || !(var5 > 0.0) || !(var7 > 0.0) && !(var9 > 0.0)) {
            if (var7 > 0.0) {
               var3 = var7;
            }

            if (var9 > 0.0) {
               var5 = var9;
            }
         } else if (!(var7 <= 0.0) && (!(var9 > 0.0) || !(var7 * var5 > var9 * var3))) {
            var5 = var5 * var7 / var3;
            var3 = var7;
         } else {
            var3 = var3 * var9 / var5;
            var5 = var9;
         }

         this.destWidth = var3;
         this.destHeight = var5;
         this.validWH = true;
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      if (this.getImage() == null) {
         return false;
      } else {
         this.recomputeWidthHeight();
         double var5 = var1 - this.getX();
         double var7 = var3 - this.getY();
         Image var9 = this.getImage();
         double var10 = var9.getWidth();
         double var12 = var9.getHeight();
         double var14 = var10;
         double var16 = var12;
         double var18 = 0.0;
         double var20 = 0.0;
         double var22 = 0.0;
         double var24 = 0.0;
         Rectangle2D var26 = this.getViewport();
         if (var26 != null) {
            var18 = var26.getWidth();
            var20 = var26.getHeight();
            var22 = var26.getMinX();
            var24 = var26.getMinY();
         }

         if (var18 > 0.0 && var20 > 0.0) {
            var14 = var18;
            var16 = var20;
         }

         var5 = var22 + var5 * var14 / this.destWidth;
         var7 = var24 + var7 * var16 / this.destHeight;
         return !(var5 < 0.0) && !(var7 < 0.0) && !(var5 >= var10) && !(var7 >= var12) && !(var5 < var22) && !(var7 < var24) && !(var5 >= var22 + var14) && !(var7 >= var24 + var16) ? Toolkit.getToolkit().imageContains(var9.impl_getPlatformImage(), (float)var5, (float)var7) : false;
      }
   }

   public static List getClassCssMetaData() {
      return ImageView.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   void updateViewport() {
      this.recomputeWidthHeight();
      if (this.getImage() != null && this.getImage().impl_getPlatformImage() != null) {
         Rectangle2D var1 = this.getViewport();
         NGImageView var2 = (NGImageView)this.impl_getPeer();
         if (var1 != null) {
            var2.setViewport((float)var1.getMinX(), (float)var1.getMinY(), (float)var1.getWidth(), (float)var1.getHeight(), (float)this.destWidth, (float)this.destHeight);
         } else {
            var2.setViewport(0.0F, 0.0F, 0.0F, 0.0F, (float)this.destWidth, (float)this.destHeight);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGImageView var1 = (NGImageView)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         var1.setX((float)this.getX());
         var1.setY((float)this.getY());
      }

      if (this.impl_isDirty(DirtyBits.NODE_SMOOTH)) {
         var1.setSmooth(this.isSmooth());
      }

      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         var1.setImage(this.getImage() != null ? this.getImage().impl_getPlatformImage() : null);
      }

      if (this.impl_isDirty(DirtyBits.NODE_VIEWPORT) || this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         this.updateViewport();
      }

   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      return var1.processLeafNode(this, var2);
   }

   private static class StyleableProperties {
      private static final CssMetaData IMAGE = new CssMetaData("-fx-image", URLConverter.getInstance()) {
         public boolean isSettable(ImageView var1) {
            return var1.image == null || !var1.image.isBound();
         }

         public StyleableProperty getStyleableProperty(ImageView var1) {
            return (StyleableProperty)var1.imageUrlProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Node.getClassCssMetaData());
         var0.add(IMAGE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
