package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

@DefaultProperty("content")
public class ScrollPane extends Control {
   private ObjectProperty hbarPolicy;
   private ObjectProperty vbarPolicy;
   private ObjectProperty content;
   private DoubleProperty hvalue;
   private DoubleProperty vvalue;
   private DoubleProperty hmin;
   private DoubleProperty vmin;
   private DoubleProperty hmax;
   private DoubleProperty vmax;
   private BooleanProperty fitToWidth;
   private BooleanProperty fitToHeight;
   private BooleanProperty pannable;
   private DoubleProperty prefViewportWidth;
   private DoubleProperty prefViewportHeight;
   private DoubleProperty minViewportWidth;
   private DoubleProperty minViewportHeight;
   private ObjectProperty viewportBounds;
   private static final String DEFAULT_STYLE_CLASS = "scroll-pane";
   private static final PseudoClass PANNABLE_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("pannable");
   private static final PseudoClass FIT_TO_WIDTH_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("fitToWidth");
   private static final PseudoClass FIT_TO_HEIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("fitToHeight");

   public ScrollPane() {
      this.getStyleClass().setAll((Object[])("scroll-pane"));
      this.setAccessibleRole(AccessibleRole.SCROLL_PANE);
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
   }

   public ScrollPane(Node var1) {
      this();
      this.setContent(var1);
   }

   public final void setHbarPolicy(ScrollBarPolicy var1) {
      this.hbarPolicyProperty().set(var1);
   }

   public final ScrollBarPolicy getHbarPolicy() {
      return this.hbarPolicy == null ? ScrollPane.ScrollBarPolicy.AS_NEEDED : (ScrollBarPolicy)this.hbarPolicy.get();
   }

   public final ObjectProperty hbarPolicyProperty() {
      if (this.hbarPolicy == null) {
         this.hbarPolicy = new StyleableObjectProperty(ScrollPane.ScrollBarPolicy.AS_NEEDED) {
            public CssMetaData getCssMetaData() {
               return ScrollPane.StyleableProperties.HBAR_POLICY;
            }

            public Object getBean() {
               return ScrollPane.this;
            }

            public String getName() {
               return "hbarPolicy";
            }
         };
      }

      return this.hbarPolicy;
   }

   public final void setVbarPolicy(ScrollBarPolicy var1) {
      this.vbarPolicyProperty().set(var1);
   }

   public final ScrollBarPolicy getVbarPolicy() {
      return this.vbarPolicy == null ? ScrollPane.ScrollBarPolicy.AS_NEEDED : (ScrollBarPolicy)this.vbarPolicy.get();
   }

   public final ObjectProperty vbarPolicyProperty() {
      if (this.vbarPolicy == null) {
         this.vbarPolicy = new StyleableObjectProperty(ScrollPane.ScrollBarPolicy.AS_NEEDED) {
            public CssMetaData getCssMetaData() {
               return ScrollPane.StyleableProperties.VBAR_POLICY;
            }

            public Object getBean() {
               return ScrollPane.this;
            }

            public String getName() {
               return "vbarPolicy";
            }
         };
      }

      return this.vbarPolicy;
   }

   public final void setContent(Node var1) {
      this.contentProperty().set(var1);
   }

   public final Node getContent() {
      return this.content == null ? null : (Node)this.content.get();
   }

   public final ObjectProperty contentProperty() {
      if (this.content == null) {
         this.content = new SimpleObjectProperty(this, "content");
      }

      return this.content;
   }

   public final void setHvalue(double var1) {
      this.hvalueProperty().set(var1);
   }

   public final double getHvalue() {
      return this.hvalue == null ? 0.0 : this.hvalue.get();
   }

   public final DoubleProperty hvalueProperty() {
      if (this.hvalue == null) {
         this.hvalue = new SimpleDoubleProperty(this, "hvalue");
      }

      return this.hvalue;
   }

   public final void setVvalue(double var1) {
      this.vvalueProperty().set(var1);
   }

   public final double getVvalue() {
      return this.vvalue == null ? 0.0 : this.vvalue.get();
   }

   public final DoubleProperty vvalueProperty() {
      if (this.vvalue == null) {
         this.vvalue = new SimpleDoubleProperty(this, "vvalue");
      }

      return this.vvalue;
   }

   public final void setHmin(double var1) {
      this.hminProperty().set(var1);
   }

   public final double getHmin() {
      return this.hmin == null ? 0.0 : this.hmin.get();
   }

   public final DoubleProperty hminProperty() {
      if (this.hmin == null) {
         this.hmin = new SimpleDoubleProperty(this, "hmin", 0.0);
      }

      return this.hmin;
   }

   public final void setVmin(double var1) {
      this.vminProperty().set(var1);
   }

   public final double getVmin() {
      return this.vmin == null ? 0.0 : this.vmin.get();
   }

   public final DoubleProperty vminProperty() {
      if (this.vmin == null) {
         this.vmin = new SimpleDoubleProperty(this, "vmin", 0.0);
      }

      return this.vmin;
   }

   public final void setHmax(double var1) {
      this.hmaxProperty().set(var1);
   }

   public final double getHmax() {
      return this.hmax == null ? 1.0 : this.hmax.get();
   }

   public final DoubleProperty hmaxProperty() {
      if (this.hmax == null) {
         this.hmax = new SimpleDoubleProperty(this, "hmax", 1.0);
      }

      return this.hmax;
   }

   public final void setVmax(double var1) {
      this.vmaxProperty().set(var1);
   }

   public final double getVmax() {
      return this.vmax == null ? 1.0 : this.vmax.get();
   }

   public final DoubleProperty vmaxProperty() {
      if (this.vmax == null) {
         this.vmax = new SimpleDoubleProperty(this, "vmax", 1.0);
      }

      return this.vmax;
   }

   public final void setFitToWidth(boolean var1) {
      this.fitToWidthProperty().set(var1);
   }

   public final boolean isFitToWidth() {
      return this.fitToWidth == null ? false : this.fitToWidth.get();
   }

   public final BooleanProperty fitToWidthProperty() {
      if (this.fitToWidth == null) {
         this.fitToWidth = new StyleableBooleanProperty(false) {
            public void invalidated() {
               ScrollPane.this.pseudoClassStateChanged(ScrollPane.FIT_TO_WIDTH_PSEUDOCLASS_STATE, this.get());
            }

            public CssMetaData getCssMetaData() {
               return ScrollPane.StyleableProperties.FIT_TO_WIDTH;
            }

            public Object getBean() {
               return ScrollPane.this;
            }

            public String getName() {
               return "fitToWidth";
            }
         };
      }

      return this.fitToWidth;
   }

   public final void setFitToHeight(boolean var1) {
      this.fitToHeightProperty().set(var1);
   }

   public final boolean isFitToHeight() {
      return this.fitToHeight == null ? false : this.fitToHeight.get();
   }

   public final BooleanProperty fitToHeightProperty() {
      if (this.fitToHeight == null) {
         this.fitToHeight = new StyleableBooleanProperty(false) {
            public void invalidated() {
               ScrollPane.this.pseudoClassStateChanged(ScrollPane.FIT_TO_HEIGHT_PSEUDOCLASS_STATE, this.get());
            }

            public CssMetaData getCssMetaData() {
               return ScrollPane.StyleableProperties.FIT_TO_HEIGHT;
            }

            public Object getBean() {
               return ScrollPane.this;
            }

            public String getName() {
               return "fitToHeight";
            }
         };
      }

      return this.fitToHeight;
   }

   public final void setPannable(boolean var1) {
      this.pannableProperty().set(var1);
   }

   public final boolean isPannable() {
      return this.pannable == null ? false : this.pannable.get();
   }

   public final BooleanProperty pannableProperty() {
      if (this.pannable == null) {
         this.pannable = new StyleableBooleanProperty(false) {
            public void invalidated() {
               ScrollPane.this.pseudoClassStateChanged(ScrollPane.PANNABLE_PSEUDOCLASS_STATE, this.get());
            }

            public CssMetaData getCssMetaData() {
               return ScrollPane.StyleableProperties.PANNABLE;
            }

            public Object getBean() {
               return ScrollPane.this;
            }

            public String getName() {
               return "pannable";
            }
         };
      }

      return this.pannable;
   }

   public final void setPrefViewportWidth(double var1) {
      this.prefViewportWidthProperty().set(var1);
   }

   public final double getPrefViewportWidth() {
      return this.prefViewportWidth == null ? 0.0 : this.prefViewportWidth.get();
   }

   public final DoubleProperty prefViewportWidthProperty() {
      if (this.prefViewportWidth == null) {
         this.prefViewportWidth = new SimpleDoubleProperty(this, "prefViewportWidth");
      }

      return this.prefViewportWidth;
   }

   public final void setPrefViewportHeight(double var1) {
      this.prefViewportHeightProperty().set(var1);
   }

   public final double getPrefViewportHeight() {
      return this.prefViewportHeight == null ? 0.0 : this.prefViewportHeight.get();
   }

   public final DoubleProperty prefViewportHeightProperty() {
      if (this.prefViewportHeight == null) {
         this.prefViewportHeight = new SimpleDoubleProperty(this, "prefViewportHeight");
      }

      return this.prefViewportHeight;
   }

   public final void setMinViewportWidth(double var1) {
      this.minViewportWidthProperty().set(var1);
   }

   public final double getMinViewportWidth() {
      return this.minViewportWidth == null ? 0.0 : this.minViewportWidth.get();
   }

   public final DoubleProperty minViewportWidthProperty() {
      if (this.minViewportWidth == null) {
         this.minViewportWidth = new SimpleDoubleProperty(this, "minViewportWidth");
      }

      return this.minViewportWidth;
   }

   public final void setMinViewportHeight(double var1) {
      this.minViewportHeightProperty().set(var1);
   }

   public final double getMinViewportHeight() {
      return this.minViewportHeight == null ? 0.0 : this.minViewportHeight.get();
   }

   public final DoubleProperty minViewportHeightProperty() {
      if (this.minViewportHeight == null) {
         this.minViewportHeight = new SimpleDoubleProperty(this, "minViewportHeight");
      }

      return this.minViewportHeight;
   }

   public final void setViewportBounds(Bounds var1) {
      this.viewportBoundsProperty().set(var1);
   }

   public final Bounds getViewportBounds() {
      return (Bounds)(this.viewportBounds == null ? new BoundingBox(0.0, 0.0, 0.0, 0.0) : (Bounds)this.viewportBounds.get());
   }

   public final ObjectProperty viewportBoundsProperty() {
      if (this.viewportBounds == null) {
         this.viewportBounds = new SimpleObjectProperty(this, "viewportBounds", new BoundingBox(0.0, 0.0, 0.0, 0.0));
      }

      return this.viewportBounds;
   }

   protected Skin createDefaultSkin() {
      return new ScrollPaneSkin(this);
   }

   public static List getClassCssMetaData() {
      return ScrollPane.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case CONTENTS:
            return this.getContent();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public static enum ScrollBarPolicy {
      NEVER,
      ALWAYS,
      AS_NEEDED;
   }

   private static class StyleableProperties {
      private static final CssMetaData HBAR_POLICY;
      private static final CssMetaData VBAR_POLICY;
      private static final CssMetaData FIT_TO_WIDTH;
      private static final CssMetaData FIT_TO_HEIGHT;
      private static final CssMetaData PANNABLE;
      private static final List STYLEABLES;

      static {
         HBAR_POLICY = new CssMetaData("-fx-hbar-policy", new EnumConverter(ScrollBarPolicy.class), ScrollPane.ScrollBarPolicy.AS_NEEDED) {
            public boolean isSettable(ScrollPane var1) {
               return var1.hbarPolicy == null || !var1.hbarPolicy.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollPane var1) {
               return (StyleableProperty)var1.hbarPolicyProperty();
            }
         };
         VBAR_POLICY = new CssMetaData("-fx-vbar-policy", new EnumConverter(ScrollBarPolicy.class), ScrollPane.ScrollBarPolicy.AS_NEEDED) {
            public boolean isSettable(ScrollPane var1) {
               return var1.vbarPolicy == null || !var1.vbarPolicy.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollPane var1) {
               return (StyleableProperty)var1.vbarPolicyProperty();
            }
         };
         FIT_TO_WIDTH = new CssMetaData("-fx-fit-to-width", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(ScrollPane var1) {
               return var1.fitToWidth == null || !var1.fitToWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollPane var1) {
               return (StyleableProperty)var1.fitToWidthProperty();
            }
         };
         FIT_TO_HEIGHT = new CssMetaData("-fx-fit-to-height", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(ScrollPane var1) {
               return var1.fitToHeight == null || !var1.fitToHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollPane var1) {
               return (StyleableProperty)var1.fitToHeightProperty();
            }
         };
         PANNABLE = new CssMetaData("-fx-pannable", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(ScrollPane var1) {
               return var1.pannable == null || !var1.pannable.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollPane var1) {
               return (StyleableProperty)var1.pannableProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(HBAR_POLICY);
         var0.add(VBAR_POLICY);
         var0.add(FIT_TO_WIDTH);
         var0.add(FIT_TO_HEIGHT);
         var0.add(PANNABLE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
