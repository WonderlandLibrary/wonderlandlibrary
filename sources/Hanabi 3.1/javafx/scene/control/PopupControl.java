package javafx.scene.control;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.Logging;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class PopupControl extends PopupWindow implements Skinnable, Styleable {
   public static final double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;
   public static final double USE_COMPUTED_SIZE = -1.0;
   protected CSSBridge bridge = new CSSBridge();
   private final ObjectProperty skin = new ObjectPropertyBase() {
      private Skin oldValue;

      public void set(Skin var1) {
         if (var1 == null) {
            if (this.oldValue == null) {
               return;
            }
         } else if (this.oldValue != null && var1.getClass().equals(this.oldValue.getClass())) {
            return;
         }

         super.set(var1);
      }

      protected void invalidated() {
         Skin var1 = (Skin)this.get();
         PopupControl.this.currentSkinClassName = var1 == null ? null : var1.getClass().getName();
         PopupControl.this.skinClassNameProperty().set(PopupControl.this.currentSkinClassName);
         if (this.oldValue != null) {
            this.oldValue.dispose();
         }

         this.oldValue = (Skin)this.getValue();
         PopupControl.this.prefWidthCache = -1.0;
         PopupControl.this.prefHeightCache = -1.0;
         PopupControl.this.minWidthCache = -1.0;
         PopupControl.this.minHeightCache = -1.0;
         PopupControl.this.maxWidthCache = -1.0;
         PopupControl.this.maxHeightCache = -1.0;
         PopupControl.this.skinSizeComputed = false;
         Node var2 = PopupControl.this.getSkinNode();
         if (var2 != null) {
            PopupControl.this.bridge.getChildren().setAll((Object[])(var2));
         } else {
            PopupControl.this.bridge.getChildren().clear();
         }

         PopupControl.this.bridge.impl_reapplyCSS();
         PlatformLogger var3 = Logging.getControlsLogger();
         if (var3.isLoggable(Level.FINEST)) {
            var3.finest("Stored skin[" + this.getValue() + "] on " + this);
         }

      }

      public Object getBean() {
         return PopupControl.this;
      }

      public String getName() {
         return "skin";
      }
   };
   private String currentSkinClassName = null;
   private StringProperty skinClassName = null;
   private DoubleProperty minWidth;
   private DoubleProperty minHeight;
   private DoubleProperty prefWidth;
   private DoubleProperty prefHeight;
   private DoubleProperty maxWidth;
   private DoubleProperty maxHeight;
   private double prefWidthCache = -1.0;
   private double prefHeightCache = -1.0;
   private double minWidthCache = -1.0;
   private double minHeightCache = -1.0;
   private double maxWidthCache = -1.0;
   private double maxHeightCache = -1.0;
   private boolean skinSizeComputed = false;
   private static final CssMetaData SKIN;
   private static final List STYLEABLES;

   public PopupControl() {
      this.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
      this.getContent().add(this.bridge);
   }

   public final StringProperty idProperty() {
      return this.bridge.idProperty();
   }

   public final void setId(String var1) {
      this.idProperty().set(var1);
   }

   public final String getId() {
      return (String)this.idProperty().get();
   }

   public final ObservableList getStyleClass() {
      return this.bridge.getStyleClass();
   }

   public final void setStyle(String var1) {
      this.styleProperty().set(var1);
   }

   public final String getStyle() {
      return (String)this.styleProperty().get();
   }

   public final StringProperty styleProperty() {
      return this.bridge.styleProperty();
   }

   public final ObjectProperty skinProperty() {
      return this.skin;
   }

   public final void setSkin(Skin var1) {
      this.skinProperty().setValue(var1);
   }

   public final Skin getSkin() {
      return (Skin)this.skinProperty().getValue();
   }

   private StringProperty skinClassNameProperty() {
      if (this.skinClassName == null) {
         this.skinClassName = new StyleableStringProperty() {
            public void set(String var1) {
               if (var1 != null && !var1.isEmpty() && !var1.equals(this.get())) {
                  super.set(var1);
               }
            }

            public void invalidated() {
               if (this.get() != null && !this.get().equals(PopupControl.this.currentSkinClassName)) {
                  Control.loadSkinClass(PopupControl.this, this.get());
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "skinClassName";
            }

            public CssMetaData getCssMetaData() {
               return PopupControl.SKIN;
            }
         };
      }

      return this.skinClassName;
   }

   private Node getSkinNode() {
      return this.getSkin() == null ? null : this.getSkin().getNode();
   }

   public final void setMinWidth(double var1) {
      this.minWidthProperty().set(var1);
   }

   public final double getMinWidth() {
      return this.minWidth == null ? -1.0 : this.minWidth.get();
   }

   public final DoubleProperty minWidthProperty() {
      if (this.minWidth == null) {
         this.minWidth = new DoublePropertyBase(-1.0) {
            public void invalidated() {
               if (PopupControl.this.isShowing()) {
                  PopupControl.this.bridge.requestLayout();
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "minWidth";
            }
         };
      }

      return this.minWidth;
   }

   public final void setMinHeight(double var1) {
      this.minHeightProperty().set(var1);
   }

   public final double getMinHeight() {
      return this.minHeight == null ? -1.0 : this.minHeight.get();
   }

   public final DoubleProperty minHeightProperty() {
      if (this.minHeight == null) {
         this.minHeight = new DoublePropertyBase(-1.0) {
            public void invalidated() {
               if (PopupControl.this.isShowing()) {
                  PopupControl.this.bridge.requestLayout();
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "minHeight";
            }
         };
      }

      return this.minHeight;
   }

   public void setMinSize(double var1, double var3) {
      this.setMinWidth(var1);
      this.setMinHeight(var3);
   }

   public final void setPrefWidth(double var1) {
      this.prefWidthProperty().set(var1);
   }

   public final double getPrefWidth() {
      return this.prefWidth == null ? -1.0 : this.prefWidth.get();
   }

   public final DoubleProperty prefWidthProperty() {
      if (this.prefWidth == null) {
         this.prefWidth = new DoublePropertyBase(-1.0) {
            public void invalidated() {
               if (PopupControl.this.isShowing()) {
                  PopupControl.this.bridge.requestLayout();
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "prefWidth";
            }
         };
      }

      return this.prefWidth;
   }

   public final void setPrefHeight(double var1) {
      this.prefHeightProperty().set(var1);
   }

   public final double getPrefHeight() {
      return this.prefHeight == null ? -1.0 : this.prefHeight.get();
   }

   public final DoubleProperty prefHeightProperty() {
      if (this.prefHeight == null) {
         this.prefHeight = new DoublePropertyBase(-1.0) {
            public void invalidated() {
               if (PopupControl.this.isShowing()) {
                  PopupControl.this.bridge.requestLayout();
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "prefHeight";
            }
         };
      }

      return this.prefHeight;
   }

   public void setPrefSize(double var1, double var3) {
      this.setPrefWidth(var1);
      this.setPrefHeight(var3);
   }

   public final void setMaxWidth(double var1) {
      this.maxWidthProperty().set(var1);
   }

   public final double getMaxWidth() {
      return this.maxWidth == null ? -1.0 : this.maxWidth.get();
   }

   public final DoubleProperty maxWidthProperty() {
      if (this.maxWidth == null) {
         this.maxWidth = new DoublePropertyBase(-1.0) {
            public void invalidated() {
               if (PopupControl.this.isShowing()) {
                  PopupControl.this.bridge.requestLayout();
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "maxWidth";
            }
         };
      }

      return this.maxWidth;
   }

   public final void setMaxHeight(double var1) {
      this.maxHeightProperty().set(var1);
   }

   public final double getMaxHeight() {
      return this.maxHeight == null ? -1.0 : this.maxHeight.get();
   }

   public final DoubleProperty maxHeightProperty() {
      if (this.maxHeight == null) {
         this.maxHeight = new DoublePropertyBase(-1.0) {
            public void invalidated() {
               if (PopupControl.this.isShowing()) {
                  PopupControl.this.bridge.requestLayout();
               }

            }

            public Object getBean() {
               return PopupControl.this;
            }

            public String getName() {
               return "maxHeight";
            }
         };
      }

      return this.maxHeight;
   }

   public void setMaxSize(double var1, double var3) {
      this.setMaxWidth(var1);
      this.setMaxHeight(var3);
   }

   public final double minWidth(double var1) {
      double var3 = this.getMinWidth();
      if (var3 == -1.0) {
         if (this.minWidthCache == -1.0) {
            this.minWidthCache = this.recalculateMinWidth(var1);
         }

         return this.minWidthCache;
      } else {
         return var3 == Double.NEGATIVE_INFINITY ? this.prefWidth(var1) : var3;
      }
   }

   public final double minHeight(double var1) {
      double var3 = this.getMinHeight();
      if (var3 == -1.0) {
         if (this.minHeightCache == -1.0) {
            this.minHeightCache = this.recalculateMinHeight(var1);
         }

         return this.minHeightCache;
      } else {
         return var3 == Double.NEGATIVE_INFINITY ? this.prefHeight(var1) : var3;
      }
   }

   public final double prefWidth(double var1) {
      double var3 = this.getPrefWidth();
      if (var3 == -1.0) {
         if (this.prefWidthCache == -1.0) {
            this.prefWidthCache = this.recalculatePrefWidth(var1);
         }

         return this.prefWidthCache;
      } else {
         return var3 == Double.NEGATIVE_INFINITY ? this.prefWidth(var1) : var3;
      }
   }

   public final double prefHeight(double var1) {
      double var3 = this.getPrefHeight();
      if (var3 == -1.0) {
         if (this.prefHeightCache == -1.0) {
            this.prefHeightCache = this.recalculatePrefHeight(var1);
         }

         return this.prefHeightCache;
      } else {
         return var3 == Double.NEGATIVE_INFINITY ? this.prefHeight(var1) : var3;
      }
   }

   public final double maxWidth(double var1) {
      double var3 = this.getMaxWidth();
      if (var3 == -1.0) {
         if (this.maxWidthCache == -1.0) {
            this.maxWidthCache = this.recalculateMaxWidth(var1);
         }

         return this.maxWidthCache;
      } else {
         return var3 == Double.NEGATIVE_INFINITY ? this.prefWidth(var1) : var3;
      }
   }

   public final double maxHeight(double var1) {
      double var3 = this.getMaxHeight();
      if (var3 == -1.0) {
         if (this.maxHeightCache == -1.0) {
            this.maxHeightCache = this.recalculateMaxHeight(var1);
         }

         return this.maxHeightCache;
      } else {
         return var3 == Double.NEGATIVE_INFINITY ? this.prefHeight(var1) : var3;
      }
   }

   private double recalculateMinWidth(double var1) {
      this.recomputeSkinSize();
      return this.getSkinNode() == null ? 0.0 : this.getSkinNode().minWidth(var1);
   }

   private double recalculateMinHeight(double var1) {
      this.recomputeSkinSize();
      return this.getSkinNode() == null ? 0.0 : this.getSkinNode().minHeight(var1);
   }

   private double recalculateMaxWidth(double var1) {
      this.recomputeSkinSize();
      return this.getSkinNode() == null ? 0.0 : this.getSkinNode().maxWidth(var1);
   }

   private double recalculateMaxHeight(double var1) {
      this.recomputeSkinSize();
      return this.getSkinNode() == null ? 0.0 : this.getSkinNode().maxHeight(var1);
   }

   private double recalculatePrefWidth(double var1) {
      this.recomputeSkinSize();
      return this.getSkinNode() == null ? 0.0 : this.getSkinNode().prefWidth(var1);
   }

   private double recalculatePrefHeight(double var1) {
      this.recomputeSkinSize();
      return this.getSkinNode() == null ? 0.0 : this.getSkinNode().prefHeight(var1);
   }

   private void recomputeSkinSize() {
      if (!this.skinSizeComputed) {
         this.bridge.applyCss();
         this.skinSizeComputed = true;
      }

   }

   protected Skin createDefaultSkin() {
      return null;
   }

   public static List getClassCssMetaData() {
      return STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public final void pseudoClassStateChanged(PseudoClass var1, boolean var2) {
      this.bridge.pseudoClassStateChanged(var1, var2);
   }

   public String getTypeSelector() {
      return "PopupControl";
   }

   public Styleable getStyleableParent() {
      Node var1 = this.getOwnerNode();
      if (var1 != null) {
         return var1;
      } else {
         Window var2 = this.getOwnerWindow();
         if (var2 != null) {
            Scene var3 = var2.getScene();
            if (var3 != null) {
               return var3.getRoot();
            }
         }

         return this.bridge.getParent();
      }
   }

   public final ObservableSet getPseudoClassStates() {
      return FXCollections.emptyObservableSet();
   }

   /** @deprecated */
   @Deprecated
   public Node impl_styleableGetNode() {
      return this.bridge;
   }

   static {
      if (Application.getUserAgentStylesheet() == null) {
         PlatformImpl.setDefaultPlatformUserAgentStylesheet();
      }

      SKIN = new CssMetaData("-fx-skin", StringConverter.getInstance()) {
         public boolean isSettable(CSSBridge var1) {
            return !var1.popupControl.skinProperty().isBound();
         }

         public StyleableProperty getStyleableProperty(CSSBridge var1) {
            return (StyleableProperty)var1.popupControl.skinClassNameProperty();
         }
      };
      ArrayList var0 = new ArrayList();
      Collections.addAll(var0, new CssMetaData[]{SKIN});
      STYLEABLES = Collections.unmodifiableList(var0);
   }

   protected class CSSBridge extends Pane {
      private final PopupControl popupControl = PopupControl.this;

      public void requestLayout() {
         PopupControl.this.prefWidthCache = -1.0;
         PopupControl.this.prefHeightCache = -1.0;
         PopupControl.this.minWidthCache = -1.0;
         PopupControl.this.minHeightCache = -1.0;
         PopupControl.this.maxWidthCache = -1.0;
         PopupControl.this.maxHeightCache = -1.0;
         super.requestLayout();
      }

      public Styleable getStyleableParent() {
         return PopupControl.this.getStyleableParent();
      }

      /** @deprecated */
      @Deprecated
      protected void setSkinClassName(String var1) {
      }

      public List getCssMetaData() {
         return PopupControl.this.getCssMetaData();
      }

      /** @deprecated */
      @Deprecated
      public List impl_getAllParentStylesheets() {
         Styleable var1 = this.getStyleableParent();
         return var1 instanceof Parent ? ((Parent)var1).impl_getAllParentStylesheets() : null;
      }

      /** @deprecated */
      @Deprecated
      protected void impl_processCSS(WritableValue var1) {
         super.impl_processCSS(var1);
         if (PopupControl.this.getSkin() == null) {
            Skin var2 = PopupControl.this.createDefaultSkin();
            if (var2 != null) {
               PopupControl.this.skinProperty().set(var2);
               super.impl_processCSS(var1);
            } else {
               String var3 = "The -fx-skin property has not been defined in CSS for " + this + " and createDefaultSkin() returned null.";
               ObservableList var4 = StyleManager.getErrors();
               if (var4 != null) {
                  CssError var5 = new CssError(var3);
                  var4.add(var5);
               }

               Logging.getControlsLogger().severe(var3);
            }
         }

      }
   }
}
